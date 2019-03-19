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

    private final  List<AerospikeData> aerospikeData;
    private final AerospikeConnectionInfo connectionInfo;



    @JsonCreator
    public AerospikeObservation(
            @JsonProperty("aerospikeConnectionInfo") AerospikeConnectionInfo connectionInfo,
            @JsonProperty("aerospikeData") List<AerospikeData> aerospikeData){
        this.aerospikeData = aerospikeData;
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
}
