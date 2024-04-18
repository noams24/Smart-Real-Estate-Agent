package com.handson.agent.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.handson.agent.model.RequestUrl;

public interface RequestUrlRepositry extends MongoRepository<RequestUrl, String> {
    // String findFirstByRequestUrl(String requestUrl);
    // RequestUrl findFirstByName(String url);
}
