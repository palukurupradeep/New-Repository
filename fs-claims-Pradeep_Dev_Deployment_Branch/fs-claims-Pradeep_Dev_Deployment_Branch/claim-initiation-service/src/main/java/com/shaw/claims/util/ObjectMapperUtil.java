package com.shaw.claims.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.beans.FeatureDescriptor;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ObjectMapperUtil {
    public <T>List<T> map(List<T> responseObject, Class<T> responseType){
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < responseObject.size(); i++) {
            T object = responseObject.get(i);
            T mappedObject = mapper.convertValue(object, responseType);
            responseObject.set(i, mappedObject);
        }
        return responseObject;
    }
    
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                		.toArray(String[]::new);
    }
}
