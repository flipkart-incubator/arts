package com.flipkart.component.testing;

import com.flipkart.component.testing.model.s3.S3Observation;

import java.io.IOException;

public class S3FileFetcher implements ObservationCollector<S3Observation>{
    /**
     * extracts the actual Observation using attributes of expected observation
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public S3Observation actualObservations(S3Observation expectedObservation) throws IOException {
        return null;
    }

    @Override
    public Class<S3Observation> getObservationClass() {
        return S3Observation.class;
    }
}
