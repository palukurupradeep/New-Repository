package com.shaw.claims.client;

import com.shaw.claims.model.ProductDetails;
import com.shaw.claims.source.response.CustomerDetailsRoot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class ProductDetailsClient {

    Logger log = LogManager.getLogger(CustomerDetailsClient.class);

    @Value("${rest.producturl}")
    private String restUrl;

    @Value("${rest.productuuid}")
    private String productUUID;
    @Autowired
    private RestTemplateClient restTemplateClient;

    public List getProductDetailsByStyleNumber(String styleNumber) {
        String formattedUrl = "";
        formattedUrl += String.format("/api/v1/Specifications/%s?uid=%s", styleNumber, productUUID);
        log.info("formattedUrl :: " + formattedUrl);
        RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
        String url = restUrl + formattedUrl;
        log.info("Final Url :: " + url);
        return restTemplate.getForObject(url, List.class);
    }

}
