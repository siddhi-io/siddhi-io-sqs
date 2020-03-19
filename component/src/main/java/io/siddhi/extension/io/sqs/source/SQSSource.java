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

import io.siddhi.annotation.Example;
import io.siddhi.annotation.Extension;
import io.siddhi.annotation.Parameter;
import io.siddhi.annotation.util.DataType;
import io.siddhi.core.config.SiddhiAppContext;
import io.siddhi.core.exception.ConnectionUnavailableException;
import io.siddhi.core.stream.ServiceDeploymentInfo;
import io.siddhi.core.stream.input.source.Source;
import io.siddhi.core.stream.input.source.SourceEventListener;
import io.siddhi.core.util.config.ConfigReader;
import io.siddhi.core.util.snapshot.state.State;
import io.siddhi.core.util.snapshot.state.StateFactory;
import io.siddhi.core.util.transport.OptionHolder;
import io.siddhi.extension.io.sqs.api.SQSBuilder;
import io.siddhi.extension.io.sqs.util.SQSConstants;
import io.siddhi.query.api.exception.SiddhiAppValidationException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * SQS_ar (Assume Role) Source extension.
 */

@Extension(
        name = "sqs_ar",
        namespace = "source",
        description = "SQS source allows users to connect and consume messages from a AWS SQS Queue. It has the" +
                " ability to receive Text messages",
        parameters = {
                @Parameter(
                        name = SQSConstants.QUEUE_URL_NAME,
                        description = "Queue name which SQS Source should subscribe to",
                        type = DataType.STRING
                ),
                @Parameter(
                        name = SQSConstants.ACCESS_KEY_NAME,
                        description = "Access Key for the Amazon Web Services. (This is a mandatory field and should " +
                                "be provided either in the deployment.yml or in the source definition itself)",
                        type = DataType.STRING,
                        optional = true,
                        defaultValue = "null"
                ),
                @Parameter(
                        name = SQSConstants.SECRET_KEY_NAME,
                        description = "Secret Key of the Amazon User. (This is a mandatory field and should " +
                                "be provided either in the deployment.yml or in the source definition itself)",
                        type = DataType.STRING,
                        optional = true,
                        defaultValue = "null"
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
                        name = SQSConstants.POLLING_INTERVAL_NAME,
                        description = "Interval (in milliseconds) between two message retrieval operations",
                        type = DataType.INT
                ),
                @Parameter(
                        name = SQSConstants.WAIT_TIME_NAME,
                        description = "Maximum amount (in seconds) that a polling call will wait for a message to " +
                                "become available in the queue",
                        type = DataType.INT,
                        optional = true,
                        defaultValue = "" + SQSConstants.DEFAULT_WAITING_TIME
                ),
                @Parameter(
                        name = SQSConstants.MAX_NUMBER_OF_MESSAGES_NAME,
                        description = "Maximum number of messages retrieved from the queue per polling call " +
                                "(Actual maybe smaller than this even if there's more messages in the queue)",
                        type = DataType.INT,
                        defaultValue = "" + SQSConstants.DEFAULT_MAX_NUMBER_OF_MESSAGES
                ),
                @Parameter(
                        name = SQSConstants.VISIBILITY_TIMEOUT_NAME,
                        description = "The length of time (in seconds) for which a message received from a queue" +
                                " will be invisible to other consumers(only applicable if consumer doesn't purge the" +
                                " received messages from the queue).",
                        type = DataType.INT,
                        optional = true,
                        defaultValue = "" + SQSConstants.DEFAULT_VISIBILITY_TIMEOUT
                ),
                @Parameter(
                        name = SQSConstants.DELETE_MESSAGES_NAME,
                        description = "Should the message be deleted from the queue after consuming it.",
                        type = DataType.BOOL,
                        optional = true,
                        defaultValue = "" + SQSConstants.DEFAULT_DELETE_AFTER_CONSUME
                ),
                @Parameter(
                        name = SQSConstants.DELETE_RETRY_INTERVAL_NAME,
                        description = "Time interval (in milliseconds) consumer should retry to delete a message in" +
                                " the case of failure during a message delete operation.",
                        type = DataType.INT,
                        optional = true,
                        defaultValue = "" + SQSConstants.DEFAULT_RETRY_INTERVAL
                ),
                @Parameter(
                        name = SQSConstants.MAX_NUMBER_OF_DELETE_RETRY_ATTEMPTS_NAME,
                        description = "Maximum number retry attempts to be performed in case of a failure.",
                        type = DataType.INT,
                        optional = true,
                        defaultValue = "" + SQSConstants.DEFAULT_RETRY_COUNT_LIMIT
                ),
                @Parameter(
                        name = SQSConstants.PARALLEL_CONSUMERS_NAME,
                        description = "Size of the thread pool that should be used for polling.",
                        type = DataType.INT,
                        defaultValue = "" + SQSConstants.DEFAULT_PARALLEL_CONSUMERS
                )
        },
        examples = {
                @Example(
                        syntax = "@source(type='sqs_ar'," +
                                "queue='http://aws.sqs.queue.url'," +
                                "access.key='aws.access.key'," +
                                "secret.key='aws.secret.key'," +
                                "use.delegation='true'," +
                                "role.arn='arn:aws:iam::123456789012:role/some-role-name'," +
                                "role.session.name='some-session-name'," +
                                "region='us-east-2'," +
                                "polling.interval='5000'," +
                                "max.number.of.messages='10'," +
                                "number.of.parallel.consumers='1'," +
                                "purge.messages='true'," +
                                "wait.time='2'," +
                                "visibility.timeout='30'," +
                                "delete.retry.interval='1000'," +
                                "max.number.of.delete.retry.attempts='10'," +
                                "@map(type='xml',enclosing.element=\"//events\"," +
                                "@attributes(symbol='symbol', message_id='trp:MESSAGE_ID') ))" +

                                "define stream inStream (symbol string, message_id string);",
                        description = "" +
                                "Above example demonstrate how an SQS source "
                                + "is getting configured in order to " +
                                "consume messages from an SQS queue.\n" +
                                "SQS source will establish the connection to a queue "
                                + "using given the provided long term credentials and " +
                                "use them to get temporary, hosrt-term credentials to "
                                + "start consuming xml messages from the queue.\n" +
                                "Once a message is received by the source from the given queue, 'xml' mapper will " +
                                "generate a siddhi event from that message and pass it to the inStream."
                )
        }
)
// for more information refer https://wso2.github.io/siddhi/documentation/siddhi-4.0/#sources
public class SQSSource extends Source {
    private static final Logger logger = Logger.getLogger(SQSSource.class);
    private ScheduledExecutorService scheduledExecutorService;
    private SQSSourceConfig sourceConfig;
    private SourceEventListener sourceEventListener;
    private List<ScheduledFuture<?>> futures = new ArrayList<>();

    /**
     * The initialization method for {@link Source}, will be called before other methods. It used to validate
     * all configurations and to get initial values.
     *
     * @param sourceEventListener After receiving events, the source should trigger onEvent() of this listener.
     *                            Listener will then pass on the events to the appropriate mappers for processing .
     * @param optionHolder        Option holder containing static configuration related to the {@link Source}
     * @param configReader        ConfigReader is used to read the {@link Source} related system configuration.
     * @param siddhiAppContext    the context of the {@link io.siddhi.query.api.SiddhiApp} used to get Siddhi
     *                            related utility functions.
     */
    @Override
    public StateFactory init(SourceEventListener sourceEventListener, OptionHolder optionHolder,
                             String[] requestedTransportPropertyNames, ConfigReader configReader,
                             SiddhiAppContext siddhiAppContext) {
        this.sourceConfig = new SQSSourceConfig(optionHolder, requestedTransportPropertyNames);

        if (this.sourceConfig.getAccessKey() == null  || sourceConfig.getAccessKey().isEmpty()) {
            this.sourceConfig.setAccessKey(configReader.readConfig(SQSConstants.ACCESS_KEY_NAME, null));
        }

        if (this.sourceConfig.getSecretKey() == null || sourceConfig.getAccessKey().isEmpty()) {
            this.sourceConfig.setSecretKey(configReader.readConfig(SQSConstants.SECRET_KEY_NAME, null));
        }

        if (sourceConfig.getAccessKey() == null || sourceConfig.getSecretKey() == null ||
                sourceConfig.getAccessKey().isEmpty() || sourceConfig.getSecretKey().isEmpty()) {
            throw new SiddhiAppValidationException("Access key and Secret key are mandatory parameters" +
                    " for the SQS client");
        }
        
        // START Customisation to support delegation (a.k.a. "Assume Role")
        if (this.sourceConfig.getRoleArn() == null || sourceConfig.getRoleArn().isEmpty()) {
            this.sourceConfig.setRoleArn(configReader.readConfig(SQSConstants.ROLE_ARN_NAME, null));
        }
        
        if (this.sourceConfig.getRoleSessionName() == null || sourceConfig.getRoleSessionName().isEmpty()) {
            this.sourceConfig.setRoleSessionName(configReader.readConfig(SQSConstants.ROLE_SESSION_NAME, null));
        }
        // END Customisation to support delegation (a.k.a. "Assume Role")

        scheduledExecutorService = siddhiAppContext.getScheduledExecutorService();
        this.sourceEventListener = sourceEventListener;
        return null;
    }

    @Override
    protected ServiceDeploymentInfo exposeServiceDeploymentInfo() {
        return null;
    }

    /**
     * Returns the list of classes which this source can output.
     *
     * @return Array of classes that will be output by the source.
     * Null or empty array if it can produce any type of class.
     */
    @Override
    public Class[] getOutputEventClasses() {
        return new Class[] {String.class}; // SQS message body supports only text.
    }

    /**
     * Initially Called to connect to the end point for start retrieving the messages asynchronously .
     *
     * @param connectionCallback Callback to pass the ConnectionUnavailableException in case of connection failure after
     *                           initial successful connection. (can be used when events are receiving asynchronously)
     * @throws ConnectionUnavailableException if it cannot connect to the source backend immediately.
     */
    @Override
    public void connect(ConnectionCallback connectionCallback, State state) throws ConnectionUnavailableException {
        this.startPolling();
    }

    /**
     * This method can be called when it is needed to disconnect from the end point.
     */
    @Override
    public void disconnect() {
        stopAndRemoveFutures();
    }

    /**
     * Called at the end to clean all the resources consumed by the {@link Source}
     */
    @Override
    public void destroy() {
        stopAndRemoveFutures();
    }

    /**
     * Called to pause event consumption
     */
    @Override
    public void pause() {
        stopAndRemoveFutures();
    }

    /**
     * Called to resume event consumption
     */
    @Override
    public void resume() {
        this.startPolling();
    }

    private void startPolling() {
        for (int i = 0; i < sourceConfig.getThreadPoolSize(); i++) {
            ScheduledFuture<?> future = scheduledExecutorService
                    .scheduleAtFixedRate(new SQSBuilder(sourceConfig).buildSourceTask(sourceEventListener),
                            0, sourceConfig.getPollingInterval(), TimeUnit.MILLISECONDS);
            futures.add(future);
        }
        logger.info("SQS Provider connected and started polling.");
    }

    private void stopAndRemoveFutures() {
        for (int i = 0; i < futures.size(); i++) {
            futures.get(i).cancel(true);
            futures.remove(i);
        }
    }
}

