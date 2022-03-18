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

import io.siddhi.annotation.Example;
import io.siddhi.annotation.Extension;
import io.siddhi.annotation.Parameter;
import io.siddhi.annotation.util.DataType;
import io.siddhi.core.config.SiddhiAppContext;
import io.siddhi.core.exception.ConnectionUnavailableException;
import io.siddhi.core.stream.ServiceDeploymentInfo;
import io.siddhi.core.stream.output.sink.Sink;
import io.siddhi.core.util.config.ConfigReader;
import io.siddhi.core.util.snapshot.state.State;
import io.siddhi.core.util.snapshot.state.StateFactory;
import io.siddhi.core.util.transport.DynamicOptions;
import io.siddhi.core.util.transport.OptionHolder;
import io.siddhi.extension.io.sqs.api.SQSBuilder;
import io.siddhi.extension.io.sqs.util.SQSConstants;
import io.siddhi.query.api.definition.StreamDefinition;
import io.siddhi.query.api.exception.SiddhiAppValidationException;

/**
 * SQS_ar (Assume Role) Sink Extension
 */

@Extension(
        name = "sqs_ar",
        namespace = "sink",
        description = "SQS sink allows users to connect and publish messages to an AWS SQS Queue. It has the" +
                " ability to only publish Text messages",
        parameters = {
                @Parameter(
                        name = SQSConstants.QUEUE_URL_NAME,
                        description = "Queue url which SQS Sink should connect to",
                        type = DataType.STRING
                ),
                @Parameter(
                        name = SQSConstants.ACCESS_KEY_NAME,
                        description = "Access Key for the Amazon Web Services. (This is a mandatory field and should " +
                                "be provided either in the deployment.yml or in the sink definition itself)",
                        type = DataType.STRING,
                        optional = true,
                        defaultValue = "none"
                ),
                @Parameter(
                        name = SQSConstants.SECRET_KEY_NAME,
                        description = "Secret Key of the Amazon User. (This is a mandatory field and should " +
                                "be provided either in the deployment.yml or in the sink definition itself)",
                        type = DataType.STRING,
                        optional = true,
                        defaultValue = "none"
                ),
                @Parameter(
                        name = SQSConstants.REGION_NAME,
                        description = "Amazon Web Service Region",
                        type = DataType.STRING
                ),
                @Parameter(
                        name = SQSConstants.ROLE_ARN_NAME,
                        description = "Amazon Web Service Role ARN for role delegation",
                        type = DataType.STRING,
                        optional = true,
                        defaultValue = "none"
                ),
                @Parameter(
                        name = SQSConstants.ROLE_SESSION_NAME,
                        description = "Amazon Web Service Role Session Name for role delegation",
                        type = DataType.STRING,
                        optional = true,
                        defaultValue = "none"
                ),
                @Parameter(
                        name = SQSConstants.USE_DELEGATION_NAME,
                        description = "Enable Role Delegation",
                        type = DataType.BOOL,
                        optional = true,
                        defaultValue = "" + SQSConstants.DEFAULT_USE_DELEGATION
                ),
                @Parameter(
                        name = SQSConstants.MESSAGE_GROUP_ID_NAME,
                        description = "ID of the group that the message belong to(only applicable for FIFO Queues)",
                        type = DataType.STRING,
                        optional = true,
                        dynamic = true,
                        defaultValue = "null"
                ),
                @Parameter(
                        name = SQSConstants.DEDUPLICATION_ID_NAME,
                        description = "ID by which a FIFO queue identifies the duplication in the queue(only " +
                                "applicable for FIFO queues)",
                        type = DataType.STRING,
                        optional = true,
                        dynamic = true,
                        defaultValue = "null"
                ),
                @Parameter(
                        name = SQSConstants.DELAY_INTERVAL_NAME,
                        description = "Time in seconds for how long the message remain in the queue until it is " +
                                "available for the consumers to consume.",
                        type = DataType.INT,
                        optional = true,
                        defaultValue = "" + SQSConstants.DEFAULT_DELAY_INTERVAL
                )
        },
        examples = {
                @Example(
                        syntax = "@sink(type='sqs_ar'," +
                                "queue='https://amazon.sqs.queue.url'," +
                                "access.key='aws.access.key'," +
                                "secret.key='aws.secret.key'," +
                                "use.delegation='true'," +
                                "role.arn='arn:aws:iam::123456789012:role/some-role-name'," +
                                "role.session.name='some-session-name'," +
                                "region='us-east-1'," +
                                "delay.interval='5'," +
                                "message.group.id='group-1',@map(type='xml') )" +
                                "define stream outStream(symbol string, deduplicationID string);",
                        description = "" +
                                "Above example demonstrate how an SQS sink is getting configured in order to publish " +
                                "messages to a SQS queue.\n" +
                                "Once an event is received by outStream, an xml message will be generated by 'xml' " +
                                "mapper from the attribute values of the event. Then SQS sink will connect to the " +
                                "queue using provided configurations and send the message to the queue.\n"
                ),
                @Example(
                        syntax = "@sink(type='sqs_ar'," +
                                "queue='https://amazon.sqs.queue.fifo'," +
                                "access.key='aws.access.key'," +
                                "secret.key='aws.secret.key'," +
                                "use.delegation='true'," +
                                "role.arn='arn:aws:iam::123456789012:role/some-role-name'," +
                                "role.session.name='some-session-name'," +
                                "region='us-east-1'," +
                                "delay.interval='5'," +
                                "deduplication.id='{{deduplicationID}}'," +
                                "message.group.id='group-1',@map(type='xml') )" +
                                "define stream outStream(symbol string, deduplicationID string);",
                        description = "" +
                                "Above example demonstrate how an SQS sink is getting configured in order to publish " +
                                "messages to a SQS FIFO queue.\n" +
                                "Once an event is received by outStream, an xml message will be generated by 'xml' " +
                                "mapper from the attribute values of the event. SQS sink will connect to the " +
                                "queue using provided configurations and send the messages to the queue.\n" +
                                "For each message deduplciation id will be selected from the attriibute " +
                                "'deduplicationID' in the outStream.\n"
                )
        }
)

// for more information refer https://wso2.github.io/siddhi/documentation/siddhi-4.0/#sinks

public class SQSSink extends Sink {
    private SQSSinkConfig sinkConfig;
    private SQSMessagePublisher sqsMessagePublisher;
    private OptionHolder optionHolder;

    /**
     * Returns the list of classes which this sink can consume.
     * Based on the type of the sink, it may be limited to being able to publish specific type of classes.
     * For example, a sink of type file can only write objects of type String .
     *
     * @return array of supported classes , if extension can support of any types of classes
     * then return empty array .
     */
    @Override
    public Class[] getSupportedInputEventClasses() {
        return new Class[] {String.class};
    }

    @Override
    protected ServiceDeploymentInfo exposeServiceDeploymentInfo() {
        return null;
    }

    /**
     * Returns a list of supported dynamic options (that means for each event value of the option can change) by
     * the transport
     *
     * @return the list of supported dynamic option keys
     */
    @Override
    public String[] getSupportedDynamicOptions() {
        return new String[] {SQSConstants.MESSAGE_GROUP_ID_NAME, SQSConstants.DEDUPLICATION_ID_NAME};
    }

    /**
     * The initialization method for {@link Sink}, will be called before other methods. It used to validate
     * all configurations and to get initial values.
     *
     * @param streamDefinition containing stream definition bind to the {@link Sink}
     * @param optionHolder     Option holder containing static and dynamic configuration related
     *                         to the {@link Sink}
     * @param configReader     to read the sink related system configuration.
     * @param siddhiAppContext the context of the {@link io.siddhi.query.api.SiddhiApp} used to
     *                         get siddhi related utility functions.
     */
    @Override
    protected StateFactory init(StreamDefinition streamDefinition, OptionHolder optionHolder, ConfigReader configReader,
                                SiddhiAppContext siddhiAppContext) {
        this.sinkConfig = new SQSSinkConfig(optionHolder);
        this.optionHolder = optionHolder;
        this.sqsMessagePublisher = null;

        if (this.sinkConfig.getAccessKey() == null || sinkConfig.getAccessKey().isEmpty()) {
            this.sinkConfig.setAccessKey(configReader.readConfig(SQSConstants.ACCESS_KEY_NAME, null));
        }

        if (this.sinkConfig.getSecretKey() == null || sinkConfig.getSecretKey().isEmpty()) {
            this.sinkConfig.setSecretKey(configReader.readConfig(SQSConstants.SECRET_KEY_NAME, null));
        }

        if (sinkConfig.getAccessKey() == null || sinkConfig.getSecretKey() == null ||
                sinkConfig.getAccessKey().isEmpty() || sinkConfig.getSecretKey().isEmpty()) {
            throw new SiddhiAppValidationException("Access key and Secret key are mandatory parameters for" +
                    " the SQS client");
        }
        
        // START Customisation to support delegation (a.k.a. "Assume Role")
        if (this.sinkConfig.getRoleArn() == null || sinkConfig.getRoleArn().isEmpty()) {
            this.sinkConfig.setRoleArn(configReader.readConfig(SQSConstants.ROLE_ARN_NAME, null));
        }
        
        if (this.sinkConfig.getRoleSessionName() == null || sinkConfig.getRoleSessionName().isEmpty()) {
            this.sinkConfig.setRoleSessionName(configReader.readConfig(SQSConstants.ROLE_SESSION_NAME, null));
        }
        // END Customisation to support delegation (a.k.a. "Assume Role")
        
        return null;
    }

    /**
     * This method will be called when events need to be published via this sink
     *
     * @param payload        payload of the event based on the supported event class exported by the extensions
     * @param dynamicOptions holds the dynamic options of this sink and Use this object to obtain dynamic options.
     * @throws ConnectionUnavailableException if end point is unavailable the ConnectionUnavailableException thrown
     *                                        such that the  system will take care retrying for connection
     */
    @Override
    public void publish(Object payload, DynamicOptions dynamicOptions, State state)
            throws ConnectionUnavailableException {
        sqsMessagePublisher.sendMessageRequest(payload, dynamicOptions);
    }

    /**
     * This method will be called before the processing method.
     * Intention to establish connection to publish event.
     *
     * @throws ConnectionUnavailableException if end point is unavailable the ConnectionUnavailableException thrown
     *                                        such that the  system will take care retrying for connection
     */
    @Override
    public void connect() throws ConnectionUnavailableException {
        sqsMessagePublisher = new SQSBuilder(sinkConfig)
                .buildSinkPublisher(optionHolder, checkFIFO(sinkConfig.getQueueUrl()));
    }

    /**
     * Called after all publishing is done, or when {@link ConnectionUnavailableException} is thrown
     * Implementation of this method should contain the steps needed to disconnect from the sink.
     */
    @Override
    public void disconnect() {
        // client uses a rest api
    }

    /**
     * The method can be called when removing an event receiver.
     * The cleanups that have to be done after removing the receiver could be done here.
     */
    @Override
    public void destroy() {
        // client uses a rest api
    }

    private boolean checkFIFO(String queueURL) {
        return (queueURL.endsWith(".fifo") ||
                queueURL.substring(0, queueURL.length() - 1).endsWith(".fifo"));
    }
}

