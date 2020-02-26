package service;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import mysql.dao.MySqlDao;
import mysql.entity.Employee;
import mysql.resource.MySqlResource;
import mysql.resource.ResourceV2;

public class ApiService extends Application<ServiceConfiguration> {

    public static void main(String[] args) throws Exception{
        new ApiService().run(args);
    }
    private HibernateBundle<ServiceConfiguration> hibernate = new HibernateBundle<ServiceConfiguration>(Employee.class) {
        public DataSourceFactory getDataSourceFactory(ServiceConfiguration configuration) {
            return configuration.getDatabaseConfiguration();
        }
    };

    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(ServiceConfiguration configuration, Environment environment) throws Exception {

        environment.jersey().register(new MySqlResource(new DBIFactory()
                .build(environment,configuration.getDatabaseConfiguration(),"mysql")
                .onDemand(MySqlDao.class)));
        environment.jersey().register(new ResourceV2());
        environment.jersey().register(new JsonProcessingExceptionMapper(true));


    }
}
