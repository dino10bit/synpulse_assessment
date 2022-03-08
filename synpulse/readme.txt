This Rest api will retreive the transactions list from state store where the transactions are saved from a standalone spring boot application.
Docker compose has not been used.
These transactions are consumed from a topic using kafka streams API. 
As and when a message is received to the topic, kafka streams process the txn and save to state store on the disc.
A zookeeper instance and a kafka instance need to be run and a topic needs to be created in the kafka server.
Create a topic using the command below
kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic transactions
Also, kafka console producer has to be running to send a message like below
kafka-console-producer --broker-list localhost:9092 --topic transactions --property parse.key=true --property key.separator=, 
12345679,{"key":"12345679","accountIBAN":"GAH64840","amount":100.05,"description":"first transaction","valueDate":"06032022"}

Option 1 : Eclipse
-----------------------------------------------------
1. Import the source code (zip) to the eclipse.
2. Add maven plugin to eclipse if not present.
3. Go to Run Configurations...
4. Select maven build in the list of items and click new launch configuration.
5. In the main tab, for the Base directory field, select the workspace option and select the Project "Synpulse".
6. In the goals field, enter "clean install".
7. Point to the custom maven settings.xml location if any in the User Settings field. Click Apply and Click on Run.
8. Maven should automatically download the dependencies and the build should be successful. (Eclipse should point to JDK 1.8 instead of JRE for the build to be successful).
9. Once the build is successful, open SynpulseApplication class and run as java application. The application should start at port 8080 by default and can see the logs in the console window of eclipse.
10.Go to postman in chrome or postman app or any similar rest api testing tools to test the services.

Request Payloads:
--------------------------------
To get JWT token
Rest API Method: POST
URL: http://localhost:8080/synpulse/customer/authenticate
Sample Request JSON:
{
	"username" : "synpulse",
	"password" : "password"
}

Response Payload:
--------------------------------
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzeW5wdWxzZSIsImlzVmlldyI6dHJ1ZSwiZXhwIjoxNjQ2NjQ0NzUxLCJpYXQiOjE2NDY2NDQxNTF9.PwXY18mewW1yPr47OEWjZuaar9o5UAMooC3QSFZDRvoTf2NM97-pAfoZ_C5_QZM7g2kmMpONk8Rw50Dpgh2Whw"
}

To get Transactions list
Rest API Method: POST
URL: http://localhost:8080/synpulse/customer/transactions
Sample Request JSON:
{
	"userId" : "test",
	"month" : 12,
	"year" : 2022
}
Request Header=> Authorization: Bearer 'token received from authenticate api'

Response Payload:
--------------------------------
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzeW5wdWxzZSIsImlzVmlldyI6dHJ1ZSwiZXhwIjoxNjQ2NjQ0NzUxLCJpYXQiOjE2NDY2NDQxNTF9.PwXY18mewW1yPr47OEWjZuaar9o5UAMooC3QSFZDRvoTf2NM97-pAfoZ_C5_QZM7g2kmMpONk8Rw50Dpgh2Whw"
}

Option 2 : CMD (command prompt)
-----------------------------------------------------
1. Copy the Source code folder to a location of choice.
2. Open the command prompt.
3. Set the environment variables for maven and jdk either in command prompt or in the system user environment variables in control panel.
4. cd to the root location of the project. e.g cd D:\Synpulse
5. Enter "mvn clean install" and click enter. The build should be successful.
6. Enter "mvn spring-boot:run" and click enter. The application should be started successfully.
7. Test the services as mentioned in point 10 in option 1 using the Request payloads provided above.