package com.flipkart.component.testing.extractors;

import com.flipkart.component.testing.internal.Constants;
import com.flipkart.component.testing.model.hazelcast.HazelcastDataStructures;
import com.flipkart.component.testing.model.hazelcast.HazelcastIndirectInput;
import com.flipkart.component.testing.model.hazelcast.HazelcastMap;
import com.flipkart.component.testing.model.hazelcast.HazelcastObservation;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.discovery.InstanceDiscoveryConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.flipkart.component.testing.internal.Constants.*;

/**
 * @author siddharth.t
 */
public class HazelcastConsumer implements ObservationCollector<HazelcastObservation> {
    @Override
    public HazelcastObservation actualObservations(HazelcastObservation expectedObservation) throws IOException {
        HazelcastDataStructures hazelcastDataStructures = getHazelcastData(expectedObservation);
        return new HazelcastObservation(
                expectedObservation.getDStoFetch(),
                hazelcastDataStructures);
    }

    private HazelcastDataStructures getHazelcastData(HazelcastObservation hazelcastObservation) {
        HazelcastInstance hazelcast = Hazelcast.getAllHazelcastInstances().iterator().next();
        HazelcastDataStructures hazelcastDataStructures = new HazelcastDataStructures();
        Map<String, HazelcastMap> maps = new HashMap<>();
        hazelcastObservation.getDStoFetch().forEach((String dsType, List<String> dsNames) -> {
            if (dsType.equals(HZ_MAPS_DS)) {
                dsNames.forEach((String dsName) -> {
                    HazelcastMap hazelcastMap = new HazelcastMap();
                    Map<Object, Object> map = new HashMap<>(hazelcast.getMap(dsName));
                    hazelcastMap.setMapData(map);
                    maps.put(dsName, hazelcastMap);
                });
            }
        });
        hazelcastDataStructures.setMaps(maps);
        return hazelcastDataStructures;
    }
}