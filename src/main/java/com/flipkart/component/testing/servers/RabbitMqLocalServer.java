package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.Utils;
import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;

import static com.flipkart.component.testing.Constants.RMQ_SERVER_PORT;

/**
 * Local RMQ sever on PORT 5672
 */
class RabbitMqLocalServer extends DependencyInitializer {
    private final Broker broker = new Broker();

    @Override
    public void initialize() throws Exception {
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

    private String findResourcePath(String fileName) {
        return Utils.createFile(fileName).getAbsolutePath();
    }


}
