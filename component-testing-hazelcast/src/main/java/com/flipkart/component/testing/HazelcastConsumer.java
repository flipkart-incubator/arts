package com.flipkart.component.testing;

import com.flipkart.component.testing.model.hazelcast.HazelcastDataStructures;
import com.flipkart.component.testing.model.hazelcast.HazelcastMap;
import com.flipkart.component.testing.model.hazelcast.HazelcastObservation;
import com.hazelcast.core.HazelcastInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Class<HazelcastObservation> getObservationClass() {
        return HazelcastObservation.class;
    }

    private HazelcastDataStructures getHazelcastData(HazelcastObservation hazelcastObservation) {
        HazelcastInstance hazelcast = HazelCastFactory.getHazelcastInstance(hazelcastObservation);
        Map<String, HazelcastMap> maps = new HashMap<>();
        hazelcastObservation.getDStoFetch().forEach((String dsType, List<String> dsNames) -> {
            if (dsType.equals(HazelCastFactory.HZ_MAPS_DS)) {
                dsNames.forEach((String dsName) -> {
                    Map<Object, Object> map = new HashMap<>(hazelcast.getMap(dsName));
                    HazelcastMap hazelcastMap = new HazelcastMap(map, null, null);
                    maps.put(dsName, hazelcastMap);
                });
            }
        });
        return new HazelcastDataStructures(maps);
    }
}