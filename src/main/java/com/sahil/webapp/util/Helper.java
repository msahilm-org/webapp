package com.sahil.webapp.util;

import com.sahil.webapp.model.Document;
import com.sahil.webapp.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

public class Helper {



    public User userUpdate(User userReq, User userDB){
    if(userReq.getFirstName()!=null) {
        if (!userReq.getFirstName().trim().equals("")) {
            userDB.setFirstName(userReq.getFirstName());
        }
    }
    if(userReq.getLastName()!=null){
        if(!userReq.getLastName().trim().equals("")){
            userDB.setLastName(userReq.getLastName());
        }
    }
    if(userReq.getPassword()!=null){
        if(!userReq.getPassword().trim().equals("")){
            userDB.setPassword(new BCryptPasswordEncoder().encode(userReq.getPassword()));
        }
    }

    userDB.setAccountUpdated(new Timestamp(System.currentTimeMillis()));
    return userDB;

    }


    public Map userToMap(User user){
        Map<String, String> map= new HashMap<String, String>();
        map.put("id", String.valueOf(user.getId()));
        map.put("first_name", user.getFirstName());
        map.put("last_name", user.getLastName());
        map.put("username", user.getUsername());
        map.put("account_created", user.getAccountCreated().toString());
        map.put("account_updated", user.getAccountUpdated()==null?  "null":  user.getAccountUpdated().toString());
        return map;
    }
    public boolean checkUserDtls(User user){
        if(user.getFirstName().equals("") || user.getFirstName().isBlank() || user.getFirstName()==null)
            return false;
        else if(user.getLastName().equals("") || user.getLastName().isBlank() || user.getLastName()==null)
            return false;
        return true;

    }
    public boolean validUsername(User user){
        if(user.getUsername().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
           // System.out.println(user.getUsername().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"));
            return true;
        }else{
            return false;
        }
    }

    public String hashToStringFromRequest(String authoToken){
        String hash= authoToken.split(" ")[1];
        //LOGGER.info(":::::::::::This is hash:::::::::::" + hash );
        String username= new String(Base64.getDecoder().decode(hash), StandardCharsets.UTF_8).split(":")[0];
//            LOGGER.info(":::::::::::This is username:::::::::::" + username );
        return username;
    }

    public boolean postRequestCheck(User user){

        try{

            if(user.getUsername()==null || user.getUsername().trim().isBlank() || user.getUsername().trim().equals("") )
                return false;
            if(user.getLastName()==null || user.getLastName().trim().isBlank() || user.getLastName().trim().equals("") )
                return false;
            if(user.getPassword()==null || user.getPassword().trim().isBlank() || user.getPassword().trim().equals("") )
                return false;
            if(user.getFirstName()==null || user.getFirstName().trim().isBlank() || user.getFirstName().trim().equals("") )
                return false;
            if(user.getAccountCreated()!=null )
                return false;
            if(user.getAccountUpdated()!=null )
                return false;
            if(this.validUsername(user)){
                return true;
            }else{
                return false;
            }

        }catch (Exception e){
            return false;
        }

    }
    public boolean putRequestCheck(User user){

        try{

            if( (user.getFirstName()==null || user.getFirstName().trim().isBlank() || user.getFirstName().trim().equals("") ) &&
                    (user.getLastName()==null || user.getLastName().trim().isBlank() || user.getLastName().trim().equals(""))&&
                            (user.getPassword()==null || user.getPassword().trim().isBlank() || user.getPassword().trim().equals("")))
            {
                return false;
            }
            if(user.getAccountCreated()!=null )
                return false;
            if(user.getAccountUpdated()!=null )
                return false;
            if(user.getUsername()!=null)
                return false;

            return true;

        }catch (Exception e){
            return false;
        }

    }

    public Map documentToMap(Document doc){
        Map<String, String> map= new HashMap<String, String>();
        map.put("doc_id", String.valueOf(doc.getDocId()));
        map.put("user_id", doc.getUser().getId().toString());
        map.put("name", doc.getName());
        map.put("date_created", doc.getDocumentCreated().toString());
        map.put("s3_bucket_path", doc.getS3BucketPath());
        return map;
    }

    public List<Map> documentListToMap(List<Document> document){
        List<Map> mapList= new ArrayList<>();
        for(Document doc:document){
            Map<String, String> map= new HashMap<String, String>();
            map.put("doc_id", String.valueOf(doc.getDocId()));
            map.put("user_id", doc.getUser().getId().toString());
            map.put("name", doc.getName());
            map.put("date_created", doc.getDocumentCreated().toString());
            map.put("s3_bucket_path", doc.getS3BucketPath());
            mapList.add(map);
        }
        return mapList;
    }
}
