package com.flipkart.component.testing;

import com.flipkart.component.testing.model.mysql.JSONDataSet;
import com.flipkart.component.testing.model.mysql.MysqlIndirectInput;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class MysqlDataLoader implements TestDataLoader<MysqlIndirectInput> {

   private MysqlIndirectInput indirectInput;
   private Connection myDbSqlconn;

    /**
     * loads the mysql data into the tables
     *
     * @param indirectInput
     */
    @Override
    public void load(MysqlIndirectInput indirectInput) {
        this.indirectInput = indirectInput;
        MysqlConnection mysqlConnection = MysqlFactory.getMysqlConnection(indirectInput);
        mysqlConnection.createDatabase();
        mysqlConnection.runDDLStatements();
        this.myDbSqlconn = mysqlConnection.getConnectionForDatabase();
        insert();
    }

    /**
     * returns the indirectInput class associated with this loader.
     */
    @Override
    public List<Class<MysqlIndirectInput>> getIndirectInputClasses() {
        return Arrays.asList(MysqlIndirectInput.class);
    }


    /**
     * inserts the data specified by the indirect Input to database
     */
    private void insert() {
        if (this.indirectInput.getTablesData() == null) return;
        try {
            IDatabaseConnection connection = new DatabaseConnection(myDbSqlconn);
            IDataSet dataset = new JSONDataSet(this.indirectInput.getTablesData());
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
