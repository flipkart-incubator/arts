package com.flipkart.component.testing.shared;

import java.sql.Connection;

public interface MysqlConnection {

    Connection get();

    Connection getConnectionForDatabase();
}
