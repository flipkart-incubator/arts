package com.flipkart.component.testing.loaders;

import com.flipkart.component.testing.model.mysql.MysqlIndirectInput;
import com.flipkart.component.testing.shared.ObjectFactory;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

import java.sql.Connection;

import static com.flipkart.component.testing.internal.Utils.getDataSet;

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
        this.myDbSqlconn = ObjectFactory.getMysqlConnection(indirectInput).getConnectionForDatabase();
        insert();
    }


    /**
     * inserts the data specified by the indirect Input to database
     */
    private void insert() {
        if (this.indirectInput.getTablesData() == null) return;
        try {
            IDatabaseConnection connection = new DatabaseConnection(myDbSqlconn);
            IDataSet dataset = getDataSet(this.indirectInput.getTablesData());
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
