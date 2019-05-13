package com.flipkart.component.testing.model.aerospike;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.IndirectInput;
import lombok.Data;
import lombok.Getter;

import java.util.List;


@JsonTypeName("aerospikeIndirectInput")
@Getter
public class AerospikeIndirectInput implements IndirectInput,AerospikeTestConfig {


    private  List<AerospikeData> aerospikeData;
    private  AerospikeConnectionInfo connectionInfo;

    @JsonCreator
    public AerospikeIndirectInput(
            @JsonProperty("aerospikeConnectionInfo") AerospikeConnectionInfo connectionInfo,
            @JsonProperty("aerospikeData") List<AerospikeData> aerospikeData){
        this.aerospikeData = aerospikeData;
        this.connectionInfo= connectionInfo;
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

    @Override
    public String getHost() {
        return getConnectionInfo().getHostIP();
    }

    @Override
    public int getPort() {
        return getConnectionInfo().getPort();
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
