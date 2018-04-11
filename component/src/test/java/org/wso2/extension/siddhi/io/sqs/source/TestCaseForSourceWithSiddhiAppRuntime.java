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

package org.wso2.extension.siddhi.io.sqs.source;

import org.testng.annotations.Test;
import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.util.EventPrinter;

public class TestCaseForSourceWithSiddhiAppRuntime {

    // Before running this test provide valid queue information in the stream definition.
    // Due to not having a service to test against this test is commented out in the testng.xml
    @Test
    public void testSQSSourceTask() throws InterruptedException {
        String inStreamDef = "@source(type='sqs'," +
                "queue='<queue url>'," +
                "access.key='<access_key>'," +
                "secret.key='<secret_key>'," +
                "region='us-east-2'," +
                "polling.interval='5000'," +
                "max.number.of.messages='10'," +
                "number.of.parallel.consumers='1'," +
                "purge.messages='true'," +
                "waiting.time='2'," +
                "visibility.timeout='30'," +
                "delete.retry.interval='1000'," +
                "max.number.of.delete.retry.attempts='10'," +
                "@map(type='xml',enclosing.element=\"//events\"," +
                "@attributes(symbol='symbol', message_id='trp:MESSAGE_ID') ))" +
                "define stream inStream (symbol string, message_id string);";

        String query = "@info(name='query1')" +
                "from inStream select * insert into outStream;";

        SiddhiAppRuntime siddhiAppRuntime = new SiddhiManager().createSiddhiAppRuntime(inStreamDef + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timestamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timestamp, inEvents, removeEvents);
            }
        });

        siddhiAppRuntime.start();
    }

}
