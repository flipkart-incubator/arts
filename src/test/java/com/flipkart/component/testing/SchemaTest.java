package com.flipkart.component.testing;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class SchemaTest {

    @Test
    public void combiningTestSchema() throws FileNotFoundException {
        JSONObject baseSchema= new JSONObject(new JSONTokener(
                new FileInputStream(new File("src/main/resources/jsonSchema/baseTestSpecification.json"))));
        JSONObject testJson = new JSONObject(
                new JSONTokener(new FileReader(new File("src/main/resources/JsonSchema/test.json"))));
        Schema schema = SchemaLoader.load(baseSchema);
        try {
            schema.validate(testJson);
        }
        catch (ValidationException e) {
            System.out.println(e.getCausingExceptions()+ " : " +e.getMessage());
            throw new RuntimeException(e.getCausingExceptions().toString()+ " : " +e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getCause());
        }
    }
}
