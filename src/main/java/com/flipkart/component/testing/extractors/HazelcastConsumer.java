package com.flipkart.component.testing.extractors;

import com.flipkart.component.testing.model.hazelcast.HazelcastDataStructures;
import com.flipkart.component.testing.model.hazelcast.HazelcastMap;
import com.flipkart.component.testing.model.hazelcast.HazelcastObservation;
import com.flipkart.component.testing.shared.ObjectFactory;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.flipkart.component.testing.internal.Constants.HZ_MAPS_DS;

/**
 * @author siddharth.t
 */
public class HazelcastConsumer implements ObservationCollector<HazelcastObservation> {
    @Override
    public HazelcastObservation actualObservations(HazelcastObservation expectedObservation) {
        HazelcastDataStructures hazelcastDataStructures = getHazelcastData(expectedObservation);
        HazelcastObservation hazelcastObservation = new HazelcastObservation(
                expectedObservation.getDStoFetch());
        hazelcastObservation.setHazelcastDS(hazelcastDataStructures);
        return hazelcastObservation;
    }

    private HazelcastDataStructures getHazelcastData(HazelcastObservation hazelcastObservation) {
        HazelcastInstance hazelcast = ObjectFactory.getHazelcastInstance(hazelcastObservation);
        Map<String, HazelcastMap> maps = new HashMap<>();
        hazelcastObservation.getDStoFetch().forEach((String dsType, List<String> dsNames) -> {
            if (dsType.equals(HZ_MAPS_DS)) {
                dsNames.forEach((String dsName) -> {
                    Map<Object, Object> map = new HashMap<>(hazelcast.getMap(dsName));
                    HazelcastMap hazelcastMap = new HazelcastMap(map, null, null);
                    maps.put(dsName, hazelcastMap);
                });
            }
        });
        HazelcastDataStructures hazelcastDataStructures = new HazelcastDataStructures(maps);
        return hazelcastDataStructures;
    }
}