package com.flipkart.component.testing.loaders;


import com.flipkart.component.testing.model.*;

/**
 * An implementation of test data loader which loads the IndirectInputs
 */
class TestDataLoaderImpl implements TestDataLoader {

    @Override
    public void load(IndirectInput indirectInput) {
        if (indirectInput.isHttpInput()) {
            new HttpMockDataLoader().load((HttpIndirectInput) indirectInput);
        } else if (indirectInput.isKafkaInput()) {
            new KafkaDataLoader().load((KafkaIndirectInput) indirectInput);
        } else if (indirectInput.isMysqlInput()) {
            new MysqlDataLoader().load((MysqlIndirectInput) indirectInput);
        } else if (indirectInput.isRMQInput()) {
            new RMQLoader().load((RMQIndirectInput) indirectInput);
        } else if (indirectInput.isHBaseInput()) {
            new HBaseDataLoader().load((HBaseIndirectInput) indirectInput);
        } else if (indirectInput.isRedisInput()) {
            new RedisDataLoader().load((RedisIndirectInput) indirectInput);
        } else if (indirectInput.isElasticSearchInput()) {
            new ElasticSearchDataLoader().load((ElasticSearchIndirectInput) indirectInput);
        } else if(indirectInput.isZkInput()){
            //TODO: //not implemented
        }else if (indirectInput.isAerospikeInput()) {
            new AerospikeDataLoader().load((AerospikeIndirectInput) indirectInput);
        }
        else {
            throw new IllegalStateException("Unable to determine the loader for indirect Input" + indirectInput);
        }
    }
}

