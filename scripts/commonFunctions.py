import json

def takeHttpMethod():
	print "Enter the HttpMethod: GET/POST/DELETE/PUT:",
	method = raw_input();
	assert len(method) != 0 and (method == 'GET' or method == 'POST' or method == "DELETE" or method == "PUT")
	return method

def takeJsonFromFile(message = "Enter the file contains json:"):
	print message,
	requestPayloadFile = raw_input()
	return json.loads(open(requestPayloadFile).read())	

def takeRequestBody(method):
	if(method == 'GET'):
		return
	print "Do you have request payload:(Y/N)", 
	choice = raw_input();
	assert choice == 'Y' or choice == 'N'

	if(choice == 'N'):
		return

	return takeJsonFromFile()	

def takeUrlPath():
	print "Enter the api path starting with /:",
	path = raw_input();
	assert len(path) != 0 and path[0] == '/'
	return path	