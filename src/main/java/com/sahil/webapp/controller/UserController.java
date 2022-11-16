package com.sahil.webapp.controller;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.sahil.webapp.model.Document;
import com.sahil.webapp.model.User;
import com.sahil.webapp.service.DocumentService;
import com.sahil.webapp.service.UserServiceIn;
import com.sahil.webapp.util.Helper;
import com.timgroup.statsd.StatsDClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/v1")
public class UserController{ 
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    private static final StatsDClient statsd = HealthCheckController.statsd;
    @Autowired
    private UserServiceIn userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DocumentService documentService;
    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private AmazonDynamoDB dynamoDB;
    @Value("${bucket.name}")
    private String bucketName;

//    @GetMapping("/allUser")
//    @ResponseBody
//    public List<User> findUsers() {
//        List<User> userList = (List<User>) userService.findAll();
//        return userList;
//    }

    @GetMapping("/account/{accountId}")
    @ResponseBody
    public ResponseEntity getUserById(@RequestHeader("Authorization") String authToken,  @PathVariable String accountId) {
        try {
            statsd.incrementCounter("/v1/account/{accountId}.http.get");
            LOGGER.info("API Call:: Get user by ID");
            Helper helper = new Helper();

            String usernameFromRequestHeader= helper.hashToStringFromRequest(authToken);
            //User user = (User) userService.findUserById(Long.parseLong(accountId));
            User user = (User) userService.findUserById(UUID.fromString(accountId));
            if(user.getUsername().equals(usernameFromRequestHeader) &&
            user.getVerificationStatus().equals("V")){
                return new ResponseEntity(helper.userToMap(user), HttpStatus.OK);
            }
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e){
            return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);

        }
    }

    @PutMapping("/account/{accountId}")
    public ResponseEntity updateUser(@RequestBody User us, @PathVariable String accountId,
                                     @RequestHeader("Authorization") String authToken) {
        try {
            statsd.incrementCounter("/v1/account/{accountId}.http.put");
            LOGGER.info("API Call:: Update user by ID");
            Helper helper = new Helper();

            String usernameFromRequestHeader= helper.hashToStringFromRequest(authToken);
            //User userFromDB = (User) userService.findUserById(Long.parseLong(accountId));
            User userFromDB = (User) userService.findUserById(UUID.fromString(accountId));
            if(userFromDB==null){
                return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);
            }

            if(userFromDB.getUsername().equals(usernameFromRequestHeader) &&
            userFromDB.getVerificationStatus().equals("V")){
                if(helper.putRequestCheck(us)){

                User updatedUser= helper.userUpdate(us,userFromDB);
                User user = userService.updateUser(updatedUser);
                LOGGER.info("::::User updation successful::::::");
                return new ResponseEntity("No Content",HttpStatus.NO_CONTENT);

                }else{
                    return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);
                }

            }else{
                return new ResponseEntity("Unauthorized",HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e){
            if(e.getMessage().contains("No value present")){
                return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);
                  }
            LOGGER.info("Bad request");
            return new ResponseEntity("Bad Request",HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/account")
    public ResponseEntity createUser(@RequestBody User us) {
        try {
            statsd.incrementCounter("/v1/account.http.post");
            LOGGER.info("API Call:: Create user");
            UUID tokenUUID= UUID.randomUUID();
            String tokenForUserVerification= tokenUUID.toString()+ "_" + new Timestamp(System.currentTimeMillis()).toString();
            User userDB= userService.findByUsername(us.getUsername());
            if(userDB!=null){
                return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);
            }
            Helper helper = new Helper();
            if(helper.postRequestCheck(us)){
                    us.setAccountCreated(new Timestamp(System.currentTimeMillis()));
                    String password= us.getPassword();
                    us.setPassword(passwordEncoder.encode(password));
                    us.setVerificationStatus("N");
                    User user = userService.createUser(us);
                    // Updating in Dynamo for lambda
                /* Create an Object of PutItemRequest */
                PutItemRequest request = new PutItemRequest();

                /* Setting Table Name */
                request.setTableName("csye-6225");

//                request.setReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);
//
//                request.setReturnValues(ReturnValue.ALL_OLD);

                /* Create a Map of attributes */
                Map<String, AttributeValue> map = new HashMap<>();
                map.put("Email", new AttributeValue(us.getUsername()));
                map.put("TokenName", new AttributeValue(tokenForUserVerification));
                map.put("TimeToLive", new AttributeValue(us.getAccountCreated().toString()));
                request.setItem(map);
                PutItemResult result = dynamoDB.putItem(request);
                LOGGER.info("Result from DynamoDB: "+ result.getSdkHttpMetadata().getHttpStatusCode());
                Map<String, String> msg = new HashMap<>();
                msg.put("username", us.getUsername());
                msg.put("token", tokenForUserVerification);
//                ObjectMapper objectMapper = new ObjectMapper();
//
//
//                String json = objectMapper.writeValueAsString(msg);
                JSONObject json = new JSONObject(msg);
                LOGGER.info("Converted to json string: "+json.toString());

//                StringBuilder mapAsString = new StringBuilder("{");
//                for (Integer key : map.keySet()) {
//                    mapAsString.append(key + "=" + map.get(key) + ", ");
//                }
//                mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");

                AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

//                CreateTopicResult topicResult = snsClient.createTopic("verify_email");
//                String topicArn = topicResult.getTopicArn();

                PublishRequest publishRequest = new PublishRequest();
                publishRequest.setMessage(json.toString());
                publishRequest.setTopicArn("arn:aws:sns:us-east-1:307333117455:verify_email");
                publishRequest.setSubject(tokenForUserVerification);

                LOGGER.info("Verification requested"+publishRequest.getMessage());
                final PublishResult publishResponse = snsClient.publish(publishRequest);
                LOGGER.info("Verification request sent successfully");
                return new ResponseEntity(helper.userToMap(user), HttpStatus.CREATED);

                }
            else{
                LOGGER.info("Bad request, unable to handle. Please check JSON parameters");
                return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);

            }

        }
        catch (Exception e){
            LOGGER.info("Bad request, unable to handle. Please check JSON parameters");
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        }

    }

    /*
     * Document endpoints
     */
    @PostMapping("/documents")
    public ResponseEntity createDoc(@RequestParam("file") MultipartFile file,
                                    @RequestHeader("Authorization") String authToken) {
        try {
            statsd.incrementCounter("/v1/documents.http.post");
            LOGGER.info("API Call:: Create document");
            Helper helper = new Helper();

            String usernameFromRequestHeader = helper.hashToStringFromRequest(authToken);
            //User userFromDB = (User) userService.findUserById(Long.parseLong(accountId));
            User userFromDB = (User) userService.findByUsername(usernameFromRequestHeader);
            if (userFromDB == null) {
                return new ResponseEntity("Forbidden", HttpStatus.FORBIDDEN);
            } else {
                //Creating document
                if (userFromDB.getVerificationStatus().equals("N")) {
                    return new ResponseEntity("Forbidden", HttpStatus.FORBIDDEN);
                }
                File modifiedFile = new File(file.getOriginalFilename());
                FileOutputStream os = new FileOutputStream(modifiedFile);
                os.write(file.getBytes());
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                //Sending it to S3

                s3Client.putObject(bucketName, fileName, modifiedFile);
                modifiedFile.delete();
                Document doc = new Document();
                doc.setName(fileName);
                doc.setDocumentCreated(new Timestamp(System.currentTimeMillis()));
                doc.setS3BucketPath(s3Client.getUrl(bucketName,fileName).toString());
                doc.setUser(userFromDB);
                doc.setStatus("ACTIVE");
                documentService.save(doc);
                return new ResponseEntity(helper.documentToMap(doc), HttpStatus.CREATED);
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }
        @GetMapping ("/documents/{documentId}")
        public ResponseEntity getDoc( @PathVariable String documentId,
                @RequestHeader("Authorization") String authToken) {
            try {
                statsd.incrementCounter("/v1/documents/{documentId}.http.get");
                LOGGER.info("API Call:: Get document by ID");
                Helper helper = new Helper();
                String usernameFromRequestHeader= helper.hashToStringFromRequest(authToken);
                User userFromDB = (User) userService.findByUsername(usernameFromRequestHeader);
                Document docFromDB= (Document) documentService.findById(UUID.fromString(documentId));
                if(userFromDB==null || docFromDB==null){
                    return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);
                }else {

                    if(userFromDB.getId()==docFromDB.getUser().getId()
                    && docFromDB.getStatus().equals("ACTIVE") &&
                    userFromDB.getVerificationStatus().equals("V")){
                        return new ResponseEntity(helper.documentToMap(docFromDB), HttpStatus.OK);

                    }

                    return new ResponseEntity(HttpStatus.FORBIDDEN);
                }
            }
            catch (Exception e){
                LOGGER.info(e.getMessage());
                return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
            }

    }

    @DeleteMapping ("/documents/{documentId}")
    public ResponseEntity deleteDoc( @PathVariable String documentId,
                                  @RequestHeader("Authorization") String authToken) {
        try {
            statsd.incrementCounter("/v1/documents/{documentId}.http.delete");
            LOGGER.info("API Call:: Delete document by ID");
            Helper helper = new Helper();
            String usernameFromRequestHeader= helper.hashToStringFromRequest(authToken);
            User userFromDB = (User) userService.findByUsername(usernameFromRequestHeader);
            Document docFromDB= (Document) documentService.findById(UUID.fromString(documentId));
            if(userFromDB==null || docFromDB==null|| userFromDB.getId()!=docFromDB.getUser().getId()){
                return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);
            }else {
                if(userFromDB.getVerificationStatus().equals("N")){
                    return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);
                }

                if(userFromDB.getId()==docFromDB.getUser().getId()
                        && docFromDB.getStatus().equals("ACTIVE")){
                    s3Client.deleteObject(bucketName,docFromDB.getName());
                    docFromDB.setStatus("DELETED");
                    documentService.save(docFromDB);
                    return new ResponseEntity(HttpStatus.NO_CONTENT);
                }

                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOGGER.info(e.getMessage());
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping ("/documents")
    public ResponseEntity<List<Map>> getAllDoc(@RequestHeader("Authorization") String authToken) {
        try {
            statsd.incrementCounter("/v1/documents.http.get");
            LOGGER.info("API Call:: Get document by ID");
            Helper helper = new Helper();
            String usernameFromRequestHeader= helper.hashToStringFromRequest(authToken);
            User userFromDB = (User) userService.findByUsername(usernameFromRequestHeader);

            if(userFromDB==null){
                return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);
            }else {
                if(userFromDB.getVerificationStatus().equals("N")){
                    return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);
                }
                List<Document> docFromDB= documentService.findAll(userFromDB);

                return new ResponseEntity<>(helper.documentListToMap(docFromDB),HttpStatus.OK);
            }
        }
        catch (Exception e){
            LOGGER.info(e.getMessage());
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        }

    }
}
