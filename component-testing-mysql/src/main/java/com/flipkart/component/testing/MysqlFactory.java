package com.flipkart.component.testing;

import com.flipkart.component.testing.model.mysql.MysqlConnectionType;
import com.flipkart.component.testing.model.mysql.MysqlTestConfig;

public class MysqlFactory {

    public static MysqlConnection getMysqlConnection(MysqlTestConfig mysqlConnectionConfig) {

        if (mysqlConnectionConfig.getConnectionInfo().getConnectionType() == MysqlConnectionType.IN_MEMORY) {
            return new InMemoryConnection(mysqlConnectionConfig);
        } else if (mysqlConnectionConfig.getConnectionInfo().getConnectionType() == MysqlConnectionType.REMOTE) {
            return new RemoteHostConnection(mysqlConnectionConfig);
        }

        throw new IllegalStateException("Connection not registered for" + mysqlConnectionConfig.getConnectionInfo().getConnectionType());
    }
}
