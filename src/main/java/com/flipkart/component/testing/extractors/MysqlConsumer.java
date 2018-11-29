package com.flipkart.component.testing.extractors;

import com.flipkart.component.testing.model.MysqlObservation;
import com.flipkart.component.testing.shared.ObjectFactory;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Holds the responsibility of reading from mysql
 */
class MysqlConsumer implements ObservationCollector<MysqlObservation> {


    /**
     * @return
     */
    private ITable getTable(String tableName, MysqlObservation mysqlObservation) {
        try {
            Connection connection = ObjectFactory.getMysqlConnection(mysqlObservation).getConnectionForDatabase();
            IDataSet databaseDataSet = new DatabaseConnection(connection).createDataSet();
            return databaseDataSet.getTable(tableName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * extracts the actual Observation using attributes of expected observation
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public MysqlObservation actualObservations(MysqlObservation expectedObservation) {
        //figure out the table names expected in observation
        Set<String> tableNames = expectedObservation.getTableNames();

        //fetch all the actual observed table data from via MysqlConsumer

        Map<String, ITable> data = new HashMap<>();
        for (String tableName : tableNames) {
            ITable actualTable = this.getTable(tableName, expectedObservation);
            data.put(tableName, actualTable);
        }

        return MysqlObservation.with(data, expectedObservation.getDatabaseName());
    }
}
