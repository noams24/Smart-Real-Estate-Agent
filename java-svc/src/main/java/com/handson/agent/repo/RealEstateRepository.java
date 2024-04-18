package com.handson.agent.repo;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.handson.agent.model.RealEstate;

public interface RealEstateRepository extends MongoRepository<RealEstate, String> {
    
}

