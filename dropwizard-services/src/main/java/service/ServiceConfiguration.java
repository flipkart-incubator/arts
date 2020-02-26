package service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ServiceConfiguration extends Configuration {


    @Valid
    @NotNull
    @JsonProperty("mySqlConfig")
    private DataSourceFactory mySqlConfig = new DataSourceFactory();

    public DataSourceFactory getDatabaseConfiguration(){
        return mySqlConfig;
    }


}
