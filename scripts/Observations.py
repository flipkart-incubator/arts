import json
from commonFunctions import takeJsonFromFile


def takeHttpObservation():
    observation = {'name': 'httpObservation'}
    return observation


def takeMysqlObservation():
    connectionTypeMap = {
        1: 'LOCALHOST',
        2: 'IN_MEMORY'
    }

    observation = {'name': 'mysqlObservation',
                   'databaseName': raw_input('Enter DB name : '),
                   "connectionType": connectionTypeMap[
                       int(raw_input('Enter connectionType 1.LOCALHOST  2.IN_MEMORY:'))]
                   }
    if 'N' == raw_input('Do you want to observe the data inserted in mysql (Y/N) : '):
        observation['data'] = {}
    else:
        tableNames = raw_input('Enter the table names separated by comma : ').split(',')
        data = {}
        for tableName in tableNames:
            data[tableName] = {}
        observation['data'] = data
    return observation


def takeRedisObservation():
    clusterType = {
        1: 'SENTINEL',
        2: 'SINGLE_HOST'
    }

    observation = {
        'name': 'redisObservation',
        "clusterType": clusterType[int(raw_input('Enter the type of cluster \n1.SENTINEL\n2.SINGLE_HOST\n '))],
        "masterName": "master"
    }
    dbIndices = []
    for i in range(0, int(raw_input('Enter the number of DB indices to be observed : '))):
        dbIndices.append(int(raw_input('Enter index ' + str(i + 1) + ' : ')))
    observation['dbIndices'] = dbIndices
    return observation


def takeSolrObservation():
    connectionTypeMap = {
        1: 'LOCALHOST',
        2: 'IN_MEMORY'
    }

    observation = {
        'name': 'solrObservation',
        'connectionType': connectionTypeMap[
            int(raw_input('Enter connectionType \n 1.LOCALHOST \n 2.IN_MEMORY \n'))],
        "solrExpectedData":
            {
                "coreName": raw_input('Enter core name : '),
                "queryFile": raw_input(' enter the path of file having solr query : ')
            }
    }
    documents = []
    while True:
        choice = raw_input('Do you want to observe a document (Y/N) : ')
        if choice == 'Y':
            document = takeJsonFromFile()
            documents.append(document)
        else:
            break
    solrExpectedData = {'documents': documents}
    observation['solrExpectedData'] = solrExpectedData

    return observation


def takeHbaseObservation():
    connectionTypeMap = {
        1: 'REMOTE',
        2: 'IN_MEMORY'
    }
    observation = {'name': 'hbaseObservation'}

    tableName = raw_input('Enter table name with prefix regression_ :');
    if 'regression_' not in tableName:
        tableName = raw_input('Table name entered did not have prefix regression_ . Try again');
        assert 'regression' in tableName
    observation['tableName'] = tableName

    observation['connectionType'] = connectionTypeMap[
        int(raw_input('Enter connectionType : \n1.REMOTE \n2.IN_MEMORY'))]

    rows = []
    while True:
        choice = raw_input('Do you want to observe  row (Y/N) : ')
        if choice == 'Y':
            row = {'rowKey': raw_input("Enter row key : ")}
            data = {}
            while True:
                choice = raw_input('Do you want to observe column family (Y/N) : ')
                if choice == 'Y':
                    colFamName = raw_input('Enter column family name : ')
                    columns = {}
                    while True:
                        choice = raw_input(
                            'Do you want to observe column under column family ' + colFamName + ' (Y/N) : ')
                        if choice == 'Y':
                            columnName = raw_input("Column name :  ")
                            columnValue = raw_input("Column value :  ")
                            columns[columnName] = columnValue
                        else:
                            break
                    data[colFamName] = columns
                else:
                    break
            row['data'] = data
            rows.append(row)
        else:
            break
    observation['rows'] = rows

    return observation


def takeAerospikeObservation():
    observation = {'name': 'aerospikeObservation'}

    connectionInfo = {'host': raw_input("Enter aerospike host IP : "),
                      'port': int(raw_input('Enter port number : '))}

    observation['connectionInfo'] = connectionInfo

    aerospikeData = []

    for i in range(0, int(raw_input('Enter the number of namespaces you want to observe : '))):
        namespace = {'namespace': raw_input('Enter the namespace name with prefix regression_ : ')}
        if 'regression_' not in namespace['namespace']:
            namespace['namespace'] = raw_input('The name entered did not have prefix regression_ . Try again : ')
            assert 'regression_' in namespace['namespace']

        namespace['set'] = raw_input('Enter set name : ')
        records = []
        while True:
            choice = raw_input('Do you want to observe records (Y/N) : ')
            if choice == 'Y':
                record = {'PK': raw_input('Enter primary key : ')}
                binData = {}
                while True:
                    choice = raw_input('Do you want to observe bin data (Y/N) : ')
                    if choice == 'Y':
                        binKey = raw_input('Enter bin key : ')
                        binValue = raw_input('Enter bin value : ')
                        binData[binKey] = binValue
                    else:
                        break
                record['binData'] = binData
                records.append(record)
            else:
                break
        namespace['records'] = records
        aerospikeData.append(namespace)

    observation['aerospikeData'] = aerospikeData
    return observation


def takeElasticSearchObservation():
    observation = {'name': 'elasticSearchObservation'}
    observation['documentsToFetch'] = documentsToFetch = []
    while True:
        choice = raw_input('Do you want to observe a document (Y/N) : ')
        if choice == 'Y':
            document = {
                'suffixOfindex': raw_input(
                    'Enter the suffix value to be added after \'regression_\' to make index name : '),
                'typeName': raw_input('Enter type name : '),
                'queryFile': raw_input('Enter the path to json file having query : ')
            }
            if 'Y' == raw_input('Do you need to add routing key for the search (Y/N) : '):
                document['routingKey'] = raw_input('Enter routing key : ')
            documentsToFetch.append(document)
        else:
            break
    return observation


def takeHazelCastObservation():
    observation = {'name': 'hazelcastObservation'}
    dsToFetch = {}
    selectDS = {
        1: 'maps',
        2: 'none'
    }
    while True:
        ds = selectDS[int(raw_input('Enter the type of data structure you want to load : \n1.maps\n2.none '))]
        if ds == 'none':
            break
        elif ds == 'maps':
            maps = []
            for i in range(0, int(raw_input('Enter the number of maps you want to observe : '))):
                maps.append(raw_input('Enter the map name : '))
            dsToFetch[ds] = maps
    observation['dsToFetch'] = dsToFetch
    return observation


def takeKafkaObservation():
    observation = {
        'name': 'kafkaObservation',
        'topic': raw_input('Enter topic name : ')
    }
    return observation


def takeRMQObservation():
    observation = {
        'name': 'rmqObservation',
        'queue': raw_input('Enter queue name : ')
    }
    return observation


def takeObservations():
    funcMapper = {
        1: takeHttpObservation,
        2: takeMysqlObservation,
        3: takeRedisObservation,
        4: takeSolrObservation,
        5: takeHbaseObservation,
        6: takeAerospikeObservation,
        7: takeElasticSearchObservation,
        8: takeHazelCastObservation,
        9: takeKafkaObservation,
        10: takeRMQObservation
    }

    observations = []

    message = "Do you want to observe results? (Y/N)"
    while True:
        print message,
        message = "Observe more results? (Y/N)"
        choice = raw_input()
        assert choice == 'Y' or choice == 'N'
        if choice == 'N':
            return observations

        print "Select Observation type 1.Http 2.Mysql 3.Redis 4.Solr 5.Hbase 6.aerospike 7.elasticsearch 8.hazelCast 9:Kafka 10:RMQ:",

        observationType = int(raw_input())
        assert 1 <= observationType <= 10

        prepareFunc = funcMapper[observationType]
        observations.append(prepareFunc())
