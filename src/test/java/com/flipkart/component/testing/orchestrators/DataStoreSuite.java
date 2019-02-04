package com.flipkart.component.testing.orchestrators;

import com.flipkart.component.testing.internal.HttpTestRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static org.mockito.Mockito.mock;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        KafkaTest.class,
        RedisTest.class,
        RMQTest.class,
        ElasticSearchTest.class,
        ZookeeperTest.class
})
public class DataStoreSuite {

    static SpecificationRunner specificationRunner;

    @BeforeClass
    public static void setUp(){
        specificationRunner = new SpecificationRunner(() -> "", mock(HttpTestRunner.class));
    }

    @AfterClass
    public static void tearDown() {
        specificationRunner.shutDown();
    }
}
