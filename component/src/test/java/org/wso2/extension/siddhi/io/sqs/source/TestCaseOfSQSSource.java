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

        Assert.assertEquals(new String[] {"message_id_provided"}, response);
    }


}


