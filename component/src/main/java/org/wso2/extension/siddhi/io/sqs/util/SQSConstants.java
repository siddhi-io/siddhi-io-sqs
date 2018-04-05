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

package org.wso2.extension.siddhi.io.sqs.util;

/**
 * Class to keep constants related to the Sources and Sinks.
 */
public class SQSConstants {

    // Option parameter names
    public static final String QUEUE_NAME = "queue";
    public static final String ACCESS_KEY_NAME = "access.key";
    public static final String SECRET_KEY_NAME = "secret.key";
    public static final String REGION_NAME = "region";
    public static final String POLLING_INTERVAL_NAME = "polling.interval";
    public static final String WAITING_TIME_NAME = "waiting.time";
    public static final String MAX_NUMBER_OF_MESSAGES_NAME = "max.number.of.messages";
    public static final String VISIBILITY_TIMEOUT_NAME = "visibility.timeout";
    public static final String DELETE_MESSAGES_NAME = "delete.messages";
    public static final String DELETE_RETRY_INTERVAL_NAME = "delete.retry.interval";
    public static final String MAX_NUMBER_OF_DELETE_RETRY_ATTEMPTS_NAME = "max.number.of.delete.retry.attempts";
    public static final String PARALLEL_CONSUMERS_NAME = "number.of.parallel.consumers";

    // transport properties
    public static final String MESSAGE_ID_PROPERTY_NAME = "MESSAGE_ID";

    // Default Options
    public static final int DEFAULT_MAX_NUMBER_OF_MESSAGES = 1;
    public static final boolean DEFAULT_DELETE_AFTER_CONSUME = true;
    public static final int DEFAULT_RETRY_COUNT_LIMIT = 10;
    public static final int DEFAULT_RETRY_INTERVAL = 5000;
    public static final int DEFAULT_POLLING_INTERVAL = 1000;
    public static final int DEFAULT_VISIBILITY_TIMEOUT = -1;
    public static final int DEFAULT_WAITING_TIME = -1;
    public static final int DEFAULT_PARALLEL_CONSUMERS = 1;
    public static final boolean DEFAULT_BATCH_MESSAGES = false;
    public static final int DEFAULT_MESSAGES_PER_BATCH = 10;
    public static final int DEFAULT_BATCH_MESSAGE_TIMEOUT = 60;

    // HTTP Constants
    public static final int HTTP_SUCCESS = 200;

    private SQSConstants() {
    }
}
