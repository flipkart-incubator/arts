//package flipkart.pnp.testing.shared;
//
//import java.io.IOException;
//
//class LocalHBaseServerOperations implements HBaseAdminOperations {
//
//
//    private static LocalHBaseServerOperations localHbaseServerOperations;
//    private HBaseTestingUtility hbaseUtility;
//
//    private LocalHBaseServerOperations(HBaseTestingUtility hBaseTestingUtility) {
//        this.hbaseUtility = hBaseTestingUtility;
//    }
//
//    public static LocalHBaseServerOperations getInstance() {
//        if (localHbaseServerOperations == null) {
//            localHbaseServerOperations = new LocalHBaseServerOperations(new HBaseTestingUtility());
//        }
//        return localHbaseServerOperations;
//    }
//
//    @Override
//    public void startCluster() {
//        try {
//            this.hbaseUtility.startMiniCluster();
//        } catch (Exception e) {
//            throw new RuntimeException("Error starting Mini Cluster \n" + e.getMessage());
//        }
//    }
//
//    @Override
//    public void stopCluster() {
//        if (this.hbaseUtility != null) {
//            try {
//                this.hbaseUtility.shutdownMiniCluster();
//            } catch (Exception e) {
//                throw new RuntimeException("Unable to close HBase connection", e);
//            }
//        }
//    }
//
//    @Override
//    public Table createTable(HbaseTestConfig hbaseTestConfig) {
//        try {
//            return this.hbaseUtility.createTable(TableName.valueOf(hbaseTestConfig.getTableName()), hbaseTestConfig.columnFamilies());
//        } catch (IOException e) {
//            throw new RuntimeException("Excpetion while creating table" + hbaseTestConfig.getTableName(), e);
//        }
//    }
//
//    @Override
//    public Table getTable(HbaseTestConfig hbaseTestConfig) {
//        try {
//            return this.hbaseUtility.getConnection().getTable(TableName.valueOf(hbaseTestConfig.getTableName()));
//        } catch (IOException e) {
//            throw new RuntimeException("Excpetion while fetching table" + hbaseTestConfig.getTableName(), e);
//        }
//    }
//}
