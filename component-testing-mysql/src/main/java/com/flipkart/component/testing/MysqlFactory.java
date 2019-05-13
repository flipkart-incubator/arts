package com.flipkart.component.testing;

import com.flipkart.component.testing.model.mysql.MysqlConnectionType;
import com.flipkart.component.testing.model.mysql.MysqlTestConfig;

public class MysqlFactory {

    public static MysqlConnection getMysqlConnection(MysqlTestConfig mysqlConnectionConfig) {

        if (mysqlConnectionConfig.getConnectionType() == MysqlConnectionType.IN_MEMORY) {
            return new InMemoryConnection(mysqlConnectionConfig);
        } else if (mysqlConnectionConfig.getConnectionType() == MysqlConnectionType.LOCALHOST) {
            return new LocalhostConnection(mysqlConnectionConfig);
        }

        throw new IllegalStateException("Connection not registered for" + mysqlConnectionConfig.getConnectionType());
    }
}
