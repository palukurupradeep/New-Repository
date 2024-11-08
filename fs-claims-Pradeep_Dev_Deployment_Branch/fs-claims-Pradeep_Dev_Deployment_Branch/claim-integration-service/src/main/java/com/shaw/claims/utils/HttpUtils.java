package com.shaw.claims.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Base64;

public class HttpUtils {

    public static HttpHeaders getHeaders ()
    {
        String adminuserCredentials = "svcTDVCMS_NP:UALB1ro7kWK4RwVLx1JDH4I%y6Affdav";
        String encodedCredentials = new String(Base64.getEncoder().encode(adminuserCredentials.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + encodedCredentials);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

}
