package com.flipkart.component.testing;

import com.flipkart.component.testing.model.kafka.KafkaIndirectInput;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * loads the data into kafka local server
 */
class KafkaDataLoader implements TestDataLoader<KafkaIndirectInput> {

    static final int KAFKA_BROKER_PORT = 9092;
    static final String BROKER_HOST = "localhost:" + KAFKA_BROKER_PORT;

    /**
     * loads the Indirect input to Kafka: Uses Kafka Producer to load the messages to a partiular topic
     * @param kafkaIndirectInput input that needs to be loaded
     */
    @Override
    public void load(KafkaIndirectInput kafkaIndirectInput) {
        Producer<String, Object> producer = new Producer<>(buildProducerConfig());
        kafkaIndirectInput.getMessages().forEach(message -> {
            KeyedMessage<String, Object> data = new KeyedMessage<>(kafkaIndirectInput.getTopic(), message);
            producer.send(data);
        });

    }

    /**
     * returns the indirectInput class associated with this loader.
     */
    @Override
    public List<Class<KafkaIndirectInput>> getIndirectInputClasses() {
        return Arrays.asList(KafkaIndirectInput.class);
    }

    /**
     * prepares the default producer config
     * @return
     */
    private ProducerConfig buildProducerConfig() {
        Properties props = new Properties();
        props.put("metadata.broker.list", BROKER_HOST);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        ProducerConfig producerConfig = new ProducerConfig(props);
        return producerConfig;
    }
}
