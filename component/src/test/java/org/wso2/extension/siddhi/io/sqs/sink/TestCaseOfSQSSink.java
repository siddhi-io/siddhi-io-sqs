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

package org.wso2.extension.siddhi.io.sqs.sink;

import io.siddhi.core.SiddhiAppRuntime;
import io.siddhi.core.SiddhiManager;
import io.siddhi.core.stream.input.InputHandler;
import org.testng.annotations.Test;

import java.util.Date;

public class TestCaseOfSQSSink {

    // Before running this test provide valid FIFO queue information in the stream definition.
    // Due to not having a service to test against this test is commented out in the testng.xml
    @Test
    public void testSQSMessagePublisherInitialization() throws InterruptedException {
        SiddhiAppRuntime siddhiAppRuntime = null;
        SiddhiManager siddhiManager = new SiddhiManager();

        try {
            String streamDef = "@sink(type='sqs'," +
                    "queue='<queue_url>'," +
                    "access.key='<aws_access_key>'," +
                    "secret.key='<aws_secret_key>'," +
                    "region='<region>'," +
                    "delay.interval='5'," +
                    "deduplication.id='{{deduplicationID}}'," +
                    "message.group.id='charuka',@map(type='xml') )" +
                    "define stream outStream(symbol string, deduplicationID string);";

            siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(streamDef);
            InputHandler inputHandler = siddhiAppRuntime.getInputHandler("outStream");
            siddhiAppRuntime.start();
            inputHandler.send(new Object[]{"message 1", new Date().getTime()});
        } finally {
            if (siddhiAppRuntime != null) {
                siddhiAppRuntime.shutdown();
            }
        }

    }

}

