package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.model.*;
import com.flipkart.component.testing.model.aerospike.AerospikeIndirectInput;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchIndirectInput;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchObservation;
import com.flipkart.component.testing.model.hbase.HBaseIndirectInput;
import com.flipkart.component.testing.model.hbase.HBaseObservation;
import com.flipkart.component.testing.model.mysql.MysqlIndirectInput;
import com.flipkart.component.testing.model.mysql.MysqlObservation;
import com.flipkart.component.testing.model.redis.RedisIndirectInput;
import com.flipkart.component.testing.model.redis.RedisObservation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Registry for all services required for testing
 */
class DependencyRegistryImpl implements DependencyRegistry {


    private List<TestSpecification> testSpecifications = new ArrayList<>();
    private Set<DependencyInitializer> dependencyInitializers = new HashSet<>();
    private Set<Class> dependencyClasses = new HashSet<>();


    @Override
    public void initialize() {
        //initialize the required dependencies for all test specifications
        testSpecifications.forEach( ts -> {
            this.initializersForIndirectInputs(ts.getIndirectInputs()).forEach(this::initializeIfNotAlready);
            this.initializersForObservations(ts.getObservations()).forEach(this::initializeIfNotAlready);
        });
    }

    private void initializeIfNotAlready(DependencyInitializer dependencyInitializer) {
        try {
            if (!dependencyClasses.contains(dependencyInitializer.getClass())) {
                dependencyInitializer.initialize();
                dependencyClasses.add(dependencyInitializer.getClass());
                this.dependencyInitializers.add(dependencyInitializer);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutDown() {
        dependencyInitializers.forEach(DependencyInitializer::shutDown);
    }

    @Override
    public void clean() {
        dependencyInitializers.forEach(DependencyInitializer::clean);
    }


    /**
     * determines the dependencies based on observations
     *
     * @param observations
     * @return
     */
    private Set<DependencyInitializer> initializersForObservations(List<Observation> observations) {
        Set<DependencyInitializer> dependencyInitializers = new HashSet<>();
        for (Observation observation : observations) {
            if (observation.isRMQ()) {
                dependencyInitializers.add(new RabbitMqLocalServer());
            } else if (observation.isMysql()) {
                dependencyInitializers.add(new MysqlServer((MysqlObservation) observation));
            } else if (observation.isRedis()) {
                dependencyInitializers.add(new RedisLocalServer((RedisObservation) observation));
            } else if (observation.isHBase()) {
                dependencyInitializers.add(new HbaseServer((HBaseObservation) observation));
            } else if (observation.isElasticSearch()) {
                dependencyInitializers.add(new ElasticSearchLocalServer((ElasticSearchObservation) observation));
            } else if(observation.isKafka()){
                dependencyInitializers.add(new KafkaLocalServer());
            } else if(observation.isZk()){
                dependencyInitializers.add(new ZookeeperLocalServer());
            }

            // TODO: atul aerospike
        }
        return dependencyInitializers;
    }

    /**
     * determines the dependencies based on indirectInputs
     *
     * @param indirectInputs
     * @return
     */
    private Set<DependencyInitializer> initializersForIndirectInputs(List<IndirectInput> indirectInputs) {
        Set<DependencyInitializer> dependencyInitializers = new HashSet<>();

        for (IndirectInput indirectInput : indirectInputs) {
            if (indirectInput.isHttpInput()) {
                dependencyInitializers.add(DependencyFactory.getMockServer());
            } else if (indirectInput.isKafkaInput()) {
                dependencyInitializers.add(new KafkaLocalServer());
            } else if (indirectInput.isMysqlInput()) {
                dependencyInitializers.add(new MysqlServer((MysqlIndirectInput) indirectInput));
            } else if (indirectInput.isElasticSearchInput()) {
                dependencyInitializers.add(new ElasticSearchLocalServer((ElasticSearchIndirectInput) indirectInput));
            } else if (indirectInput.isRedisInput()) {
                dependencyInitializers.add(new RedisLocalServer((RedisIndirectInput) indirectInput));
            } else if (indirectInput.isZkInput()) {
                dependencyInitializers.add(new ZookeeperLocalServer());
            } else if (indirectInput.isHBaseInput()) {
                dependencyInitializers.add(new HbaseServer((HBaseIndirectInput) indirectInput));
            } else if (indirectInput.isAerospikeInput()) {
                dependencyInitializers.add(new AerospikeServer((AerospikeIndirectInput) indirectInput));
            } else if (indirectInput.isRMQInput()) {
                dependencyInitializers.add(new RabbitMqLocalServer());
            }

        }
        return dependencyInitializers;
    }

    /**
     * registers the dependencies for test specification.
     *
     * @param testSpecification
     */
    @Override
    public void registerDependencies(TestSpecification testSpecification) {
        this.testSpecifications.add(testSpecification);
    }
}
