import json

from commonFunctions import takeHttpMethod
from commonFunctions import takeJsonFromFile
from commonFunctions import takeRequestBody
from commonFunctions import takeUrlPath


def takeRMQInput():
	# TODO
	return
def takeKafkaInput():	
	#TODO
	return

def takeAerospikeInput():
	#TODO
	return

def takeHbaseInput():
	#TODO
	return

def takeElasticSearchInput():
	#TODO
	return	

def takeHazelCastInput():
	#TODO
	return

def takeRedisInput():
	#TODO
	return	

def takeSolrInput():
	#TODO
	return	

def takeHttpInput():
	indirectInput = {}
	indirectInput['name'] = 'httpIndirectInput'
	specification = {}
	indirectInput['specification'] = specification
	request = {}
	specification['request'] = request
	request['method'] = takeHttpMethod()
	request['url'] = takeUrlPath()
	response = {}
	specification['response'] = response
	response['body'] = takeJsonFromFile()

	print 'IndirectInput is:'
	print '-------------------------------------------------'
	print json.dumps(indirectInput, indent = 4)
	print '-------------------------------------------------'
	return indirectInput


def takeMysqlInput():
	return
	# indirectInput = {}
	# indirectInput['name'] = 'mysqlIndirectInput'
	# indirectInput['connectionType'] = 'LOCALHOST'
	# print 'Please start mysql server on your local: limitation on HsqlDb to support all data types and features like hibernate_sequence'
	# print 'databaseName:', 
	# indirectInput['databaseName'] = raw_input()
	# assert len(indirectInput['databaseName']) !=0 

	# print 'Enter the file which contains schema/ddl statements for table creation eg : CREATE TABLE t1 .... each table creation in one line', 
	# fileName = raw_input()
	# ddlStatements = []
	# with open(fileName) as f:
	# 	for line in f:
	# 		ddlStatements.append(line)
	# indirectInput['ddlStatements'] = ddlStatements		


 #    while True:
	# 	print 'Do you want to load the data into mysql?(Y/N)',
	# 	choice = raw_input()
	# 	if(choice == 'N'):
	# 		return

	# 	print 'Enter the table Name to load the data',
	# 	tableName = raw_input()
	# 	print 'Enter the csv file which contains the data to be loaded into mysql, including headers',
	# 	fileName = raw_input()

	# 	with open(fileName) as csvfile:
	# 		reader = csv.reader(csvfile);
	# 		colNames = next(reader, None)
	# 		for row in reader:


def takeIndirectInputs():
	funcMapper = {
		1 : takeHttpInput,
		2 : takeMysqlInput,
		3 : takeRedisInput,
		4 : takeSolrInput,
		5 : takeHbaseInput,
		6 : takeAerospikeInput,
		7 : takeElasticSearchInput,
		8 : takeHazelCastInput,
		9 : takeKafkaInput,
		10: takeRMQInput
	}

	indirectInputs = []
	while True:
		print "Add dependencies?(Y/N)",
		choice = raw_input()
		assert choice == 'Y' or choice == 'N'
		if(choice == 'N'):
			return indirectInputs

		print "Select dependency type"
		print "1.Http Dependency"
		print "2.Mysql Dependency"
		print "3.Redis Dependency"
		print "4.Solr Dependency"
		print "5.Hbase Dependency"
		print "6.aerospike Dependency"
		print "7:elastic search dependency"
		print "8:hazelCast Dependency "
		print "9:Kafka Dependency"
		print "10: RMQ Dependency"

		depType = int(raw_input())
		assert depType >=1 and depType <= 2

		prepareFunc = funcMapper[depType]
		indirectInputs.append(prepareFunc())	



