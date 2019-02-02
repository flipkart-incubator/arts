package com.flipkart.component.testing.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.internal.Constants;
import com.flipkart.component.testing.model.hazelcast.HazelcastIndirectInput;
import com.flipkart.component.testing.model.hazelcast.HazelcastMap;
import com.flipkart.component.testing.shared.ObjectFactory;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;

import static com.flipkart.component.testing.internal.Constants.HZ_INSTANCE_NAME;

/**
 * @author siddharth.t
 */
public class HazelcastDataLoader implements TestDataLoader<HazelcastIndirectInput> {

    private ObjectMapper objectMapper;

    @Override
    public void load(HazelcastIndirectInput indirectInput) {
        try {

            this.objectMapper = new ObjectMapper();
            HazelcastInstance hazelcastInstance = null;
            hazelcastInstance = ObjectFactory.getHazelcastInstance(indirectInput);
            loadMaps(indirectInput.getMaps(), hazelcastInstance);
        } catch (Exception e) {
            throw new RuntimeException("Error loading data to Hazelcast", e);
        }
    }


    private void loadMaps(Map<String, HazelcastMap> map, HazelcastInstance hazelcastClient)
            throws ClassNotFoundException {
        for (Map.Entry<String, HazelcastMap> mapNameToHazelCastMap : map.entrySet()) {

            String mapName = mapNameToHazelCastMap.getKey();
            hazelcastClient.getMap(mapName);
            HazelcastMap hazelcastMapDS = mapNameToHazelCastMap.getValue();

            for (Map.Entry<Object, Object> mapEntry : hazelcastMapDS.getMapData().entrySet()) {

                Object key = mapEntry.getKey();
                Object value = mapEntry.getValue();
                Class keyClass = Class.forName(hazelcastMapDS.getKeyClass());
                Class valueClass = Class.forName(hazelcastMapDS.getValueClass());

                key = this.objectMapper.convertValue(key, keyClass);
                value = this.objectMapper.convertValue(value, valueClass);
                hazelcastClient.getMap(mapName).putIfAbsent(key, value);

            }
        }
    }

}
