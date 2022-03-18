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

package io.siddhi.extension.io.sqs.sink;

import io.siddhi.core.util.transport.OptionHolder;
import io.siddhi.extension.io.sqs.util.SQSConfig;
import io.siddhi.extension.io.sqs.util.SQSConstants;

/**
 * Bean class to keep the information related to the SQS Sink.
 */
public class SQSSinkConfig extends SQSConfig {
    private int delayIntervalTime = SQSConstants.DEFAULT_DELAY_INTERVAL;

    public SQSSinkConfig(OptionHolder optionHolder) {
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
                case SQSConstants.DELAY_INTERVAL_NAME:
                    this.setDelayIntervalTime(getIntegerOptionValue(optionHolder, key));
                    break;
                default:
                    // not an option.
            }
        });
    }

    public int getDelayIntervalTime() {
        return delayIntervalTime;
    }

    public void setDelayIntervalTime(int delayIntervalTime) {
        this.delayIntervalTime = delayIntervalTime;
    }
}
