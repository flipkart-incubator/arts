package com.flipkart.component.testing;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.flipkart.component.testing.model.s3.S3IndirectInput;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class S3FileLoader implements TestDataLoader<S3IndirectInput>{
    /**
     * loads the indirectinput into relevant components
     *
     * @param indirectInput
     */
    @Override
    public void load(S3IndirectInput indirectInput) {
        try {
            AmazonS3 client = client();
            client.createBucket(indirectInput.getBucketName());
            client.putObject(indirectInput.getBucketName(), indirectInput.getFileKey(), new File(indirectInput.getFilePath()));
        } catch (Exception e) {
            throw new RuntimeException("Exception while loading data to s3", e);
        }
    }

    private AmazonS3 client() throws Exception {
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration("http://localhost:" + S3LocalServer.S3_PORT, "us-west-2");
        return AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();
    }

    /**
     * returns the indirectInput class associated with this loader.
     */
    @Override
    public List<Class<S3IndirectInput>> getIndirectInputClasses() {
        return Arrays.asList(S3IndirectInput.class);
    }
}
