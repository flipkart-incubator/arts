package com.flipkart.component.testing;

import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.mysql.MysqlConnectionType;
import com.flipkart.component.testing.model.mysql.MysqlIndirectInput;
import com.flipkart.component.testing.model.mysql.MysqlObservation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class MysqlTest {

    @Test
    public void test() throws Exception {
        Map<String, List<Map<String, Object>>> map = new HashMap<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();
        row.put("name", "testMYSQL");
        rows.add(row);
        map.put("TestTable", rows);
        List<String> ddlStatements = newArrayList("CREATE TABLE testtable (name text NOT NULL)");
        IndirectInput indirectInput = new MysqlIndirectInput("abc", ddlStatements, map,MysqlConnectionType.LOCALHOST);
        MysqlObservation expectedObservation = new MysqlObservation(map, "abc", MysqlConnectionType.LOCALHOST);
        List<Observation> observations = new SpecificationRunner(()-> null).runLite(new TestSpecification(null, null, newArrayList(indirectInput), newArrayList(expectedObservation), null));
        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue( observations.get(0) instanceof MysqlObservation);
        MysqlObservation mysqlObservation = (MysqlObservation) observations.get(0);
        Assert.assertTrue(mysqlObservation.getTable("TestTAble").getRowCount() == 1);
    }
}
