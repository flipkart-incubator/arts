package com.flipkart.component.testing;

import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.hbase.HbaseTestConfig;

class HbaseFactory {

    static HBaseAdminOperations getHBaseOperations(HbaseTestConfig hbaseTestConfig) {
        if (hbaseTestConfig.getConnectionType() == ConnectionType.IN_MEMORY) {
            throw new IllegalStateException("not yet implemented");
        }
        return new RemoteHBaseOperations(hbaseTestConfig);
    }
}
