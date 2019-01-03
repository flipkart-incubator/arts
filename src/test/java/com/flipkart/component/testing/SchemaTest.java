package com.flipkart.component.testing;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class SchemaTest {

    @Test
    public void testHttpDirectInput() throws FileNotFoundException {
        JSONObject jsonSchema = new JSONObject(
                new JSONTokener(new FileReader(new File("/Users/pavan.dv/Desktop/workspace/component-blackbox-testing/src/main/resources/schema/http_direct_input.json"))));
        JSONObject jsonSubject = new JSONObject(
                new JSONTokener(new FileReader(new File("/Users/pavan.dv/Desktop/workspace/component-blackbox-testing/src/test/resources/http-direct-input.json"))));

        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonSubject);
    }
}
