# webapp
A cloud native application
# CSYE6225-Network Structures & Cloud Computing 
Web application built with Spring-Boot

## Build Instructions
* Clone the main branch of the repository.
* Run the following commands in cli: 
  * cd webapp,
  * mvn clean install
  * mvn spring-boot:run
  * For running r=test user: mvn clean test




## Assignment 1
-   Endpoint - 'http://localhost:8080/healthz'

## Assignment 2 
Endpoints:
  - GET:  http://localhost:8080/v1/account/{accountId}
  - PUT:  http://localhost:8080/v1/account/{accountId}
  - POST: http://localhost:8080/v1/account
  - GET:  http://localhost:8080/healthz

## Assignment 5
Endpoints:
- GET:  http://awsipv4:8080/v1/documents  (To get all documents for a user)
- DELETE: http://awsipv4:8080/v1/documents/{docId}  (To delete a document using docId)
- POST: http://awsipv4:8080/v1/documents (To add a document to S3 bucket)
- GET:  http://awsipv4:8080/v1/documents/{docId} (To get documents for a user, using docId)


| Name | NEU ID | Email Address              
|------| --- |----------------------------
| Sahil Mattoo | 002968582 | mattoo.s@northeastern.edu 




