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

package io.siddhi.extension.io.sqs.api;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import io.siddhi.core.exception.SiddhiAppRuntimeException;
import io.siddhi.core.stream.input.source.SourceEventListener;
import io.siddhi.core.util.transport.OptionHolder;
import io.siddhi.extension.io.sqs.sink.SQSMessagePublisher;
import io.siddhi.extension.io.sqs.sink.SQSSinkConfig;
import io.siddhi.extension.io.sqs.source.SQSSourceConfig;
import io.siddhi.extension.io.sqs.source.SQSSourceTask;
import io.siddhi.extension.io.sqs.util.SQSConfig;

/**
 * Class to provide the executable tasks for the Source and Sink.
 */

public class SQSBuilder {
    private AmazonSQS amazonSQS;
    private SQSConfig sqsConfig;

    public SQSBuilder(SQSConfig sqsConfig) {
        this.sqsConfig = sqsConfig;
        BasicAWSCredentials credentials = new BasicAWSCredentials(sqsConfig.getAccessKey(), sqsConfig.getSecretKey());

        try {
            this.amazonSQS = AmazonSQSClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(sqsConfig.getRegion())
                    .build();
        } catch (SdkClientException e) {
            throw new SiddhiAppRuntimeException(
                    "Failed to create SQS client due to invalid configuration. " + e.getMessage(), e);
        }

    }

    public SQSSourceTask buildSourceTask(SourceEventListener sourceEventListener) {
        if (sqsConfig instanceof SQSSourceConfig) {
            return new SQSSourceTask((SQSSourceConfig) sqsConfig, amazonSQS, sourceEventListener);
        }

        return null;
    }

    public SQSMessagePublisher buildSinkPublisher(OptionHolder optionHolder, boolean isFIFO) {
        if (sqsConfig instanceof SQSSinkConfig) {
            return new SQSMessagePublisher((SQSSinkConfig) sqsConfig, amazonSQS, optionHolder, isFIFO);
        }

        return null;
    }
}
