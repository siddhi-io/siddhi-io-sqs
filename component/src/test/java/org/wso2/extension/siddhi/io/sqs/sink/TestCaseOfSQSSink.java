package org.wso2.extension.siddhi.io.sqs.sink;

import org.testng.annotations.Test;
import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.stream.input.InputHandler;

import java.util.Date;

public class TestCaseOfSQSSink {
    // If you will know about this related testcase,
    //refer https://github.com/wso2-extensions/siddhi-io-file/blob/master/component/src/test

    /**
     * To run this test case a FIFO queue should be created in the AWS SQS service.
     * Since a valid sqs queue information are required. for this test case it was commented.
     */
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

