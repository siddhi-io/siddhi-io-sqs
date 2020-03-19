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

package io.siddhi.extension.io.sqs.util;

/**
 * Class to keep constants related to the Sources and Sinks.
 */
public class SQSConstants {

    // general properties for sqs client
    public static final String ACCESS_KEY_NAME = "access.key";
    public static final String SECRET_KEY_NAME = "secret.key";
    public static final String REGION_NAME = "region";
    public static final String QUEUE_URL_NAME = "queue";
    // Customisation to support delegation (a.k.a. "Assume Role")
    public static final String ROLE_ARN_NAME = "role.arn";
    public static final String ROLE_SESSION_NAME = "role.session.name";
    public static final String USE_DELEGATION_NAME = "use.delegation";

    // Option parameter names for source
    public static final String POLLING_INTERVAL_NAME = "polling.interval";
    public static final String WAIT_TIME_NAME = "wait.time";
    public static final String MAX_NUMBER_OF_MESSAGES_NAME = "max.number.of.messages";
    public static final String VISIBILITY_TIMEOUT_NAME = "visibility.timeout";
    public static final String DELETE_MESSAGES_NAME = "delete.messages";
    public static final String DELETE_RETRY_INTERVAL_NAME = "delete.retry.interval";
    public static final String MAX_NUMBER_OF_DELETE_RETRY_ATTEMPTS_NAME = "max.number.of.delete.retry.attempts";
    public static final String PARALLEL_CONSUMERS_NAME = "number.of.parallel.consumers";

    // Option parameters for sink
    public static final String MESSAGE_GROUP_ID_NAME = "message.group.id";
    public static final String DEDUPLICATION_ID_NAME = "deduplication.id";
    public static final String DELAY_INTERVAL_NAME = "delay.interval";

    // transport properties (only for source)
    public static final String MESSAGE_ID_PROPERTY_NAME = "MESSAGE_ID";

    // source default Options
    public static final int DEFAULT_MAX_NUMBER_OF_MESSAGES = 1;
    public static final boolean DEFAULT_DELETE_AFTER_CONSUME = true;
    public static final int DEFAULT_RETRY_COUNT_LIMIT = 10;
    public static final int DEFAULT_RETRY_INTERVAL = 5000;
    public static final int DEFAULT_POLLING_INTERVAL = 1000;
    public static final int DEFAULT_VISIBILITY_TIMEOUT = -1;
    public static final int DEFAULT_WAITING_TIME = -1;
    public static final int DEFAULT_PARALLEL_CONSUMERS = 1;

    // sink default options
    public static final int DEFAULT_DELAY_INTERVAL = -1;

    // HTTP Constants
    public static final int HTTP_SUCCESS = 200;
    
    // Customisation to support delegation (a.k.a. "Assume Role")
    public static final boolean DEFAULT_USE_DELEGATION = false;
    

    private SQSConstants() {
    }
}
