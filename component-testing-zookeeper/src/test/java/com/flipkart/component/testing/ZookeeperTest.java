package com.flipkart.component.testing;

import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.zookeeper.ZookeeperIndirectInput;
import com.flipkart.component.testing.model.zookeeper.ZookeeperObservation;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

public class ZookeeperTest {

    @Test
    public void testZookeeper() throws Exception {

        Set<String> paths = Sets.newHashSet("/abc", "/abc/def", "/abc/ghi");
        ImmutableMap<String, String> data = ImmutableMap.of("/abc/def", "val1");
        IndirectInput indirectInput = new ZookeeperIndirectInput(paths, data);

        TestSpecification testSpecification = new TestSpecification(null, null, newArrayList(indirectInput), newArrayList(new ZookeeperObservation(paths,data.keySet())), null);

        List<Observation> observations = new SpecificationRunner(() -> null).runLite(testSpecification);

        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof ZookeeperObservation);
        ZookeeperObservation zkObservation = (ZookeeperObservation) observations.get(0);
        Assert.assertTrue(zkObservation.exists("/abc"));

    }
}
