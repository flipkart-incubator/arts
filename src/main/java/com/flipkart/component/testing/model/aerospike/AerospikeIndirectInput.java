package com.flipkart.component.testing.model.aerospike;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Host;
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.shared.AerospikeOperations;
import com.flipkart.component.testing.shared.AerospikeTestConfig;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;


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

    @Override
    public List<AerospikeData> getInputData() {
        return getAerospikeData();
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
