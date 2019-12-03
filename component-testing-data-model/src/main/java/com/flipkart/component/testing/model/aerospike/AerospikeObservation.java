package com.flipkart.component.testing.model.aerospike;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.Observation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;


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

    @Override
    public String getUser() {return connectionInfo.getUser(); }

    @Override
    public String getPassword() {return connectionInfo.getPassword(); }

    @Data
    public static class AerospikeConnectionInfo{

        private String hostIP;
        private int port;
        private String user;
        private String password;
        @JsonCreator
        public AerospikeConnectionInfo (
                @JsonProperty("host") String hostIP,
                @JsonProperty("port") int port,
                @JsonProperty("user") String user,
                @JsonProperty("password") String password){
            this.hostIP= hostIP;
            this.port= port;
            this.user= user;
            this.password= password;
        }

    }
}
