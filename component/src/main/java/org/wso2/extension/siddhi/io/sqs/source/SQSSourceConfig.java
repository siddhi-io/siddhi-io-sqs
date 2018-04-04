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

package org.wso2.extension.siddhi.io.sqs.source;

import org.wso2.extension.siddhi.io.sqs.util.SQSConfig;
import org.wso2.extension.siddhi.io.sqs.util.SQSConstants;
import org.wso2.siddhi.core.util.transport.OptionHolder;

/**
 * Model class for a Source configuration.
 */
public class SQSSourceConfig extends SQSConfig {

    private int waitTime = SQSConstants.DEFAULT_WAITING_TIME;
    private int visibilityTimeout = SQSConstants.DEFAULT_VISIBILITY_TIMEOUT;
    private int maxNumberOfMessages = SQSConstants.DEFAULT_MAX_NUMBER_OF_MESSAGES;
    private boolean deleteAfterConsume = SQSConstants.DEFAULT_DELETE_AFTER_CONSUME;
    private int retryCountLimit = SQSConstants.DEFAULT_RETRY_COUNT_LIMIT;
    private int retryInterval = SQSConstants.DEFAULT_RETRY_INTERVAL;
    private int pollingInterval = SQSConstants.DEFAULT_POLLING_INTERVAL;
    private int threadPoolSize = SQSConstants.DEFAULT_PARALLEL_CONSUMERS;
    private String[] requestedTransportProperties;

    public SQSSourceConfig(OptionHolder optionHolder, String[] requestedTransportProperties) {

        this.requestedTransportProperties = requestedTransportProperties.clone();
        optionHolder.getStaticOptionsKeys().forEach(key -> {
            switch (key.toLowerCase()) {
                case SQSConstants.QUEUE_NAME:
                    super.setQueueUrl(optionHolder.validateAndGetStaticValue(key));
                    break;
                case SQSConstants.ACCESS_KEY_NAME:
                    super.setAccessKey(optionHolder.validateAndGetStaticValue(key));
                    break;
                case SQSConstants.SECRET_KEY_NAME:
                    super.setSecretKey(optionHolder.validateAndGetStaticValue(key));
                    break;
                case SQSConstants.REGION_NAME:
                    super.setRegion(optionHolder.validateAndGetStaticValue(key));
                    break;
                case SQSConstants.WAITING_TIME_NAME:
                    this.setWaitTime(getIntegerOptionValue(optionHolder, key, SQSConstants.DEFAULT_WAITING_TIME));
                    break;
                case SQSConstants.VISIBILITY_TIMEOUT_NAME:
                    this.setVisibilityTimeout(
                            getIntegerOptionValue(optionHolder, key, SQSConstants.DEFAULT_VISIBILITY_TIMEOUT));
                    break;
                case SQSConstants.MAX_NUMBER_OF_MESSAGES_NAME:
                    this.setMaxNumberOfMessages(
                            getIntegerOptionValue(optionHolder, key, SQSConstants.DEFAULT_MAX_NUMBER_OF_MESSAGES));
                    break;
                case SQSConstants.PURGE_MESSAGES_NAME:
                    this.setDeleteAfterConsume(
                            getBooleanValue(optionHolder, key, SQSConstants.DEFAULT_DELETE_AFTER_CONSUME));
                    break;
                case SQSConstants.DELETE_RETRY_INTERVAL_NAME:
                    this.setRetryInterval(
                            getIntegerOptionValue(optionHolder, key, SQSConstants.DEFAULT_RETRY_INTERVAL));
                    break;
                case SQSConstants.MAX_NUMBER_OF_DELETE_RETRY_ATTEMPTS_NAME:
                    this.setRetryCountLimit(
                            getIntegerOptionValue(optionHolder, key, SQSConstants.DEFAULT_RETRY_COUNT_LIMIT));
                    break;
                case SQSConstants.POLLING_INTERVAL_NAME:
                    this.setPollingInterval(
                            getIntegerOptionValue(optionHolder, key, SQSConstants.DEFAULT_POLLING_INTERVAL));
                    break;
                case SQSConstants.PARALLEL_CONSUMERS_NAME:
                    this.setThreadPoolSize(
                            getIntegerOptionValue(optionHolder, key, SQSConstants.DEFAULT_PARALLEL_CONSUMERS));
                    break;
                default:
                    // not a supported option.
                    break;
            }
        });
    }

    public String[] getRequestedTransportProperties() {
        return requestedTransportProperties.clone();
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getVisibilityTimeout() {
        return visibilityTimeout;
    }

    public void setVisibilityTimeout(int visibilityTimeout) {
        this.visibilityTimeout = visibilityTimeout;
    }

    public int getMaxNumberOfMessages() {
        return maxNumberOfMessages;
    }

    public void setMaxNumberOfMessages(int maxNumberOfMessages) {
        this.maxNumberOfMessages = maxNumberOfMessages;
    }

    public boolean deleteAfterConsume() {
        return deleteAfterConsume;
    }

    public void setDeleteAfterConsume(boolean deleteAfterConsume) {
        this.deleteAfterConsume = deleteAfterConsume;
    }

    public int getRetryCountLimit() {
        return retryCountLimit;
    }

    public void setRetryCountLimit(int retryCountLimit) {
        this.retryCountLimit = retryCountLimit;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    public int getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(int pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
}
