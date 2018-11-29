package com.flipkart.component.testing.extractors;

import com.flipkart.component.testing.Constants;
import com.flipkart.component.testing.model.KafkaObservation;
import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

import static com.flipkart.component.testing.Constants.KAFKA_BROKER_PORT;

/**
 * Kafka Consumer which connects to local kafka server on port 5000
 */
class KafkaLocalConsumer implements ObservationCollector<KafkaObservation> {

    /**
     * original kafka Consumer
     */
    private final SimpleConsumer kafkaconsumer;


    /**
     * Creates a local Kafka Consumer to broker on port 5000
     */
    KafkaLocalConsumer() {
        kafkaconsumer = new SimpleConsumer("localhost", KAFKA_BROKER_PORT, 10000, 1024000, "test");
    }

    /**
     * returns the messages from the kafka for the specified topic with partition 0 and a total of
     * Maximum returned messages will be 10000: should be sufficient for test cases.
     * @return returns the list of messages in string format
     */
    private List<String> getMessages(String topic) {
        List<String> messages = new ArrayList<>();
        long offset = 0L;

        FetchRequest request = new FetchRequestBuilder().clientId("clientName").addFetch(topic, 0, offset, Constants.MAX_MESSAGES_TO_FETCH_FROM_KAFKA).build();
        FetchResponse fetchResponse = kafkaconsumer.fetch(request);

        for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(topic, 0)) {
            messageAndOffset.offset();
            String message = byteBufferToString(messageAndOffset.message().payload());
            messages.add(message);
        }

        return messages;
    }

    /**
     * prepares string from byte buffer
     * @param buffer
     * @return
     */
    private String byteBufferToString(ByteBuffer buffer) {
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();

        String data = "";
        try {
            int old_position = buffer.position();
            data = decoder.decode(buffer).toString();
            // reset buffer's position to its original so it is not altered:
            buffer.position(old_position);
        } catch (Exception e) {
            return data;
        }
        return data;
    }

    /**
     * extracts the actual Observation using attributes of expected observation
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public KafkaObservation actualObservations(KafkaObservation expectedObservation) {
        List<String> messages = this.getMessages(expectedObservation.getTopic());
        return new KafkaObservation(messages, expectedObservation.getTopic());
    }
}
