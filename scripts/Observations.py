from commonFunctions import takeJsonFromFile


def takeHttpObservation():
    observation = {'name': 'httpObservation'}
    return observation


def takeMysqlObservation():
    connectionTypeMap = {
        1: 'LOCALHOST',
        2: 'IN_MEMORY'
    }

    observation = {
        'name': 'mysqlObservation',
        'databaseName': raw_input('Enter DB name : '),
        "connectionType": connectionTypeMap[int(raw_input('Enter connectionType 1.LOCALHOST  2.IN_MEMORY:'))]
    }
    if 'N' == raw_input('Do you want to observe the data inserted in mysql (Y/N) : ').upper():
        observation['data'] = {}
    else:
        tableNames = raw_input('Enter the table names separated by comma : ').split(',')
        data = {}
        for tableName in tableNames:
            data[tableName] = {}
            for j in range(0,int(raw_input("Enter the number of columns you need to observe in table "+ tableName+' : '))):
                data[tableName] = {
                    raw_input("Enter column name : "): raw_input("Enter column value : ")
                }
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
        dbIndices.append(int(raw_input('Enter DB number ' + str(i + 1) + ' : ')))
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
        if raw_input('Do you want to observe a document (Y/N) : ').upper() == 'Y':
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

    tableName = raw_input('Enter table name with prefix regression_ :')
    if 'regression_' not in tableName:
        tableName = raw_input('Table name entered did not have prefix regression_ . Try again')
        assert 'regression' in tableName
    observation['tableName'] = tableName

    observation['connectionType'] = connectionTypeMap[
        int(raw_input('Enter connectionType : \n1.REMOTE \n2.IN_MEMORY : '))]

    rows = []
    while True:
        if raw_input('Do you want to observe row (Y/N) : ').upper() == 'Y':
            row = {'rowKey': raw_input("Enter row key : ")}
            data = {}
            while True:
                if raw_input(
                        'Do you want to observe column family in the table ' + tableName + ' (Y/N) : ').upper() == 'Y':
                    colFamName = raw_input('Enter column family name : ')
                    columns = {}
                    for i in range(0, int(
                            raw_input("Enter the number of columns to be observed under family " + colFamName+' '))):
                        columnName = raw_input("Column name :  ")
                        columnValue = raw_input("Column value :  ")
                        columns[columnName] = columnValue
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
    observation = {
        'name': 'aerospikeObservation',
        'connectionInfo': {
            'host': raw_input("Enter aerospike host IP : "),
            'port': int(raw_input('Enter port number : ')),
            'user': raw_input("Enter aerospike user : "),
            'password': raw_input("Enter aerospike password : ")
        }
    }
    aerospikeData = []

    for i in range(0, int(raw_input('Enter the number of namespaces you want to observe : '))):
        namespace = {'namespace': raw_input('Enter the namespace name with prefix regression_ : ')}
        if 'regression_' not in namespace['namespace']:
            namespace['namespace'] = raw_input('The name entered did not have prefix regression_ . Try again : ')
            assert 'regression_' in namespace['namespace']

        namespace['set'] = raw_input('Enter set name : ')
        records = []
        while True:
            if raw_input('Do you want to observe records (Y/N) : ').upper() == 'Y':
                record = {'PK': raw_input('Enter primary key : ')}
                binData = {}
                for j in range(0, int(raw_input("Enter the number of bins to be observed : "))):
                    binKey = raw_input('Enter bin key : ')
                    binValue = raw_input('Enter bin value : ')
                    binData[binKey] = binValue
                record['binData'] = binData
                records.append(record)
            else:
                break
        namespace['records'] = records
        aerospikeData.append(namespace)

    observation['aerospikeData'] = aerospikeData
    return observation


def takeElasticSearchObservation():
    connectionTypeMap = {
        1: 'REMOTE',
        2: 'IN_MEMORY'
    }
    observation = {'name': 'elasticSearchObservation'}

    if connectionTypeMap[int(raw_input('Enter connectionType 1.REMOTE  2.IN_MEMORY: '))] =='REMOTE':
        observation['connectionInfo'] = {
            'clusterName': raw_input('Enter cluster name '),
            'host':raw_input('Enter the host IP '),
            'connectionType':'REMOTE'
        }

    observation['documentsToFetch'] = documentsToFetch = []
    while True:
        if raw_input('Do you want to observe a document (Y/N) : ').upper() == 'Y':
            document = {
                'index': raw_input(
                    'Enter the index name (add prefix \'regression_\' if connection type is REMOTE) : '),
                'typeName': raw_input('Enter type name : '),
                'queryFile': raw_input('Enter the path to json file having query : ')
            }
            if 'Y' == raw_input('Do you need to add routing key for the search (Y/N) : ').upper():
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
        choice = raw_input().upper()
        assert choice == 'Y' or choice == 'N'
        if choice == 'N':
            return observations

        print "Select Observation type 1.Http 2.Mysql 3.Redis 4.Solr 5.Hbase 6.aerospike 7.elasticsearch 8.hazelCast 9:Kafka 10:RMQ:",

        observationType = int(raw_input())
        assert 1 <= observationType <= 10

        prepareFunc = funcMapper[observationType]
        observations.append(prepareFunc())
