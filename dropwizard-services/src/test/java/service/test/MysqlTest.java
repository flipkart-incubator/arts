package service.test;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.http.HttpObservation;
import mysql.entity.Employee;
import mysql.entity.ResponseMessage;
import org.junit.Assert;
import org.junit.Test;
import test.Runner.SuiteSetUp;
import com.flipkart.component.testing.model.Observation;

import java.util.List;



public class MysqlTest extends SuiteSetUp {
List<Observation> observations;
ObjectMapper mapper;

    @Test
    public void addNewEmployee() throws Exception {
        String specFilePath ="src/test/resources/mysql/addNewEmployee.json";
        observations = specificationRunner.runLite(specFilePath);
        mapper = new ObjectMapper();
        Employee employee= mapper.readValue(((HttpObservation)observations.get(0)).getResponse().toString(), Employee.class);

        String id="2";
        String name="two";
        String department="Senior";

        Assert.assertEquals(employee.getId(),id);
        Assert.assertEquals(employee.getName(),name);
        Assert.assertEquals(employee.getDepartment(),department);
        
    }

    @Test
    public void getEmployeeById() throws Exception{

        String specFilePath= "src/test/resources/mysql/getEmployeeById.json";
        observations = specificationRunner.runLite(specFilePath);
        mapper = new ObjectMapper();

        Employee employee = mapper.readValue(((HttpObservation) observations.get(0)).getResponse().toString(),Employee.class);

        String id="1";
        String name="one";
        String department="Junior";

        Assert.assertEquals(employee.getId(),id);
        Assert.assertEquals(employee.getName(),name);
        Assert.assertEquals(employee.getDepartment(),department);

    }


    @Test
    public void editEmployeeDetailsById() throws Exception{

        String specFilePath= "src/test/resources/mysql/editEmployeeDetailsById.json";
        observations = specificationRunner.runLite(specFilePath);
        mapper = new ObjectMapper();

        Employee employee = mapper.readValue(((HttpObservation) observations.get(0)).getResponse().toString(),Employee.class);

        String id="1";
        String name="changed one";
        String department="changed Junior";

        Assert.assertEquals(employee.getId(),id);
        Assert.assertEquals(employee.getName(),name);
        Assert.assertEquals(employee.getDepartment(),department);

    }

    @Test
    public void deleteEmployee() throws Exception {
        String specFilePath= "src/test/resources/mysql/deleteEmployeeById.json";
        observations = specificationRunner.runLite(specFilePath);
        mapper = new ObjectMapper();

        ResponseMessage responseMessage = mapper.readValue(((HttpObservation) observations.get(0)).getResponse().toString(),ResponseMessage.class);
        String expectedResponse = "Deleted";

        Assert.assertEquals(expectedResponse,responseMessage.getMessage());
    }


}
