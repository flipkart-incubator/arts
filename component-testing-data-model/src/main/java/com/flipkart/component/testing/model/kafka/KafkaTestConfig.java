package com.flipkart.component.testing.model.kafka;


import com.flipkart.component.testing.model.TestConfig;

public interface KafkaTestConfig extends TestConfig {

    KafkaAuthentication getAuthentication();
}
