package com.flipkart.component.testing.model.mysql;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConnectionInfo {
    private MysqlConnectionType connectionType;
    private String host ;
    private String user;
    private String password;

    @JsonCreator
    public ConnectionInfo(@JsonProperty("connectionType")MysqlConnectionType connectionType,
                          @JsonProperty("host")String host,
                          @JsonProperty("user")String user,
                          @JsonProperty("password")String password){
        this.connectionType = connectionType;
        this.host = host;
        this.user = user;
        this.password = password;
    }

}
