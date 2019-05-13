package com.flipkart.component.testing;


import com.flipkart.component.testing.model.http.HttpIndirectInput;

import java.util.Arrays;
import java.util.List;

/**
 * Holds the responsibility of loading the HttpIndirectInput to the mock server
 */
class HttpMockDataLoader implements TestDataLoader<HttpIndirectInput> {

    HttpMockDataLoader() {

    }

    /**
     * loads the http indirect inputs to the mock server based on the path
     * this may evolve based on different needs of mocking: Evaluate wiremock on the complexity of mocking later.
     * not doing now to keep it simple and flexibility.
     * registers the response based on the api path
     * @param indirectInput
     */
    @Override
    public void load(HttpIndirectInput indirectInput) {
        MockServerOperations.INSTANCE.load(indirectInput);
    }

    /**
     * returns the indirectInput class associated with this loader.
     */
    @Override
    public List<Class<HttpIndirectInput>> getIndirectInputClasses() {
        return Arrays.asList(HttpIndirectInput.class);
    }

}
