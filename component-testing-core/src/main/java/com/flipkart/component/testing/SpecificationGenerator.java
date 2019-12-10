package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.flipkart.component.testing.model.TestSpecification;
import com.google.common.collect.Sets;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import net.sf.json.util.JSONUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;



public class SpecificationGenerator {
    private List<TestSpecification> testSpecificationList ;
    private String specificationTemplate;
    private ObjectMapper mapper = new ObjectMapper();


    public SpecificationGenerator(String csvFilePath,String specificationTemplate){
        this.specificationTemplate = specificationTemplate;
        this.testSpecificationList = getListOfSpecificationFromCsv(csvFilePath);
    }

    public TestSpecification getTestSpecification(int testDataIndex) {
        try {
            return testSpecificationList.get(testDataIndex);
        }catch (Exception e){
            throw new RuntimeException("Failed to get testSpecification for the testId "+testDataIndex+" : "+e);
        }
    }

    private String generateSpecificationAsJsonString(Map<String, Object> givenTestData) {
        try {
            Set<String> headers = Sets.newHashSet(givenTestData.keySet());
            JsonContext jsonContext = (JsonContext) JsonPath.using(Configuration.builder()
                    .jsonProvider(new JacksonJsonNodeJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .build())
                    .parse(specificationTemplate);
            for (String header : headers) {
                if(!String.valueOf(givenTestData.get(header)).isEmpty())
                    jsonContext.set(header, givenTestData.get(header));
            }
            return jsonContext.jsonString();
        }catch (Exception e){
            throw new RuntimeException("Failed to generate specification as string "+e);
        }

    }

    private List<TestSpecification>  getListOfSpecificationFromCsv(String csvFilePath) {
        try {
            List<TestSpecification> listOfSpecification = new ArrayList<>() ;
            List<Object> csvDataList =  readCsvData(csvFilePath);
            for (Object row : csvDataList) {
                row = checkForJsonObjects((Map<String, Object>) row);
                TestSpecification testSpecification = mapper.readValue(generateSpecificationAsJsonString((HashMap) row), TestSpecification.class);
                listOfSpecification.add(testSpecification);
            }
            return listOfSpecification;
        } catch (IOException e) {
            throw new RuntimeException("Failed to add specification to the list of specification");
        }
    }

    private List<Object> readCsvData(String csvFilePath){
        try {
            return new CsvMapper().reader(HashMap.class)
                    .with(CsvSchema.builder().setUseHeader(true).build())
                    .with(CsvSchema.emptySchema()
                            .withHeader())
                    .readValues(new File(csvFilePath).getAbsoluteFile())
                    .readAll();
        }catch (Exception e){
            throw new RuntimeException("Failed to read data from csv : "+e);
        }
    }

    private  Map<String, Object> checkForJsonObjects( Map<String, Object> row) {
        row.forEach((key,value)->{
            try {
                String csvValue = String.valueOf(value);
                if (!csvValue.isEmpty() && JSONUtils.mayBeJSON(csvValue)){
                    if(new JSONTokener(csvValue).nextValue() instanceof JSONArray)
                        row.put(key, new ObjectMapper().readValue((row.get(key).toString()), ArrayList.class));
                    else if (new JSONTokener(csvValue).nextValue() instanceof JSONObject)
                        row.put(key, new ObjectMapper().readValue((row.get(key).toString()), HashMap.class));
                }
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        });
        return row;
    }

}
