package com.flipkart.component.testing;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;

public class SchemaTest {

    @Test
    public void testHttpDirectInput() throws FileNotFoundException {
        JSONObject jsonSchema = new JSONObject(
                new JSONTokener(new FileReader(new File(
                        Objects.requireNonNull(
                                this.getClass().getClassLoader().getResource("schema/http_direct_input.json")).getFile()
                            ))));
        JSONObject jsonSubject = new JSONObject(
                new JSONTokener(new FileReader(new File(
                        Objects.requireNonNull(this.getClass().getClassLoader().getResource("http-direct-input.json")).getFile()
                            ))));

        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonSubject);
    }
}
