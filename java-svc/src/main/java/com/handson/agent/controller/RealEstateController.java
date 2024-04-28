package com.handson.agent.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.handson.agent.model.RealEstate;
import com.handson.agent.model.RequestUrl;
import com.handson.agent.repo.RequestUrlRepositry;
import com.handson.agent.service.ScrapeService;
import com.handson.agent.service.PredictService;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import static com.handson.agent.model.RequestUrl.urlBuilder.aRequestUrl;

@Service
@RestController
@RequestMapping("/api")
public class RealEstateController {

        @Autowired
        ScrapeService scrapeService;

        @Autowired
        private RequestUrlRepositry requestUrlRepository;

        @Autowired
        private MongoTemplate mongoTemplate;

        @Autowired
        private PredictService predictService;

        // @CrossOrigin
        @CrossOrigin(origins = {"http://localhost:3000", "https://smart-agent.vercel.app/"})
        @RequestMapping(value = "/agent", method = RequestMethod.GET)
        public ResponseEntity<?> getHouses(@RequestParam(defaultValue = "forsale") String type,
                        @RequestParam String city,
                        @RequestParam(defaultValue = "0") Integer minPrice,
                        @RequestParam(defaultValue = "100000000") Integer maxPrice,
                        @RequestParam(required = false) Integer rooms)
                        throws IOException {

                String url = "https://gw.yad2.co.il/feed-search-legacy/realestate/" + type + "?city=" + city + "&"
                                + "price=" + minPrice + "-" + maxPrice;
                if (rooms instanceof Integer)
                        url += "&rooms=" + rooms + "-" + rooms;
                boolean exist = requestUrlRepository.existsById(url);
                if (exist) {
                        new Thread(() -> {
                                try {
                                        String url2 = "https://gw.yad2.co.il/feed-search-legacy/realestate/" + type
                                                        + "?city=" + city + "&"
                                                        + "price=" + minPrice + "-" + maxPrice;
                                        if (rooms instanceof Integer)
                                                url2 += "&rooms=" + rooms + "-" + rooms;
                                        JSONObject data = scrapeService.getHouseData(url2, type, city);
                                        Iterator<String> keys = data.keys();
                                        Set<String> keys2 = data.keySet();
                                        JSONObject predictedPrice = predictService.predictPrice(keys2);
                                        Query query = Query.query(Criteria.where("_id").is(url2)); // Iterate through
                                                                                                   // the keys
                                        while (keys.hasNext()) {
                                                String key = keys.next();
                                                Query query2 = Query
                                                                .query(Criteria.where("realEstates.houseUrl").is(key));
                                                if (!mongoTemplate.exists(query2, "request_url")) {
                                                        JSONObject realEstateObj = (JSONObject) data.get(key);
                                                        RealEstate realEstate = new RealEstate(realEstateObj,
                                                                        predictedPrice.getJSONObject(key));
                                                        Update update = new Update().addToSet("realEstates",
                                                                        realEstate);
                                                        mongoTemplate.updateFirst(query, update, "request_url");
                                                }
                                        }
                                } catch (Exception e) {
                                }
                        }).start();
                } else {
                        RequestUrl newUrl = aRequestUrl().withId(url).build();
                        requestUrlRepository.insert(newUrl);
                        JSONObject data = scrapeService.getHouseData(url, type, city);
                        Iterator<String> keys = data.keys();
                        Set<String> keys2 = data.keySet();
                        JSONObject predictedPrice = predictService.predictPrice(keys2);
                        Query query = Query.query(Criteria.where("_id").is(url)); // Iterate through the keys
                        while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject realEstateObj = (JSONObject) data.get(key);
                                RealEstate realEstate = new RealEstate(realEstateObj,
                                                predictedPrice.getJSONObject(key));
                                Update update = new Update().addToSet("realEstates", realEstate);
                                mongoTemplate.updateFirst(query, update, "request_url");
                        }
                }
                RequestUrl realEstates = requestUrlRepository.findById(url).get();
                return new ResponseEntity<>(realEstates.getRealEstates(), HttpStatus.OK);
        }

        // scrape html rather than json - didnt used
        @CrossOrigin
        @RequestMapping(value = "/agent2", method = RequestMethod.GET)
        public ResponseEntity<?> agent(@RequestParam(defaultValue = "forsale") String type,
                        @RequestParam String city,
                        @RequestParam(defaultValue = "0") Integer minPrice,
                        @RequestParam(defaultValue = "100000000") Integer maxPrice,
                        @RequestParam(required = false) Integer rooms) throws IOException {

                String url = "";
                if (rooms instanceof Integer)
                        url = "https://www.yad2.co.il/realestate/" + type + "?city=" + city + "&" +
                                        "rooms=" + rooms + "-" + rooms
                                        + "&" + "price=" + minPrice + "-" + maxPrice;

                else
                        url = "https://www.yad2.co.il/realestate/" + type + "?city=" + city
                                        + "&" + "price=" + minPrice + "-" + maxPrice;

                // check if the data exists
                boolean exist = requestUrlRepository.existsById(url);

                // if data not exists, scrape and return data:
                if (!exist) {
                        JSONObject data = scrapeService.scrapeYad2(url);
                        // save to db:
                        RequestUrl newUrl = aRequestUrl().withId(url).build();
                        requestUrlRepository.insert(newUrl);
                        Query query = Query.query(Criteria.where("_id").is(url));

                        Set<String> keys = data.keySet();
                        JSONObject predictedPrice = predictService.predictPrice(keys);
                        for (String key : keys) {
                                JSONObject value = (JSONObject) data.get(key);
                                RealEstate realEstate = new RealEstate(type, city, value,
                                                predictedPrice.getJSONObject(key));
                                Update update = new Update().addToSet("realEstates", realEstate);
                                mongoTemplate.updateFirst(query, update, "request_url");

                        }
                } else {
                        // if data exists, return data and update new real estates
                        new Thread(() -> {
                                try {
                                        String url1 = "";
                                        if (rooms instanceof Integer)
                                                url1 = "https://www.yad2.co.il/realestate/" + type + "?city=" + city
                                                                + "&" +
                                                                "rooms=" + rooms + "-" + rooms
                                                                + "&" + "price=" + minPrice + "-" + maxPrice;

                                        else
                                                url1 = "https://www.yad2.co.il/realestate/" + type + "?city=" + city
                                                                + "&" + "price=" + minPrice + "-" + maxPrice;
                                        JSONObject data = scrapeService.scrapeYad2(url1);
                                        Query query = Query.query(Criteria.where("_id").is(url1));

                                        Set<String> keys = data.keySet();
                                        JSONObject predictedPrice = predictService.predictPrice(keys);
                                        for (String key : keys) {
                                                JSONObject value = (JSONObject) data.get(key);
                                                // make sure real estate doesn't already exist
                                                Query query2 = Query.query(Criteria.where("realEstates.houseUrl")
                                                                .is(value.getString("houseUrl")));
                                                if (!mongoTemplate.exists(query2, "request_url")) {
                                                        RealEstate realEstate = new RealEstate(type, city, value,
                                                                        predictedPrice.getJSONObject(key));
                                                        Update update = new Update().addToSet("realEstates",
                                                                        realEstate);
                                                        mongoTemplate.updateFirst(query, update, "request_url");
                                                }
                                        }

                                } catch (Exception e) {
                                        e.printStackTrace();
                                }
                        }).start();
                }
                RequestUrl realEstates = requestUrlRepository.findById(url).get();
                return new ResponseEntity<>(realEstates.getRealEstates(), HttpStatus.OK);
        }
}

// @CrossOrigin
// @RequestMapping(value = "/realEstate", method = RequestMethod.GET)
// public ResponseEntity<?> getHouses(@RequestParam(defaultValue = "forsale")
// String type,
// @RequestParam String city,
// @RequestParam(defaultValue = "0") Integer minPrice,
// @RequestParam(defaultValue = "100000000") Integer maxPrice,
// @RequestParam(required = false) Integer rooms)
// throws IOException {
// return new ResponseEntity<>(realEstateService.searchHouses(type, city,
// minPrice, maxPrice, rooms),
// HttpStatus.OK);
// }

// @CrossOrigin
// @RequestMapping(value = "/smartAgent", method = RequestMethod.GET)
// public ResponseEntity<?> startAgent(@RequestParam(defaultValue = "forsale")
// String type,
// @RequestParam String city,
// @RequestParam(defaultValue = "0") Integer minPrice,
// @RequestParam(defaultValue = "100000000") Integer maxPrice,
// @RequestParam(required = false) Integer rooms) throws IOException {

// String url = "";
// if (rooms instanceof Integer)
// url = "https://www.yad2.co.il/realestate/" + type + "?city=" + city + "&" +
// "rooms=" + rooms + "-" + rooms
// + "&" + "price=" + minPrice + "-" + maxPrice;

// else
// url = "https://www.yad2.co.il/realestate/" + type + "?city=" + city
// + "&" + "price=" + minPrice + "-" + maxPrice;

// // check if the data exists
// boolean exist = requestUrlRepository.existsById(url);

// // if not exists:
// if (!exist) {
// RequestUrl newUrl = aRequestUrl().withId(url).build();
// requestUrlRepository.insert(newUrl);

// // step 1: scrape data
// JSONArray data = scrapeService.scrapeYad2(url);

// // step 2: save the data to the db

// Query query = Query.query(Criteria.where("_id").is(url));
// for (int i = 0; i < data.length(); i++) {
// // make sure real estate doesn't already exist
// Query query2 = Query.query(Criteria.where("realEstates.houseUrl")
// .is(data.getJSONObject(i).getString("houseUrl")));
// if (mongoTemplate.exists(query2, "request_url")) {
// // System.out.println("Exists!");
// }
// // create new real estate object and add it to the db!
// else {
// RealEstate realEstate = new RealEstate(type, city, data.getJSONObject(i));
// Update update = new Update().addToSet("realEstates", realEstate);
// mongoTemplate.updateFirst(query, update, "request_url");
// }
// }
// return new ResponseEntity<>(data.toString(), HttpStatus.OK);
// }

// // if exists:
// RequestUrl realEstates = requestUrlRepository.findById(url).get();
// return new ResponseEntity<>(realEstates.getRealEstates(), HttpStatus.OK);

// }

// @CrossOrigin
// @RequestMapping(value = "/activateAgentThread", method = RequestMethod.POST)
// public ResponseEntity<?> activateAgentThread(@RequestParam(defaultValue =
// "forsale") String type,
// @RequestParam String city,
// @RequestParam(defaultValue = "0") Integer minPrice,
// @RequestParam(defaultValue = "100000000") Integer maxPrice,
// @RequestParam(required = false) Integer rooms) {

// new Thread(() -> {
// while (true) {
// try {
// Thread.sleep(10000);
// String url = "";
// if (rooms instanceof Integer)
// url = "https://www.yad2.co.il/realestate/" + type + "?city=" + city
// + "&" +
// "rooms=" + rooms + "-" + rooms
// + "&" + "price=" + minPrice + "-" + maxPrice;

// else
// url = "https://www.yad2.co.il/realestate/" + type + "?city=" + city
// + "&" + "price=" + minPrice + "-" + maxPrice;

// JSONArray data = scrapeService.scrapeYad2(url);
// Query query = Query.query(Criteria.where("_id").is(url));
// for (int i = 0; i < data.length(); i++) {
// // make sure real estate doesn't already exist
// Query query2 = Query.query(Criteria.where("realEstates.houseUrl")
// .is(data.getJSONObject(i).getString("houseUrl")));
// if (mongoTemplate.exists(query2, "request_url")) {
// // System.out.println("Exists!");
// }
// // create new real estate object and add it to the db!
// else {
// RealEstate realEstate = new RealEstate(type, city,
// data.getJSONObject(i));
// Update update = new Update().addToSet("realEstates",
// realEstate);
// mongoTemplate.updateFirst(query, update, "request_url");
// }
// }
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
// }).start();
// return new ResponseEntity<>("Agent started", HttpStatus.OK);
// }

// @RequestMapping(value = "/user/{name}", method = RequestMethod.GET)
// public List<RealEstate> getRealEstate(@RequestParam String name) {
// return realEstateRepository.findAll();
// }

// @RequestMapping(value = "/request", method = RequestMethod.GET)
// public Optional<RequestUrl> getRequestUrl(@RequestParam String url) {
// return requestUrlRepository.findById(url);
// }

// @RequestMapping(value = "/addRequestUrl", method = RequestMethod.POST)
// public RequestUrl createRequestUrl(@RequestParam String url) {
// RequestUrl newUrl = aRequestUrl().withId(url).build();
// return requestUrlRepository.insert(newUrl);
// }

// @RequestMapping(value = "/addRealEstate", method = RequestMethod.POST)
// public ResponseEntity<?> createRealEstate(@RequestParam String url) {
// Optional<RequestUrl> exist = requestUrlRepository.findById(url);
// System.out.println(exist);
// if (exist.isEmpty()) {
// RequestUrl newUrl = aRequestUrl().withId(url).build();
// requestUrlRepository.insert(newUrl);
// }

// RealEstate newRealEstate = new RealEstate("forsale", "neve shanan", "haifa",
// "pic.com", 100000, 3, 100,
// 1,
// "houseurl.com");

// Query query = Query.query(Criteria.where("_id").is(url));
// Update update = new Update().addToSet("realEstates", newRealEstate);
// mongoTemplate.updateFirst(query, update, "request_url");
// return new ResponseEntity<>(HttpStatus.OK);
// }

// @RequestMapping(value = "/addRealEstate", method = RequestMethod.POST)
// public RealEstate createRealEstate(@RequestParam String name) {
// RealEstate realEstate = new RealEstate("forsale", "neve shanan", "haifa",
// "pic.com", 100000, 3, 100, 1,
// "houseurl.com");
// return realEstateRepository.insert(realEstate);
// }

// @CrossOrigin
// @RequestMapping(value = "/smartAgent", method = RequestMethod.GET)
// public ResponseEntity<?> getHouses2(@RequestParam(defaultValue = "forsale")
// String type, @RequestParam String city,
// @RequestParam(defaultValue = "0") Integer minPrice,
// @RequestParam(defaultValue = "100000000") Integer maxPrice,
// @RequestParam(required = false) Integer rooms)
// throws IOException {
// return new ResponseEntity<>(smartAgentService.searchHouses2(type, city,
// minPrice, maxPrice, rooms),
// HttpStatus.OK);
// }

// @CrossOrigin
// @RequestMapping(value = "/{id}", method = RequestMethod.GET)
// public ResponseEntity<?> getOneStudent(@PathVariable Long id) {
// return new ResponseEntity<>(studentService.findById(id), HttpStatus.OK);