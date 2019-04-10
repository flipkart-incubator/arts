import json
from commonFunctions import takeHttpMethod
from commonFunctions import takeJsonFromFile
from commonFunctions import takeRequestBody
from commonFunctions import takeUrlPath




def takeHttpObservation():
	return

def takeMysqlObservation():	
	return

def takeRedisObservation():
	return	

def takeSolrObservation():
	return	

def takeHbaseObservation():	
	return

def takeAerospikeObservation():
	return

def takeElasticSearchObservation():
	return

def takeHazelCastObservation():
	return

def takeKafkaObservation():
	return	

def takeRMQObservation():
	return	

def takeObservations():

	funcMapper = {
		1 : takeHttpObservation,
		2 : takeMysqlObservation,
		3 : takeRedisObservation,
		4 : takeSolrObservation,
		5 : takeHbaseObservation,
		6 : takeAerospikeObservation,
		7 : takeElasticSearchObservation,
		8 : takeHazelCastObservation,
		9 : takeKafkaObservation,
		10: takeRMQObservation
	}


	observations = []

	while True:
		print "Do you want to observe results? (Y/N)",
		choice = raw_input()
		assert choice == 'Y' or choice == 'N'
		if(choice == 'N'):
			return observations

		print "Select Observation type"
		print "1.Http Observation/Response of API"
		print "2.Mysql Observation"
		print "3.Redis Observation"
		print "4.Solr Observation"
		print "5.Hbase Observation"
		print "6.aerospike Observation"
		print "7:elastic search Observation"
		print "8:hazelCast Observation "
		print "9:Kafka Observation"
		print "10:RMQ Observation"

		observationType = int(raw_input())
		assert observationType >=1 and observationType <= 10

		prepareFunc = funcMapper[observationType]
		observations.append(prepareFunc())	
			