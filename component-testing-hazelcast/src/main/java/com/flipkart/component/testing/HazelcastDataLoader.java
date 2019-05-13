package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.hazelcast.EmbeddedHZIndirectInput;
import com.flipkart.component.testing.model.hazelcast.HazelcastIndirectInput;
import com.flipkart.component.testing.model.hazelcast.HazelcastMap;
import com.flipkart.component.testing.model.hazelcast.ServerHZIndirectInput;
import com.hazelcast.core.HazelcastInstance;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
            hazelcastInstance = HazelCastFactory.getHazelcastInstance(indirectInput);
            loadMaps(indirectInput.getMaps(), hazelcastInstance);
        } catch (Exception e) {
            throw new RuntimeException("Error loading data to Hazelcast", e);
        }
    }

    /**
     * returns the indirectInput class associated with this loader.
     */
    @Override
    public List getIndirectInputClasses() {
        return Arrays.asList(EmbeddedHZIndirectInput.class, ServerHZIndirectInput.class);
    }


    private void loadMaps(Map<String, HazelcastMap> map, HazelcastInstance hazelcastClient)
            throws ClassNotFoundException {
        for (Map.Entry<String, HazelcastMap> mapNameToHazelCastMap : map.entrySet()) {

            String mapName = mapNameToHazelCastMap.getKey();
            hazelcastClient.getMap(mapName);
            HazelcastMap hazelcastMapDS = mapNameToHazelCastMap.getValue();

            for (Map.Entry<?, ?> mapEntry : hazelcastMapDS.getMapData().entrySet()) {

                Object key = mapEntry.getKey();
                Object value = mapEntry.getValue();
                Class keyClass = Class.forName(hazelcastMapDS.getKeyClass());
                Class valueClass = Class.forName(hazelcastMapDS.getValueClass());

                key = this.objectMapper.convertValue(key, keyClass);
                value = this.objectMapper.convertValue(value, valueClass);
                hazelcastClient.getMap(mapName).put(key, value);

            }
        }
    }

}
