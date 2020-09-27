package com.adev.gather.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component("collectionNameProvider")
public class CollectionNameProvider {
    public static final String DEFAULT_COLLECTION_NAME = "default";
    private static ThreadLocal<String> collectionNameThreadLocal = new ThreadLocal<>();

    public static void setCollectionName(String collectionName) {
        collectionNameThreadLocal.set(collectionName);
    }

    public String getCollectionName() {
        String collectionName = collectionNameThreadLocal.get();
        if (StringUtils.isNotBlank(collectionName)) {
            return collectionName;
        } else {
            return DEFAULT_COLLECTION_NAME;
        }
    }
}
