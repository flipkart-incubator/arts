package com.flipkart.component.testing.extractors;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.flipkart.component.testing.model.rmq.RMQObservation;
import com.flipkart.component.testing.shared.ObjectFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * RMQ consumer which connects to local RMQ server
 */
class RMQLocalConsumer implements ObservationCollector<RMQObservation> {

    /**
     * rmq specifics : channel used to collect messages
     */
    private final Channel channel;


    /**
     * Creates a new Connection to local RMQ server for the specified queue.
     */
    RMQLocalConsumer() throws IOException {
        this.channel = ObjectFactory.getRMQChannel();
    }

    /**
     * returns the messages from local RMQ server.
     * Returns all the messages available in RMQ and will be removed from queue.
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private List<String> getMessages(String queueName) throws IOException, InterruptedException {
        List<String> messages = new ArrayList<>();
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, false, consumer);
        QueueingConsumer.Delivery delivery = consumer.nextDelivery(100);

        while (delivery != null) {
            long deliveryTag = delivery.getEnvelope().getDeliveryTag();
            messages.add(new String(delivery.getBody()));
            channel.basicAck(deliveryTag, false);
            delivery = consumer.nextDelivery(100);
        }

        return messages;
    }

    /**
     * extracts the actual Observation using attributes of expected observation
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public RMQObservation actualObservations(RMQObservation expectedObservation) {
        try {
            List<String> messages = this.getMessages(expectedObservation.getQueue());
            return new RMQObservation(messages, expectedObservation.getQueue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
