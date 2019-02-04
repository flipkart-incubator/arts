package com.flipkart.component.testing.model.http;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.IndirectInput;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

import static com.flipkart.component.testing.shared.ObjectFactory.OBJECT_MAPPER;

/**
 * Representation for a Http Indirect Input.
 */
@JsonTypeName("httpIndirectInput")
@Getter
@ToString
public class HttpIndirectInput implements IndirectInput {

    private final Map<String, Object> specification;

    @JsonCreator
    public HttpIndirectInput(@JsonProperty("specification") Map<String, Object> specification) {
        if (specification.get("response") == null) {
            throw new IllegalArgumentException("response of specification cannot be null");
        }
        Map<String, Object> map = (Map<String, Object>) specification.get("response");
        try {
            //convert object to map
            String body = OBJECT_MAPPER.writeValueAsString(map.get("body"));
            map.put("body", body);
            this.specification = specification;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("unable to serialize" + specification);
        }
    }

    /**
     * config for indirect input to whether load before or after SUT start
     *
     * @return
     */
    @Override
    public boolean isLoadAfterSUT() {
        return false;
    }
}
