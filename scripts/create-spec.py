
import json
import csv


from indirectInputs import takeIndirectInputs
from Observations import takeObservations

from commonFunctions import takeHttpMethod
from commonFunctions import takeJsonFromFile
from commonFunctions import takeRequestBody
from commonFunctions import takeUrlPath


def takeDirectInput():
	directInput = {}
	directInput['name'] = 'httpDirectInput'
	directInput['method'] = takeHttpMethod()
	directInput['path'] = takeUrlPath()
	directInput['request'] = takeRequestBody(directInput['method']);
	return directInput




print '-----------------------------------------------------'
print 'A step by step guide to create a test specification'
print '-----------------------------------------------------'

print 'Enter 1 for HttpAPI, 2 for Storm Topology:',

choice = int(raw_input());
assert choice == 1 or choice == 2 

testSpecification = {}
if(choice == 1):
	testSpecification['directInput'] = takeDirectInput()

indirectInputs = takeIndirectInputs()
observations = takeObservations()

testSpecification['indirectInputs'] = indirectInputs
testSpecification['observations'] = observations

print 'Final Test Specification is:'
print "------------------------------------------------------------------------------------"
print json.dumps(testSpecification, indent=4)
print '------------------------------------------------------------------------------------'







