package com.flipkart.component.testing;

import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.hbase.HbaseTestConfig;

public class HbaseFactory {

    public static HBaseAdminOperations getHBaseOperations(HbaseTestConfig hbaseTestConfig) {
        if (hbaseTestConfig.getConnectionType() == ConnectionType.IN_MEMORY) {
//            return LocalHBaseServerOperations.getInstance();
            throw new IllegalStateException("not yet implemented");
        }

        return new RemoteHBaseOperations();
    }
}
