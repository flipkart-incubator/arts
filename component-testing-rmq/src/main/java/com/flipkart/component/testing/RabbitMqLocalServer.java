package com.flipkart.component.testing;

import com.flipkart.component.testing.model.TestConfig;
import com.flipkart.component.testing.model.rmq.RMQIndirectInput;
import com.flipkart.component.testing.model.rmq.RMQObservation;
import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;

/**
 * Local RMQ sever on PORT 5672
 */
class RabbitMqLocalServer implements DependencyInitializer<RMQIndirectInput, RMQObservation, TestConfig> {

    private static final int RMQ_SERVER_PORT = 5672;
    private final Broker broker = new Broker();

    @Override
    public void initialize(TestConfig testConfig) throws Exception {
        final String configFileName = "qpid.json";
        final String passwordFileName = "passwd.properties";
        // prepare options
        final BrokerOptions brokerOptions = new BrokerOptions();
        brokerOptions.setConfigProperty("qpid.amqp_port", String.valueOf(RMQ_SERVER_PORT));
        brokerOptions.setConfigProperty("qpid.pass_file", findResourcePath(passwordFileName));
        brokerOptions.setConfigProperty("qpid.work_dir", Utils.createTempDir().getAbsolutePath());
        brokerOptions.setInitialConfigurationLocation(findResourcePath(configFileName));
        // start broker
        broker.startup(brokerOptions);
    }

    @Override
    public void shutDown() {
        broker.shutdown();
    }

    @Override
    public void clean() {
        broker.shutdown();
        try {
            this.initialize(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<RMQIndirectInput> getIndirectInputClass() {
        return RMQIndirectInput.class;
    }

    @Override
    public Class<RMQObservation> getObservationClass() {
        return RMQObservation.class;
    }

    private String findResourcePath(String fileName) {
        return Utils.createFile(fileName).getAbsolutePath();
    }


}
