package com.example;

import avro.shaded.com.google.common.collect.ImmutableMap;
import com.example.model.Request;
import com.example.model.Response;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Fail.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableBinding(Processor.class)
public class SpringCloudStreamKafkaApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringCloudStreamKafkaApplicationTests.class);

    private final LinkedBlockingQueue<Response> linkedBlockingQueue = new LinkedBlockingQueue<>(1);

    @Autowired
    Processor processor;

    @Autowired
    TestListener testListener;

    @Before
    public void setUp() throws Exception {
        testListener.setResponses(linkedBlockingQueue);

    }

    @Test
    public void testRoundTrip() throws IOException, InterruptedException {

        Request request = new Request();
        request.setCorrelationId("correlationId");
        request.setCounterparty("BOC");

        SpecificDatumWriter<Request> specificDatumWriter = new SpecificDatumWriter<>(Request.class);
        specificDatumWriter.setSchema(Request.getClassSchema());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        specificDatumWriter.write(request, EncoderFactory.get().directBinaryEncoder(outputStream, null));

        Map<String, Object> headers = ImmutableMap.of("contentType", "application/*+avro");

        assertTrue(processor.input().send(MessageBuilder.withPayload(outputStream.toByteArray()).copyHeaders(headers).build()));
        Response result = linkedBlockingQueue.poll(10, TimeUnit.SECONDS);
        if (result == null) {
            fail("test timed out");
        }
        assertEquals(request.getCorrelationId(), result.getCorrelationId());
    }
}

@EnableBinding(Processor.class)
class TestListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);

    private LinkedBlockingQueue<Response> responses;

    public void setResponses(LinkedBlockingQueue<Response> responses) {
        this.responses = responses;
    }

    @StreamListener(Processor.OUTPUT)
    public void process(Response response) {
        LOGGER.info("Received response {}", response);
        responses.offer(response);
    }

}




