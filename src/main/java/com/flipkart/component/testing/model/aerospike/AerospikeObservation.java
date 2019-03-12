package com.flipkart.component.testing.model.aerospike;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.shared.AerospikeTestConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.Map;


@JsonTypeName("aerospikeObservation")
@Getter
@EqualsAndHashCode
public class AerospikeObservation implements Observation,AerospikeTestConfig {

    private final  List<AerospikeObservationData> data;
    private final AerospikeConnectionInfo connectionInfo;



    @JsonCreator
    public AerospikeObservation(
            @JsonProperty("aerospikeConnectionInfo") AerospikeConnectionInfo connectionInfo,
            @JsonProperty("aerospikeData") List<AerospikeObservationData> data){
        this.data = data;
        this.connectionInfo= connectionInfo;
    }

    @Override
    public String getHost() {
        return connectionInfo.getHostIP();
    }

    @Override
    public int getPort() {
        return connectionInfo.getPort();
    }

    @Override
    public List<AerospikeData> getInputData() {
        return null;
    }

    @Data
    public static class AerospikeConnectionInfo{

        private String hostIP;
        private int port;
        @JsonCreator
        public AerospikeConnectionInfo (
                @JsonProperty("host") String hostIP,
                @JsonProperty("port") int port){
            this.hostIP= hostIP;
            this.port= port;
        }

    }

    @Data
    public static class AerospikeObservationData {

        private String namespace;
        private String set;
        private List<AerospikeRecords> records;

        @JsonCreator
        public AerospikeObservationData(
                @JsonProperty("namespace") String namespace,
                @JsonProperty("set") String set,
                @JsonProperty("records") List<AerospikeRecords> records)
        {

            this.namespace = namespace;
            this.set = set;
            this.records = records;
        }




        @Data
        public static class AerospikeRecords {
            @Getter
            private String primaryKey;
            private Map<String,Object> binData;

            @JsonCreator
            public AerospikeRecords(@JsonProperty("PK") String primaryKey,
                                    @JsonProperty("binData") Map<String, Object> binData){
                this.primaryKey= primaryKey;
                this.binData= binData;
            }

        }
    }

}
