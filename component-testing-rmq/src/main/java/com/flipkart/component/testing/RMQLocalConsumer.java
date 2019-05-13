package com.flipkart.component.testing;

import com.flipkart.component.testing.model.rmq.RMQObservation;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * RMQ consumer which connects to local RMQ server
 */
class RMQLocalConsumer implements ObservationCollector<RMQObservation> {

    private static final int RMQ_SERVER_PORT = 5672;



    /**
     * Creates a new Connection to local RMQ server for the specified queue.
     */
    RMQLocalConsumer() throws IOException {
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
        Channel channel = getRMQChannel();
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

    @Override
    public Class<RMQObservation> getObservationClass() {
        return RMQObservation.class;
    }

    static Channel getRMQChannel() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Address[] addresses = new Address[]{new Address("localhost", RMQ_SERVER_PORT)};
        com.rabbitmq.client.Connection connection = factory.newConnection(addresses);
        return connection.createChannel();
    }
}
