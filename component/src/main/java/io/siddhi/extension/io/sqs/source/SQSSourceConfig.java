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

import io.siddhi.core.util.transport.OptionHolder;
import io.siddhi.extension.io.sqs.util.SQSConfig;
import io.siddhi.extension.io.sqs.util.SQSConstants;

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
                case SQSConstants.QUEUE_URL_NAME:
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
                // START Customisation to support delegation (a.k.a. "Assume Role")
                case SQSConstants.USE_DELEGATION_NAME:
                    super.setUseDelegation(getBooleanValue(optionHolder, key));
                    break;
                case SQSConstants.ROLE_ARN_NAME:
                    super.setRoleArn(optionHolder.validateAndGetStaticValue(key));
                    break;    
                case SQSConstants.ROLE_SESSION_NAME:
                    super.setRoleSessionName(optionHolder.validateAndGetStaticValue(key));
                    break;
                // END Customisation to support delegation (a.k.a. "Assume Role")
                case SQSConstants.WAIT_TIME_NAME:
                    this.waitTime = getIntegerOptionValue(optionHolder, key);
                    break;
                case SQSConstants.VISIBILITY_TIMEOUT_NAME:
                    this.visibilityTimeout = getIntegerOptionValue(optionHolder, key);
                    break;
                case SQSConstants.MAX_NUMBER_OF_MESSAGES_NAME:
                    this.maxNumberOfMessages = getIntegerOptionValue(optionHolder, key);
                    break;
                case SQSConstants.DELETE_MESSAGES_NAME:
                    this.deleteAfterConsume = getBooleanValue(optionHolder, key);
                    break;
                case SQSConstants.DELETE_RETRY_INTERVAL_NAME:
                    this.retryInterval = getIntegerOptionValue(optionHolder, key);
                    break;
                case SQSConstants.MAX_NUMBER_OF_DELETE_RETRY_ATTEMPTS_NAME:
                    this.retryCountLimit = getIntegerOptionValue(optionHolder, key);
                    break;
                case SQSConstants.POLLING_INTERVAL_NAME:
                    this.pollingInterval = getIntegerOptionValue(optionHolder, key);
                    break;
                case SQSConstants.PARALLEL_CONSUMERS_NAME:
                    this.threadPoolSize = getIntegerOptionValue(optionHolder, key);
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

    public int getVisibilityTimeout() {
        return visibilityTimeout;
    }

    public int getMaxNumberOfMessages() {
        return maxNumberOfMessages;
    }

    public boolean deleteAfterConsume() {
        return deleteAfterConsume;
    }

    public int getRetryCountLimit() {
        return retryCountLimit;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public int getPollingInterval() {
        return pollingInterval;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

}
