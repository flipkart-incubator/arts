package com.flipkart.component.testing;

import com.flipkart.component.testing.model.http.HttpIndirectInput;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CompositeTestDataLoaderTest {

    private static boolean valueToWatch = false;
    @Test
    public void testRegister(){
        CompositeTestDataLoader compositeTestDataLoader = CompositeTestDataLoader.getInstance();
        HashMap<String, Object> specMap = new HashMap<>();
        specMap.put("response", new HashMap<>());
        HttpIndirectInput httpIndirectInput = new HttpIndirectInput(specMap,null);
        compositeTestDataLoader.load(httpIndirectInput);
        Assert.assertTrue(valueToWatch);

    }

    static class ClassPathTestDataLoader implements TestDataLoader<HttpIndirectInput>{

        /**
         * loads the indirectinput into relevant components
         *
         * @param indirectInput
         */
        @Override
        public void load(HttpIndirectInput indirectInput) {
            valueToWatch = true;
        }

        /**
         * returns the indirectInput class associated with this loader.
         */
        @Override
        public List<Class<HttpIndirectInput>> getIndirectInputClasses() {
            return Arrays.asList(HttpIndirectInput.class);
        }
    }
}
