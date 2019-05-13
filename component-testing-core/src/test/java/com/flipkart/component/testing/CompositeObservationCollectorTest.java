package com.flipkart.component.testing;

import com.flipkart.component.testing.model.http.HttpObservation;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CompositeObservationCollectorTest {

    /**
     * test for observartion collector in classpath getting registered correctly
     * @throws IOException
     */
    @Test
    public void testRegister() throws IOException {
        CompositeObservationCollector compositeObservationCollector = CompositeObservationCollector.getInstance();
        HttpObservation observation = (HttpObservation) compositeObservationCollector.actualObservations(new HttpObservation(304, null, null));
        Assert.assertNotNull(observation);
        Assert.assertEquals(503,observation.getStatuscode());
    }

    /**
     * A dummy collector to be available in classpath
     */
    static class ClassPathObservationCollector implements ObservationCollector<HttpObservation>{

        @Override
        public HttpObservation actualObservations(HttpObservation expectedObservation) throws IOException {
            return new HttpObservation(503,null, null);
        }

        @Override
        public Class<HttpObservation> getObservationClass() {
            return HttpObservation.class;
        }
    }
}
