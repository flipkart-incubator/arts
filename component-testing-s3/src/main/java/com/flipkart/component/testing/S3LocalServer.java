package com.flipkart.component.testing;

import com.flipkart.component.testing.model.TestConfig;
import com.flipkart.component.testing.model.s3.S3IndirectInput;
import com.flipkart.component.testing.model.s3.S3Observation;
import io.findify.s3mock.S3Mock;

public class S3LocalServer implements DependencyInitializer<S3IndirectInput,S3Observation,TestConfig>{

    private S3Mock s3;

    public static final int S3_PORT = 7778;

    @Override
    public void initialize(TestConfig testConfig) throws Exception {
        s3 = new S3Mock.Builder().withPort(S3_PORT).withInMemoryBackend().build();
        s3.start();
    }

    @Override
    public void shutDown() {
        s3.stop();
    }

    @Override
    public void clean() {
        s3.stop();
        s3.start();
    }

    @Override
    public Class<S3IndirectInput> getIndirectInputClass() {
        return S3IndirectInput.class;
    }

    @Override
    public Class<S3Observation> getObservationClass() {
        return S3Observation.class;
    }
}
