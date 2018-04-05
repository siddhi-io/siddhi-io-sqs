package org.wso2.extension.siddhi.io.sqs.source;

import com.amazonaws.http.SdkHttpMetadata;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteMessageResult;
import com.amazonaws.services.sqs.model.InvalidIdFormatException;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiptHandleIsInvalidException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.legacy.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.wso2.extension.siddhi.io.sqs.util.SQSConstants;
import org.wso2.siddhi.core.stream.input.source.SourceEventListener;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest(SQSSourceTask.class)
public class TestCaseOfSQSSource {

    /**
     * For this Test case Follow the following instructions.
     * 1) Create any type of queue in a Service
     * 2) Replace the queue url, access key, secret key in proper places with the ones relevant to the queue.
     * 3) Uncomment and run the Test.
     */
//    @Test
//    public void testSQSSourceTask() throws InterruptedException {
//        String inStreamDef = "@source(type='sqs'," +
//                "queue='<queue url>'," +
//                "access.key='<access_key>'," +
//                "secret.key='<secret_key>'," +
//                "region='us-east-2'," +
//                "polling.interval='5000'," +
//                "max.number.of.messages='10'," +
//                "number.of.parallel.consumers='1'," +
//                "purge.messages='true'," +
//                "waiting.time='2'," +
//                "visibility.timeout='30'," +
//                "delete.retry.interval='1000'," +
//                "max.number.of.delete.retry.attempts='10'," +
//                "@map(type='xml',enclosing.element=\"//events\"," +
//                "@attributes(symbol='symbol', message_id='trp:MESSAGE_ID') ))" +
//                "define stream inStream (symbol string, message_id string);";
//
//        String query = "@info(name='query1')" +
//                "from inStream select * insert into outStream;";
//
//        SiddhiAppRuntime siddhiAppRuntime = new SiddhiManager().createSiddhiAppRuntime(inStreamDef + query);
//
//        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
//            @Override
//            public void receive(long timestamp, Event[] inEvents, Event[] removeEvents) {
//                EventPrinter.print(timestamp, inEvents, removeEvents);
//            }
//        });
//
//        siddhiAppRuntime.start();
//    }


    @Test
    public void testDeleteMessageFromQueueMethod() throws Exception {
        AmazonSQS amazonSQS = mock(AmazonSQS.class);
        DeleteMessageResult deleteMessageResult = mock(DeleteMessageResult.class);
        SdkHttpMetadata sdkHttpMetadata = mock(SdkHttpMetadata.class);

        when(sdkHttpMetadata.getHttpStatusCode()).thenReturn(200);
        when(deleteMessageResult.getSdkHttpMetadata()).thenReturn(sdkHttpMetadata);
        when(amazonSQS.deleteMessage(any(DeleteMessageRequest.class))).thenReturn(deleteMessageResult);

        Message message = mock(Message.class);

        when(message.getMessageId()).thenReturn("message_id");
        when(message.getReceiptHandle()).thenReturn("receipt_handle");

        SQSSourceConfig config = mock(SQSSourceConfig.class);
        SourceEventListener sourceEventListener = mock(SourceEventListener.class);

        SQSSourceTask sqsSourceTask = new SQSSourceTask(config, amazonSQS, sourceEventListener);

        boolean response = Whitebox.
                <Boolean>invokeMethod(sqsSourceTask, "deleteMessageFromQueue", message);

        Assert.assertEquals(true, response);

        when(amazonSQS.deleteMessage(any(DeleteMessageRequest.class)))
                .thenThrow(new InvalidIdFormatException("wrong id format"));

        response = Whitebox.<Boolean>invokeMethod(sqsSourceTask, "deleteMessageFromQueue", message);

        Assert.assertEquals(false, response);

        when(amazonSQS.deleteMessage(any(DeleteMessageRequest.class)))
                .thenThrow(new ReceiptHandleIsInvalidException("wrong id format"));

        response = Whitebox.<Boolean>invokeMethod(sqsSourceTask, "deleteMessageFromQueue", message);

        Assert.assertEquals(false, response);

    }

    @Test
    public void testGetTransportProperties() throws Exception {
        Message message = mock(Message.class);

        when(message.getMessageId()).thenReturn("message_id_provided");

        String[] transportProperties = {SQSConstants.MESSAGE_ID_PROPERTY_NAME};

        AmazonSQS amazonSQS = mock(AmazonSQS.class);
        SQSSourceConfig sourceConfig = mock(SQSSourceConfig.class);
        SourceEventListener sourceEventListener = mock(SourceEventListener.class);

        SQSSourceTask sqsSourceTask = new SQSSourceTask(sourceConfig, amazonSQS, sourceEventListener);

        String[] response = Whitebox
                .invokeMethod(sqsSourceTask, "getTransportProperties", message, transportProperties);

        Assert.assertEquals(new String[]{"message_id_provided"}, response);
    }


}


