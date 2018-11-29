package com.flipkart.component.testing.loaders;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.flipkart.component.testing.Utils;
import com.flipkart.component.testing.model.RMQIndirectInput;
import com.flipkart.component.testing.shared.ObjectFactory;

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
            this.channel = ObjectFactory.getRMQChannel();
            channel.queueDeclare(this.rmqIndirectInput.getQueue(), true, false, false, null);
            rmqIndirectInput.getMessages().forEach(this::pushMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * push message to queue
     *
     * @param msg
     */
    private void pushMessage(Object msg) {
        try {
            this.channel.basicPublish("", rmqIndirectInput.getRoutingKey(), MessageProperties.PERSISTENT_TEXT_PLAIN, Utils.toByteArray(msg));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
