import json


def takeHttpMethod():
    print "Enter the HttpMethod: GET/POST/DELETE/PUT : ",
    method = raw_input();
    assert len(method) != 0 and (method.upper() == 'GET' or method.upper() == 'POST' or method.upper() == "DELETE" or method.upper() == "PUT")
    return method.upper()


def takeJsonFromFile(message="Enter the file containing json : "):
    print message,
    requestPayloadFile = raw_input()
    return json.loads(open(requestPayloadFile).read())


def takeRequestBody(method):
    if method == 'GET':
        return
    print "Do you have request payload:(Y/N) ",
    choice = raw_input();
    assert choice.upper() == 'Y' or choice.upper() == 'N'
    if choice == 'N':
        return
    return takeJsonFromFile()


def takeUrlPath():
    print "Enter the api path starting with /:",
    path = raw_input();
    if path[0] != "/":
        path = "/"+path
    assert len(path) != 0 and path[0] == '/'
    return path
