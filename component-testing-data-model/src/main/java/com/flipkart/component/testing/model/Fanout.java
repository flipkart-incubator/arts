package com.flipkart.component.testing.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.hbase.HBaseIndirectInput;
import com.flipkart.component.testing.model.http.HttpIndirectInput;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fanout {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public List<IndirectInput> fanOutIndirectInputs(List<IndirectInput> indirectInputs) {
        try{
            List<IndirectInput> finalIndirectInputList = indirectInputs;
            List<IndirectInput> nullInputs = new ArrayList<>();
            List<IndirectInput> fanOutInput = new ArrayList<>();
            indirectInputs.forEach(indirectInput->{
                if(indirectInput instanceof HttpIndirectInput){
                    if(((HttpIndirectInput) indirectInput).getSpecification()==null)
                        nullInputs.add(indirectInput);
                    fanOutInput.addAll(fanOutHttpIndirectInputs(((HttpIndirectInput) indirectInput).getInputFile()));
                }else if (indirectInput instanceof HBaseIndirectInput){
                    if (((HBaseIndirectInput)indirectInput).getTableName()==null)
                        nullInputs.add(indirectInput);
                    fanOutInput.addAll(fanOutHbaseIndirectInputs(((HBaseIndirectInput) indirectInput).getInputFile()));

                }
            });
            finalIndirectInputList.addAll(fanOutInput);
            finalIndirectInputList.removeAll(nullInputs);
            return finalIndirectInputList;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private List<HttpIndirectInput> fanOutHttpIndirectInputs(String inputFile){
        List<HttpIndirectInput> httpIndirectInputs = new ArrayList<>();
        if(inputFile!=null) {
            try {
                objectMapper.readValue(new File(inputFile).getAbsoluteFile(), ArrayList.class).forEach(spec-> {
                    if(!HttpIndirectInput.requestList.contains(((Map<String ,Object>)spec).get("request"))){
                        HttpIndirectInput.requestList.add((Map<String, Object>) ((Map<String ,Object>)spec).get("request"));
                        httpIndirectInputs.add(new HttpIndirectInput((HashMap<String, Object>) spec, null));
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        HttpIndirectInput.requestList.clear();
        return httpIndirectInputs;
    }

    private List<HBaseIndirectInput> fanOutHbaseIndirectInputs(String inputFile) {
        try {
            List<HBaseIndirectInput> hbaseIndirectInputs = new ArrayList<>();
            if(inputFile!=null)
                new ObjectMapper().readValue(new File(inputFile).getAbsoluteFile(), ArrayList.class).forEach(input -> {
                    try {
                        HBaseIndirectInput hbaseInputs = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(input),HBaseIndirectInput.class);
                        hbaseIndirectInputs.add(new HBaseIndirectInput(hbaseInputs.getTableName(),
                                hbaseInputs.getRows(),
                                ConnectionType.valueOf(hbaseInputs.getConnectionType().toString()),
                                hbaseInputs.getHbaseSiteConfig(),
                                null));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            return hbaseIndirectInputs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
