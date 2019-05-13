package com.flipkart.component.testing;

import com.flipkart.component.testing.model.zookeeper.ZookeeperObservation;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class ZookeeperConsumer implements ObservationCollector<ZookeeperObservation>{

    /**
     * extracts the actual Observation using attributes of expected observation
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public ZookeeperObservation actualObservations(ZookeeperObservation expectedObservation) {
        ZookeeperOperations zookeeperOperations = ZookeeperFactory.getZookeeperOperations();
        Set<String> observedPaths = expectedObservation.getPaths().stream().filter(zookeeperOperations::exists).collect(Collectors.toSet());
        Map<String, byte[]> values = expectedObservation.getValuePaths().stream().collect(Collectors.toMap(e -> e, zookeeperOperations::getValue));
        return new ZookeeperObservation(observedPaths, values);
    }

    @Override
    public Class<ZookeeperObservation> getObservationClass() {
        return ZookeeperObservation.class;
    }
}
