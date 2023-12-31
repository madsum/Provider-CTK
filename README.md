<p align="left">
  <a href="https://github.com/dcsaorg/DCSA-Certification-ToolKit-API-Provider"><img alt="GitHub Actions status" src="https://github.com/actions/setup-java/workflows/Main%20workflow/badge.svg"></a>
</p>

Status
-------------------------------------
This certification toolkit is targeted at the DCSA standard for Track and Trace v2.2.

This certification toolkit is currently in a "pre-alpha" development stage. It must not be used to establish conformance of implementations of the standard.

It is made available in order to allow early feedback on the toolkit itself.

# DCSA-Certification-ToolKit-API-Provider
Concept version of a new certification toolkit.

The DCSA certification toolkit will be used to test a given implementation of
DCSA's OpenAPI against the reference test suite to ensure that all
endpoints are behaving as expected.

### Technology Stack
* Java 17
* Cucumber
* RestAssued/TestNG

### Project Setup
#### 1. Clone the repository
`git clone https://github.com/dcsaorg/DCSA-Certification-ToolKit-API-Provider.git`

#### 1. Setup the environment
Export following environment variables
```shell
# API_ROOT_URI-This is where the API validator can find the implementation of the API
exmaple: API_ROOT_URI=http://192.168.178.38:9090/v2 or https://apis.dsca.org/apis/v2 
# CALLBACK_URI-Base Domain URL/DNS Combability Kit used to send in request body
exmaple: CALLBACK_URI=http://192.168.178.38:9092 or http://subscriptions.dsca.org/callback
# CALLBACK_PORT callback APIs server listener port. Should be in Sync with CALLBACK_URI port
exmaple: CALLBACK_PORT=9092
# Max wait time in milliseconds, CTK will wait for any call back request to be received e.g., notification/head request
exmaple: CALLBACK_WAIT=20000
# TNT API developer provide test data in json formatted file and define TEST_DATA environment variables 
# If no TEST_DATA defined, it usages default test data "config/v2/testdata.json"   
exmaple: TEST_DATA=testdata.json
# TNT API developer provide configuration data in json formatted file and define CONFIG_DATA environment variables 
# If no CONFIG_DATA defined, it usages default configuration data "config/v2/config.json"   
exmaple: CONFIG_DATA=testdata.json
```

#### 2. API under test

To test an API, the implementor will need the following:

* A running instance of the API that will be tested.
* The API should be loaded with prerequisites reference data.
* The certification toolkit should be configured with test data provided mentioned in the API testing instructions
* The certification toolkit SHOULD only be run on test systems(production like)

#### 2. Run DCSA TNT - Track & Trace
Follow instruction @ https://github.com/dcsaorg/DCSA-TNT

### Executing the certification toolkit

#### 1. Configure Compatibility Kit
Configure the compatibility kit with you test data in config.json file present under resources as per instruction provided in compatibility tool kit for that API

#### 2. Run Compatibility Kit
Run the relevant test suite (here TnT APIs) with following options
```shell
1.using maven
mvn clean compile exec:java -Dexec.mainClass=org.testng.TestNG -Dexec.args="suitexmls\TNT-TestSuite.xml"
Or
2.Use IDE to run it as TestNG projects
Or
3. Create package using bat file(for window) e.g. CreatePackage_TNT.bat and run following command from ctk folder under package (e.g. TNT-v2.2.0/ctk)
java -jar DCSA-Validator-Toolkit.jar TestSuite.xml
```
Also run it by docker-compose:
```shell
// build and run
docker-compose up --build --remove-orphans
// run
docker-compose up
```


### Test Reports
Following test result reports will be generated under reports folder or under mounted volume(in case of docker), you can verify the result in case of any discrepancies:
* Detailed Html report at technical level to verify each steps performed to validate a test case

For the newman reporting prerequisite must be system must install this extension  
```shell
npm install -g newman
npm install -g newman-reporter-htmlextra
```