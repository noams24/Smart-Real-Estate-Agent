package com.handson.agent.service;

import java.io.IOException;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Service
public class PredictService {

    public JSONObject predictPrice(Set<String> keys)
            throws IOException {
        
        String[] keys2 = keys.toArray(new String[keys.size()]);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String[]> requestEntity = new HttpEntity<>(keys2, headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "https://smart-agent-fastapi2.runmydocker-app.com/predict-house-all/",
            // "http://fastapi:8000/predict-house-all/",
                // "http://127.0.0.1:8000/predict-house-all/",
                HttpMethod.POST,
                requestEntity,
                String.class);
        JSONObject predictedPrice = new JSONObject(response.getBody());
        return predictedPrice;
    }

}