/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.siddhi.extension.io.sqs.source;

import com.amazonaws.AbortedException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteMessageResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import io.siddhi.core.stream.input.source.SourceEventListener;
import io.siddhi.extension.io.sqs.util.SQSConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Runnable Task for polling from the queue.
 */
public class SQSSourceTask implements Runnable {
    private static final Logger logger = LogManager.getLogger(SQSSourceTask.class);

    private ReceiveMessageRequest messageRequest;
    private AmazonSQS amazonSQS;
    private SourceEventListener sourceEventListener;
    private SQSSourceConfig sourceConfig;
    private String[] reqTransportProperties;

    public SQSSourceTask(SQSSourceConfig sqsConfig, AmazonSQS amazonSQS, SourceEventListener sourceEventListener) {
        this.sourceEventListener = sourceEventListener;
        this.amazonSQS = amazonSQS;
        initializeRequest(sqsConfig);
    }

    @Override
    public void run() {
        try {
            boolean receivedEmptyMessageList = false;

            while (!receivedEmptyMessageList) {
                List<Message> messages = amazonSQS.receiveMessage(messageRequest).getMessages();
                if (messages != null && !messages.isEmpty()) {
                    messages.forEach(message -> {
                        String[] transportProperties = getTransportProperties(message, reqTransportProperties);
                        this.sourceEventListener.onEvent(message.getBody(), transportProperties);
                        if (sourceConfig.deleteAfterConsume()) {
                            delete(message);
                        }
                    });
                } else {
                    receivedEmptyMessageList = true;
                }
            }
        } catch (AbortedException e) {
            // Exception is thrown when stopping the queue via the Future call.
            logger.info("Executor thread aborted.");
        } catch (SdkClientException e) {
            // Exception is thrown when there is a limit exceed in the configuration provided.
            logger.error("Error occurred while trying to receive messages from the queue. Hence waiting for " +
                    "polling cycle.\n" + e.getMessage(), e);
        }

    }

    private void initializeRequest(SQSSourceConfig sqsConfig) {
        this.sourceConfig = sqsConfig;
        this.reqTransportProperties = sourceConfig.getRequestedTransportProperties();

        this.messageRequest = new ReceiveMessageRequest()
                .withQueueUrl(this.sourceConfig.getQueueUrl())
                .withMaxNumberOfMessages(this.sourceConfig.getMaxNumberOfMessages());

        if (this.sourceConfig.getWaitTime() != -1) {
            messageRequest = messageRequest.withWaitTimeSeconds(this.sourceConfig.getWaitTime());
        }

        if (this.sourceConfig.getVisibilityTimeout() != -1) {
            messageRequest = messageRequest.withVisibilityTimeout(this.sourceConfig.getVisibilityTimeout());
        }
    }

    private void delete(Message message) {
        int deleteRetryCount = 0;
        while (!deleteMessageFromQueue(message) && (deleteRetryCount < sourceConfig.getRetryCountLimit())) {
            deleteRetryCount++;
            try {
                Thread.sleep(sourceConfig.getRetryInterval());
            } catch (InterruptedException e) {
                logger.info("SQS source thread got interrupted during delete retry.");
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean deleteMessageFromQueue(Message message) {
        String messageReceiptHandle = message.getReceiptHandle();
        try {
            DeleteMessageResult deleteMessageResult =
                    amazonSQS.deleteMessage(new DeleteMessageRequest(sourceConfig.getQueueUrl(), messageReceiptHandle));
            return deleteMessageResult.getSdkHttpMetadata().getHttpStatusCode() == SQSConstants.HTTP_SUCCESS;
        } catch (SdkClientException e) {
            logger.error(String.format("Failed to delete message '%s' from the queue '%s'. Hence retrying.",
                    message.getBody(), sourceConfig.getQueueUrl()), e);
            return false;
        }
    }

    private String[] getTransportProperties(Message message, String[] reqTransportProperties) {
        String[] transportProperties = new String[reqTransportProperties.length];

        for (int i = 0; i < reqTransportProperties.length; i++) {
            if (reqTransportProperties[i].equalsIgnoreCase(SQSConstants.MESSAGE_ID_PROPERTY_NAME)) {
                transportProperties[i] = message.getMessageId();
            }
        }

        return transportProperties;
    }

}
