package com.sahil.webapp.util;

import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

//    @Value("${secret.key}")
//    private String secretKey;
//
//    @Value("${access.key}")
//    private String accessKey;
    @Value("${bucket.region")
    private String region;

    @Bean
    public AmazonS3 getclient(){
       // AWSCredentials credential = new BasicAWSCredentials(accessKey,secretKey);
        return AmazonS3ClientBuilder
                .standard()
                //.withCredentials(new AWSStaticCredentialsProvider(credential))
                .withCredentials(new InstanceProfileCredentialsProvider())
                .withRegion(Regions.fromName(region))
                        .build();
    }


}
