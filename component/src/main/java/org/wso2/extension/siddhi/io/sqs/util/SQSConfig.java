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

import org.wso2.siddhi.core.util.transport.OptionHolder;
import org.wso2.siddhi.query.api.exception.SiddhiAppValidationException;

import java.util.InputMismatchException;

/**
 * Class to keep configuration of a AmazonSQS object.
 */
public class SQSConfig {
    private String accessKey;
    private String secretKey;
    private String queueUrl;
    private String region;

    public String getAccessKey() {
        return accessKey;
    }

    protected void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    protected void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getQueueUrl() {
        return queueUrl;
    }

    protected void setQueueUrl(String queueUrl) {
        this.queueUrl = queueUrl;
    }

    public String getRegion() {
        return region;
    }

    protected void setRegion(String region) {
        this.region = region;
    }

    protected int getIntegerOptionValue(OptionHolder optionHolder, String key) {
        try {
            return Integer.parseInt(optionHolder.validateAndGetStaticValue(key));
        } catch (InputMismatchException e) {
            throw new SiddhiAppValidationException(
                    String.format("Option value provided for attribute %s is not of type Integer.", key), e);
        }
    }

    protected boolean getBooleanValue(OptionHolder optionHolder, String key) {
        try {
            return Boolean.parseBoolean(optionHolder.validateAndGetStaticValue(key));
        } catch (InputMismatchException e) {
            throw new SiddhiAppValidationException(
                    String.format("Option value provided for attribute %s is not of type Boolean.", key), e);
        }
    }
}
