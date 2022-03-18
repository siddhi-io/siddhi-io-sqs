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

import java.util.Optional;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
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
    
    /** If there is the Role Arn as a system property, use it */
    private static final String ENV_ROLE_ARN = "AWS_SQS_ROLE_ARN";
    /** If there is the Role Session Name as a system property, use it */
    private static final String ENV_ROLE_SESSION_NAME = "AWS_SQS_ROLE_SESSION_NAME";


    public SQSBuilder(SQSConfig sqsConfig) {
        this.sqsConfig = sqsConfig;
        BasicAWSCredentials credentials = new BasicAWSCredentials(sqsConfig.getAccessKey(), sqsConfig.getSecretKey());

        try {
            if (!sqsConfig.isDelegationEnabled()) {
            	//Standard auth
                this.amazonSQS = AmazonSQSClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(sqsConfig.getRegion())
                    .build();
            } else {
            	//Assume Role auth
                EndpointConfiguration endpointConfiguration = new EndpointConfiguration("sqs." 
                            + sqsConfig.getRegion() + ".amazonaws.com", sqsConfig.getRegion());
                this.amazonSQS = AmazonSQSClientBuilder.standard()
                                    .withEndpointConfiguration(endpointConfiguration)
                                    .withCredentials(this.getSTSAssumeRoleSessionCredentialsProvider()).build();
            }
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
    
    /**
     * Return credentials to be used for exchanging long term credentials with temporary ones
     * @return
     */
    private AWSCredentialsProvider getSTSAssumeRoleSessionCredentialsProvider() {    
        return new STSAssumeRoleSessionCredentialsProvider.Builder(
                        this.envRoleArn(), 
                        this.envRoleSessionName()
                ).withStsClient(getAWSSecurityTokenService()).build();
    }
    
    /**
     * Return an instance of STS configured with long term credentials
     * @return
     */
    private AWSSecurityTokenService getAWSSecurityTokenService() {    

        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(envCredentials());

        return AWSSecurityTokenServiceClientBuilder.standard()
                .withRegion(this.sqsConfig.getRegion())
                .withCredentials(credentialsProvider)
                .build();
    }
    
    /**
     * Retrieve the configured short-term credentials.
     * TODO Offer the possibility of reading them from environment system properties, 
     * just like Role Arn and Role Session Name below
     * @return Short Term Credentials
     */
    private AWSCredentials envCredentials() {
        return new BasicAWSCredentials(sqsConfig.getAccessKey(), sqsConfig.getSecretKey());
    }
    
    /**
     * Retrieve the Role Arn as a system environment property.
     * If not defined, use the configured one
     * @return AWS Role Arn
     */
    private String envRoleArn() {
        Optional<String> roleArn = Optional.ofNullable(System.getenv(ENV_ROLE_ARN));
        return roleArn.orElse(sqsConfig.getRoleArn());
    }
    
    /**
     * Retrieve the Role Session Name as a system environment property.
     * If not defined, use the configured one
     * @return AWS Role Arn
     */
    private String envRoleSessionName() {
        Optional<String> roleSessionName = Optional.ofNullable(System.getenv(ENV_ROLE_SESSION_NAME));
        return roleSessionName.orElse(sqsConfig.getRoleSessionName());
    }
}
