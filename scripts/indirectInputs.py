from commonFunctions import takeHttpMethod
from commonFunctions import takeJsonFromFile
from commonFunctions import takeUrlPath


def takeRMQInput():
    indirectInput = {
        'name': 'rmqIndirectInput',
        'queue': raw_input('Enter queue name to load the messages into : ')
    }

    messages = []
    messageCount = int(raw_input('Enter the number of messages to be added :'))
    print 'Enter message in each line'
    for i in range(messageCount):
        messages.append(raw_input())
    indirectInput['messages'] = messages
    return indirectInput


def takeKafkaInput():
    indirectInput = {
        'name': 'kafkaIndirectInput',
        'topic': raw_input('Enter topic name : ')
    }

    messages = []
    messageCount = int(raw_input('Enter the number of messages to be added :'))
    print 'Enter message in each line'
    for i in range(messageCount):
        messages.append(raw_input())
    indirectInput['messages'] = messages
    return indirectInput


def takeAerospikeInput():
    indirectInput = {
        'name': 'aerospikeIndirectInput',
        'connectionInfo': {
            'host': raw_input("Enter aerospike host IP : "),
            'port': int(raw_input('Enter port number : '))
        }
    }
    aerospikeData = []

    for i in range(0, int(raw_input('Enter the number of namespaces required : '))):
        namespace = {
            'namespace': raw_input('Enter the namespace name with prefix regression_ : ')
        }
        if 'regression_' not in namespace['namespace']:
            namespace['namespace'] = raw_input('The name entered did not have prefix regression_ . Try again : ')
            assert 'regression_' in namespace['namespace']

        namespace['set'] = raw_input('Enter set name : ')
        records = []
        while True:
            if raw_input('Do you want to add records (Y/N) : ').upper() == 'Y':
                record = {'PK': raw_input('Enter primary key : ')}
                binData = {}
                for j in range(0, int(raw_input("Enter the number of bins to be added : "))):
                    binKey = raw_input('Enter bin key : ')
                    binValue = raw_input('Enter bin value : ')
                    binData[binKey] = binValue
                record['binData'] = binData
                records.append(record)
            else:
                break
        namespace['records'] = records
        aerospikeData.append(namespace)

    indirectInput['aerospikeData'] = aerospikeData
    return indirectInput


def takeHbaseInput():
    connectionTypeMap = {
        1: 'REMOTE',
        2: 'IN_MEMORY'
    }
    indirectInput = {'name': 'hbaseIndirectInput'}

    tableName = raw_input('Enter table name with prefix regression_ :')
    if 'regression_' not in tableName:
        tableName = raw_input('Table name entered did not have prefix regression_ . Try again')
        assert 'regression' in tableName
    indirectInput['tableName'] = tableName

    indirectInput['connectionType'] = connectionTypeMap[
        int(raw_input('Enter connectionType : \n1.REMOTE \n2.IN_MEMORY : '))]

    rows = []
    while True:
        if raw_input('Do you want to add row (Y/N) : ').upper() == 'Y':
            row = {'rowKey': raw_input("Enter row key : ")}
            data = {}
            while True:
                if raw_input('Do you want to add column family in the table ' + tableName + ' (Y/N) : ').upper() == 'Y':
                    colFamName = raw_input('Enter column family name : ')
                    columns = {}
                    for i in range(0, int(
                            raw_input("Enter the number of columns to want to add under column family " + colFamName))):
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
    indirectInput['rows'] = rows

    return indirectInput


def takeElasticSearchInput():
    connectionTypeMap = {
        1: 'REMOTE',
        2: 'IN_MEMORY'
    }
    indirectInput = {'name': 'elasticSearchIndirectInput'}

    if connectionTypeMap[int(raw_input('Enter connectionType 1.REMOTE  2.IN_MEMORY: '))] =='REMOTE':
        indirectInput['connectionInfo'] = {
            'clusterName': raw_input('Enter cluster name '),
            'host':raw_input('Enter the host IP '),
            'connectionType':'REMOTE'
        }


    documentsOfIndexAndType = []
    while True:
        if raw_input('Do you want to add an index (Y/N) : ').upper() == 'Y':
            index = {
                'index': raw_input(
                    'Enter the index name (add prefix \'regression_\' if connection type is REMOTE) : '),
                'type': raw_input('Enter the typename : '),
                'mappingFile': raw_input('Enter the path to mapping file : ')
            }
            if raw_input(" Do you want to use routing key for the data insertion in ES (Y/N) : ").upper() == 'Y':
                index['routingKey'] = raw_input('Enter the routing key value : ')

            documents = []
            while True:
                if raw_input('Do you want to add a document under the index regression_' + index['index'] + ' (Y/N) : ').upper() == 'Y':
                    document = {'_id': raw_input('Enter the document id : ')}
                    document.update(takeJsonFromFile())
                    documents.append(document)
                else:
                    break
            index['documents'] = documents
        else:
            break
        documentsOfIndexAndType.append(index)
    indirectInput['documentsOfIndexAndType'] = documentsOfIndexAndType
    return indirectInput


def takeHazelCastInput():
    def getMaps():
        maps = {}
        while True:
            if raw_input('Do you want to add map (Y/N) : ').upper() == 'Y':
                mapName = raw_input('Enter map name : ')
                map = maps[mapName] = {}
                mapData = {}
                for i in range(0, int(raw_input("Enter the number of K-V pair to be added : "))):
                    testKey = raw_input('Enter key : ')
                    value = raw_input('Enter value : ')
                    mapData[testKey] = value
                map['mapData'] = mapData
                map['keyClass'] = raw_input('Enter complete canonical class name of the key ')
                map['valueClass'] = raw_input('Enter complete canonical class name of the value ')
            else:
                break
        return maps

    hazelCastType = {
        1: 'Embedded',
        2: 'Remote'
    }

    selectDS = {
        1: 'maps',
        2: 'none'
    }
    indirectInput = {}
    hazelCastCluster = hazelCastType[
        int(raw_input('Enter the type of Hazelcast cluster you need to run/connect to : \n1.Embedded\n2.Remote\n'))]
    if hazelCastCluster == 'Embedded':
        indirectInput = {
            'name': 'embeddedhzIndirectInput'
        }
        hazelcastDS = {}
        while True:
            ds = selectDS[int(raw_input('Enter the type of data structure you want to load : \n1.maps\n2.none '))]
            if ds == 'none':
                break
            elif ds == 'maps':
                hazelcastDS[ds] = getMaps()
        indirectInput['hazelcastDS'] = hazelcastDS
    elif hazelCastCluster == 'Remote':
        indirectInput = {
            'name': 'serverhzIndirectInput',
            'group': raw_input('Enter group name : '),
            'password': raw_input('Enter password : '),
            'user': raw_input('Enter user name : '),
        }
        hazelcastDS = {}
        while True:
            ds = selectDS[int(raw_input('Enter the type of data structure you want to load : \n1.maps\n2.none '))]
            if ds == 'none':
                break
            elif ds == 'maps':
                hazelcastDS[ds] = getMaps()
        indirectInput['hazelcastDS'] = hazelcastDS

        serializerConfigMap = {}
        while True:
            choice = raw_input('Do you want to add serializer class in serializer config map (Y/N) : ')
            if choice == 'Y':
                className = raw_input('Enter class name : ')
                serializerClass = raw_input('Enter serializer class name : ')
                serializerConfigMap[className] = serializerClass
            else:
                break
        indirectInput['serializerConfigMap'] = serializerConfigMap

    return indirectInput


def takeRedisInput():
    def getHashMap():
        hashMap = {}
        while True:
            if raw_input('Do you want to add an outer key (Y/N) : ').upper() == 'Y':
                outerKey = raw_input('Enter outer key : ')
                innerMap = {}
                for j in range(0, int(raw_input("Enter the number of K-V pair to be added under " + outerKey))):
                    innerKey = raw_input('Enter the inner key : ')
                    innerValue = raw_input('Enter the inner value : ')
                    innerMap[innerKey] = innerValue
                hashMap[outerKey] = innerMap
            else:
                break
        return hashMap

    def getKeyValues():
        keyValues = {}
        for j in range(0, int(raw_input('Enter the number of key:value pair to be added : '))):
            key = raw_input('Enter key : ')
            val = int(raw_input('Enter value : '))
            keyValues[key] = val
        return keyValues

    def getSortedSets():
        sortedSets = {}
        for j in range(0, int(raw_input('Enter the number of sorted set to be added : '))):
            setName = raw_input('Enter name of set ' + str(i + 1) + ' :')
            set = sortedSets[setName] = {}
            for j in range(0, int(raw_input("Enter the number of elements to be added in set " + setName + ' :'))):
                key = raw_input('Enter mem key :')
                value = raw_input('Enter value :')
                set[key] = value
        return sortedSets

    def getSet():
        sets = {}
        for j in range(0, int(raw_input('Enter the number of sets to be added : '))):
            setKey = raw_input('Enter set key : ')
            set = sets[setKey] = []
            values = int(raw_input('Enter the number of elements to be added under set ' + setKey + ' : '))
            print 'Enter value in each line'
            for j in range(values):
                set.append(raw_input())
        return sets

    def getLists():
        lists = {}
        for j in range(0, int(raw_input('Enter the number of list you want to use : '))):
            listKey = raw_input('Enter list key : ')
            list = lists[listKey] = []
            values = int(raw_input('Enter the number of elements to be added under : ' + listKey))
            print 'Enter value in each line'
            for j in range(values):
                list.append(raw_input())
        return lists

    clusterType = {
        1: 'SENTINEL',
        2: 'SINGLE_HOST'
    }
    dataStructure = {
        1: 'hashMap',
        2: 'keyValues',
        3: 'sortedSets',
        4: 'sets',
        5: 'lists',
        6: 'none'
    }

    indirectInput = {
        'name': 'redisIndirectInput',
        "clusterType": clusterType[int(raw_input('Enter the type of cluster 1.SENTINEL 2.SINGLE_HOST: '))],
        "masterName": "master"
    }
    dbToDSMap = {}
    for i in range(0, int(raw_input('How many databases do you need to load : '))):
        dbNumber = raw_input('Enter the db number : ')
        dbMap = {}
        while True:
            ds = dataStructure[int(raw_input('Enter the data structure that you want to add : '
                                             '1.hashMap 2.keyValues 3.sortedSets 4.sets 5.lists 6.none: '))]
            if ds == 'none':
                break
            elif ds == 'hashMap':
                dbMap[ds] = getHashMap()
            elif ds == 'keyValues':
                dbMap[ds] = getKeyValues()
            elif ds == 'sortedSets':
                dbMap[ds] = getSortedSets()
            elif ds == 'sets':
                dbMap[ds] = getSet()
            elif ds == 'lists':
                dbMap[ds] = getLists()
            dbToDSMap[dbNumber] = dbMap
    indirectInput['dbToDSMap'] = dbToDSMap
    return indirectInput


def takeSolrInput():
    connectionTypeMap = {
        1: 'LOCALHOST',
        2: 'IN_MEMORY'
    }

    indirectInput = {
        "name": "solrIndirectInput",
        "connectionType": connectionTypeMap[
            int(raw_input('Enter connectionType 1.LOCALHOST 2.IN_MEMORY: '))],
        "solrData":
            {
                "coreName": raw_input('Enter core name : '),
                "solrConfigFiles": raw_input(
                    'Enter the path of folder containing solr.xml,solrconfig.xml and managed-schema : '),
                "uniqueKey": raw_input('Enter  unique key of the document : ')
            }
    }
    documents = []
    while True:
        if raw_input('Do you want to add document (Y/N) : ').upper() == 'Y':
            document = takeJsonFromFile()
            documents.append(document)
        else:
            break
    solrData = {'documents': documents}
    indirectInput['solrData'].update(solrData)

    return indirectInput


def takeHttpInput():
    indirectInput = {
        'name': 'httpIndirectInput',
        'specification': {
            'request': {
                'method': takeHttpMethod(),
                'url': takeUrlPath()
            },
            'response': {
                'body': takeJsonFromFile("Enter the file containing response expected:")
            }
        }
    }
    return indirectInput


def takeMysqlInput():
    connectionTypeMap = {
        1: 'LOCALHOST',
        2: 'IN_MEMORY'
    }

    indirectInput = {
        'name': 'mysqlIndirectInput',
        'databaseName': raw_input('Enter DB name : '),
        'connectionType': connectionTypeMap[int(raw_input('Enter connectionType 1.LOCALHOST  2.IN_MEMORY: '))],
        'ddlStatements': []
    }

    statementsFile = open(raw_input(
        'Enter the path to .sql file that contains SQL commands to run one command each line: create table, insert data: '),
        'r')
    sqlFile = statementsFile.read()
    statementsFile.close()
    sqlCommands = sqlFile.split(";")
    for command in sqlCommands:
        indirectInput['ddlStatements'].append(command)
    return indirectInput


def takeIndirectInputs():
    funcMapper = {
        1: takeHttpInput,
        2: takeMysqlInput,
        3: takeRedisInput,
        4: takeSolrInput,
        5: takeHbaseInput,
        6: takeAerospikeInput,
        7: takeElasticSearchInput,
        8: takeHazelCastInput,
        9: takeKafkaInput,
        10: takeRMQInput
    }

    indirectInputs = []
    message = 'Add dependencies(Y/N)?'
    while True:
        print message,
        message = 'Add more dependencies(Y/N)?'
        choice = raw_input()
        assert choice.upper() == 'Y' or choice.upper() == 'N'
        if choice.upper() == 'N':
            break

        print "Enter dependency number 1.Http 2.Mysql 3.Redis 4.Solr 5.Hbase 6.Aerospike 7.elasticsearch 8.hazelCast 9.Kafka 10.RMQ: ",

        depType = int(raw_input())
        assert 1 <= depType <= 10

        prepareFunc = funcMapper[depType]
        indirectInputs.append(prepareFunc())
    return indirectInputs
    # print json.dumps(indirectInputs, indent=4)

# takeIndirectInputs()
