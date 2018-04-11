package org.wso2.extension.siddhi.io.sqs.sink;

import org.testng.annotations.Test;
import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.stream.input.InputHandler;

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

