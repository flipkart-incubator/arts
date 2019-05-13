package com.flipkart.component.testing;

import java.sql.Connection;

public interface MysqlConnection {

    Connection get();

    Connection getConnectionForDatabase();
}
