package com.flipkart.component.testing.loaders;

import com.flipkart.component.testing.model.zookeeper.ZookeeperIndirectInput;
import com.flipkart.component.testing.shared.ObjectFactory;
import com.flipkart.component.testing.shared.ZookeeperOperations;

import java.util.Optional;

class ZookeeperDataLoader implements TestDataLoader<ZookeeperIndirectInput>{

    /**
     * loads the indirectinput into relevant components
     *
     * @param indirectInput
     */
    @Override
    public void load(ZookeeperIndirectInput indirectInput) {
        ZookeeperOperations zookeeperOperations = ObjectFactory.getZookeeperOperations();
        Optional.ofNullable(indirectInput.getPathsToCreate()).ifPresent(x -> x.forEach(zookeeperOperations::createPath));
        Optional.ofNullable(indirectInput.getDataToLoad()).ifPresent(x -> x.forEach(zookeeperOperations::putValue));
    }
}
