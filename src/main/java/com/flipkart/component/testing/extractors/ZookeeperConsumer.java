package com.flipkart.component.testing.extractors;

import com.flipkart.component.testing.model.zookeeper.ZookeeperObservation;
import com.flipkart.component.testing.shared.ObjectFactory;
import com.flipkart.component.testing.shared.ZookeeperOperations;
import org.apache.curator.framework.CuratorFrameworkFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
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
        ZookeeperOperations zookeeperOperations = ObjectFactory.getZookeeperOperations();
        Set<String> observedPaths = expectedObservation.getPaths().stream().filter(zookeeperOperations::exists).collect(Collectors.toSet());
        Map<String, byte[]> values = expectedObservation.getValuePaths().stream().collect(Collectors.toMap(e -> e, zookeeperOperations::getValue));
        return new ZookeeperObservation(observedPaths, values);
    }
}
