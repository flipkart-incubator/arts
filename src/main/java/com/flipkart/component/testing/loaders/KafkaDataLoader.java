package com.flipkart.component.testing.loaders;

import com.flipkart.component.testing.internal.Constants;
import com.flipkart.component.testing.model.kafka.KafkaIndirectInput;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * loads the data into kafka local server
 */
class KafkaDataLoader implements TestDataLoader<KafkaIndirectInput> {

    /**
     * loads the Indirect input to Kafka: Uses Kafka Producer to load the messages to a partiular topic
     * @param kafkaIndirectInput input that needs to be loaded
     */
    @Override
    public void load(KafkaIndirectInput kafkaIndirectInput) {
        Producer<String, Object> producer = new Producer<>(buildProducerConfig(kafkaIndirectInput.getSerializerClass()));
        kafkaIndirectInput.getMessages().forEach(message -> {
            KeyedMessage<String, Object> data = new KeyedMessage<>(kafkaIndirectInput.getTopic(), message);
            producer.send(data);
        });

    }

    /**
     * prepares the default producer config
     * @param serializerClass
     * @return
     */
    private ProducerConfig buildProducerConfig(String serializerClass) {
        Properties props = new Properties();
        props.put("metadata.broker.list", Constants.BROKER_HOST);
        props.put("serializer.class", serializerClass);
        ProducerConfig producerConfig = new ProducerConfig(props);
        return producerConfig;
    }
}
