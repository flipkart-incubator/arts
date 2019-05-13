package com.flipkart.component.testing.model.s3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.IndirectInput;
import lombok.Getter;

@Getter
@JsonTypeName("s3IndirectInput")
@JsonIgnoreProperties
public class S3IndirectInput implements IndirectInput {

    private String bucketName;

    private String fileKey;

    private String filePath;


    public S3IndirectInput(@JsonProperty("bucketName") String bucketName,
                           @JsonProperty("fileKey") String fileKey,
                           @JsonProperty("filePath") String filePath){
        this.bucketName = bucketName;
        this.fileKey = fileKey;
        this.filePath = filePath;
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
