package com.handson.agent.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.handson.agent.service.RealEstateService;

@Service
@RestController
@RequestMapping("/api")
public class RealEstateController {

    @Autowired
    RealEstateService realEstateService;

    @CrossOrigin
    @RequestMapping(value = "/realEstate", method = RequestMethod.GET)
    public ResponseEntity<?> getHouses(@RequestParam(defaultValue = "forsale") String type, @RequestParam String city,
            @RequestParam(defaultValue = "0") Integer minPrice,
            @RequestParam(defaultValue = "100000000") Integer maxPrice, @RequestParam(required = false) Integer rooms) throws IOException {
        return new ResponseEntity<>(realEstateService.searchHouses(type, city, minPrice, maxPrice, rooms),
                HttpStatus.OK);
    }
}