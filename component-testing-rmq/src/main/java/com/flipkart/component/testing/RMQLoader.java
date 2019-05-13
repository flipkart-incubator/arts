package com.flipkart.component.testing;

import com.flipkart.component.testing.model.rmq.RMQIndirectInput;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Arrays;
import java.util.List;

/**
 * loader which loads the rmqIndirectInput into Local RMQ
 */
public class RMQLoader implements TestDataLoader<RMQIndirectInput> {

    /**
     * the indirect input which holds the information to load into RMQ
     */
    private RMQIndirectInput rmqIndirectInput;
    private Channel channel;

    /**
     * loads the rmqInput into RMQ
     *
     * @param rmqIndirectInput
     */
    @Override
    public void load(RMQIndirectInput rmqIndirectInput) {
        this.rmqIndirectInput = rmqIndirectInput;
        try {
            this.channel = RMQLocalConsumer.getRMQChannel();
            channel.queueDeclare(this.rmqIndirectInput.getQueue(), true, false, false, null);
            rmqIndirectInput.getMessages().forEach(this::pushMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * returns the indirectInput class associated with this loader.
     */
    @Override
    public List<Class<RMQIndirectInput>> getIndirectInputClasses() {
        return Arrays.asList(RMQIndirectInput.class);
    }

    /**
     * push message to queue
     *
     * @param msg
     */
    private void pushMessage(String msg) {
        try {
            this.channel.basicPublish("", rmqIndirectInput.getQueue(), MessageProperties.PERSISTENT_TEXT_PLAIN,  msg.toString().getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
