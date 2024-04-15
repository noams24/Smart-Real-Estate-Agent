package com.handson.agent.service;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
// import org.json.JSONArray;
import org.json.JSONObject;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class RealEstateService {

    public static final Pattern PRODUCT_PATTERN = Pattern.compile(
            "neighborhood\":\\{\"text\":\"([^\"]+)\"},\"street\":\\{\"text\":\"([^\"]+)\"[^0-9]+[^:]+:([0-9]).*?(?=price)price\":([0-9]+),\"token\":\"([^\"]+).*?(?=property)property\":\\{\"text\":\"([א-ת]+)[^:]+:([0-9])[^:]+:([0-9]+).*?(?=Image)Image\":\"([^\"]+)");
    OkHttpClient client = new OkHttpClient().newBuilder().build();

    // @Autowired
    // ObjectMapper _om;

    public String searchHouses(String type, String city, Integer minPrice, Integer maxPrice, Integer rooms)
            throws IOException {

        return parseProductHtml(getHouseHtml(type, city, minPrice, maxPrice, rooms));
    }

    private String parseProductHtml(String html) {
        Matcher matcher = PRODUCT_PATTERN.matcher(html);
        JSONArray houses = new JSONArray();
        for (int i = 0; i < 10; i++) {
            if (matcher.find()) {

                JSONObject house = new JSONObject();
                house.put("neighborhood", matcher.group(1));
                house.put("street", matcher.group(2));
                house.put("floor", matcher.group(3));
                house.put("price", matcher.group(4));
                house.put("houseUrl", matcher.group(5));
                house.put("type", matcher.group(6));
                house.put("rooms", matcher.group(7));
                house.put("area", matcher.group(8));
                house.put("imgUrl", matcher.group(9));
                houses.put(house);
            } else
                break;
        }
        return houses.toString();
    }

    public String getHouseHtml(String type, String city, Integer minPrice, Integer maxPrice, Integer rooms)
            throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        
        String url = "";
        if (rooms instanceof Integer)
        url = "https://www.yad2.co.il/realestate/" + type + "?city=" + city + "&" +
        "rooms=" + rooms + "-" + rooms
        + "&" + "price=" + minPrice + "-" + maxPrice;

        else
        url = "https://www.yad2.co.il/realestate/" + type + "?city=" + city
        + "&" + "price=" + minPrice + "-" + maxPrice;

        System.out.println(url);

        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .addHeader("Accept-Language", "en-US,en;q=0.9,he;q=0.8")
                .addHeader("Cache-Control", "max-age=0")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cookie",
                        "__uzma=e3f122fb-6916-423f-b546-5719de55510c; __uzmb=1713095547; __uzme=9311; canary=never; __ssds=3; y2018-2-cohort=65; cohortGroup=D; __ssuzjsr3=a9be0cd8e; __uzmaj3=06aef554-5bd1-4c1f-8973-8705bd787281; __uzmbj3=1713095548; __uzmlj3=pmDOwewlAi5qdJb72E7RoZBRcy/PWf62V582GS/Krgs=; _gcl_au=1.1.2081713043.1713095551; __rtbh.lid=%7B%22eventType%22%3A%22lid%22%2C%22id%22%3A%22albJaquLyTAfpxmtmcc6%22%7D; _gid=GA1.3.807215980.1713095551; guest_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjp7InV1aWQiOiI5NjgwYTBkYS04NmIwLTQ0NzItOGNiZC02YTRkNzAwYzZmN2YifSwiaWF0IjoxNzEzMDk1NTQ5LCJleHAiOjE3NDYzNDQ2NDQ2NTN9.lxN8tnSx03zVhG7ocad-wmK0myEQ_5gsyGdP39FlWA4; _fbp=fb.2.1713095551626.607811564; bc.visitor_token=7185243520940462080; _hjSessionUser_266550=eyJpZCI6Ijg2NTk0YjUyLTI5ODktNWY5OS05MWEzLTdiNmQxOTNjNDExOSIsImNyZWF0ZWQiOjE3MTMwOTU1NTE1MzcsImV4aXN0aW5nIjp0cnVlfQ==; _ga=GA1.1.2050923833.1713095551; _hjSession_266550=eyJpZCI6IjQyODdlMjczLWI4OGEtNDhmNS04NTdhLThhN2JhNmYwNjgzZCIsImMiOjE3MTMxNzk1ODU5MDYsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MH0=; __rtbh.uid=%7B%22eventType%22%3A%22uid%22%7D; __uzmcj3=891854371337; __uzmdj3=1713185420; __uzmfj3=7f6000dfd32e8c-2178-4149-a600-1eacec73d142171309554868289871807-edab8ad7e4d54a8843; __gads=ID=4d19524338000a2b:T=1713095548:RT=1713185420:S=ALNI_MYwEP-_IA5hhE5mhDiMLMWx5yDWNQ; __gpi=UID=00000d5d2bd7ded4:T=1713095548:RT=1713185420:S=ALNI_MatlsKcKcDoEdBQDTay9HFOQI1kfQ; __eoi=ID=baa195149acc833c:T=1713095548:RT=1713185421:S=AA-Afjb7xizWPdkcdMRDMJ0sYsoE; _ga_GQ385NHRG1=GS1.1.1713184379.4.1.1713185687.60.0.0; __uzmd=1713185994; __uzmf=7f6000dfd32e8c-2178-4149-a600-1eacec73d142171309554751090446551-8fabe6283ce5e1eb100; uzmx=7f90009e14d5ca-c54d-4d74-9d83-32cf6f7763432-171309554751090446551-fc69873051a01d5e205; __uzmc=9448110069226; uzmx=7f90001624387b-805c-4e23-b7a9-5737ab809bf05-1712849284514336833328-3c73621350ac99ac118; __uzma=1341c08b-6bc1-4ec1-af90-36225ab0fde2; __uzmb=1713019041; __uzmc=4206910332996; __uzmd=1713186117; __uzme=8283; __uzmf=7f6000dfd32e8c-2178-4149-a600-1eacec73d142171309554751090570332-6814910826af762c103")
                .addHeader("Referer", "https://www.yad2.co.il/")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static class RealEstateResponse {
        List<RealEstateObject> data;

        public List<RealEstateObject> getData() {
            return data;
        }
    }

    static class RealEstateObject {
        Integer price;

        public Integer getPrice() {
            return price;
        }
    }

}

// Request request = new Request.Builder()
// .url("https://www.yad2.co.il/realestate/_next/data/brkZ6ISVI0DS6Gz3cZCE6/forsale.json?city=4000")
// .method("GET", null)
// .addHeader("Accept", "*/*")
// .addHeader("Accept-Language", "en-US,en;q=0.9")
// .addHeader("Connection", "keep-alive")
// .addHeader("Cookie",
// "__uzma=8622c8ce-0079-465b-b7d5-79f22db79968; __uzmb=1712849284; __uzme=0671;
// abTestKey=33; canary=never;
// _vwo_uuid_v2=DA574880C0011244C664DBA285F9A8788|2c207fdb48ad9491e37d512c7111401b;
// _gid=GA1.3.965663550.1712849286; _gcl_au=1.1.571837519.1712849288;
// __rtbh.lid=%7B%22eventType%22%3A%22lid%22%2C%22id%22%3A%22YCrp4FPrfrfpeXXMNuw8%22%7D;
// __ssds=3; __ssuzjsr3=a9be0cd8e;
// __uzmaj3=cfabb6db-954d-4916-ba90-84d9cc9f42d7; __uzmbj3=1712849288;
// __uzmlj3=5ag6lZiUWGyokHGQ6ajLXbJ50zVLYAU3nTjxLD7CHZ0=; server_env=production;
// y2018-2-cohort=36; leadSaleRentFree=53; y2_cohort_2020=66;
// use_elastic_search=1;
// guest_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjp7InV1aWQiOiIzZDc1YjBkZS0zNTQ5LTQzMTUtOWI3My00NWUxZTc4MzE4MWUifSwiaWF0IjoxNzEyODQ5MzU2LCJleHAiOjE3NDYwOTgyMDU0NDN9.KZJuoY9ADDJBZdqkHH5YmT3zdbWeUNTLtJRyRFQpX84;
// _hjSession_266550=eyJpZCI6IjQ3MjczNGNkLTE1MjUtNGU3Mi1hYjg5LWNiMGUyYWMzOGJiOCIsImMiOjE3MTI4NDkzNTcwNDIsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjoxLCJzcCI6MX0=;
// _fbp=fb.2.1712849357096.1176162590; bc.visitor_token=7184210912156667904;
// _hjSessionUser_266550=eyJpZCI6ImZlMzlhYjliLTIwYjEtNTk1OS1hZWQwLWYzYWM2N2IzZjQ1YiIsImNyZWF0ZWQiOjE3MTI4NDkzNTcwNDEsImV4aXN0aW5nIjp0cnVlfQ==;
// recommendations-home-category={\"categoryId\":2,\"subCategoryId\":1};
// _ga=GA1.1.1813773480.1712849286; cohortGroup=C;
// favorites_userid=jei1052988282;
// __gads=ID=05ad888bad811e54:T=1712849354:RT=1712851070:S=ALNI_MYm3pT30wq8b8Mdd6jquRxXroxbTA;
// __gpi=UID=00000deb1b6dfdd6:T=1712849354:RT=1712851070:S=ALNI_MZfM4iJcXB3CnUEymhmLxMwL4x6QQ;
// __eoi=ID=c746e60f03257f26:T=1712849354:RT=1712851070:S=AA-AfjZMAx7gNFkd4xFjCr4XkDm_;
// dicbo_id=%7B%22dicbo_fetch%22%3A1712851074864%7D; __uzmcj3=303367315762;
// __uzmdj3=1712851197;
// __uzmfj3=7f6000621b1a18-3ddf-4153-b093-07ec6f2d5a9817128492887341908909-eb2ff35df21b163b73;
// _ga_GQ385NHRG1=GS1.1.1712849355.1.1.1712851200.40.0.0; __uzmd=1712851256;
// __uzmc=8852116015590;
// __uzmf=7f6000621b1a18-3ddf-4153-b093-07ec6f2d5a9817128492845141972008-5cd71407e2417a84160;
// uzmx=7f90001624387b-805c-4e23-b7a9-5737ab809bf01-17128492845141972008-e4c4dd1cc14b6bc9475;
// uzmx=7f90001624387b-805c-4e23-b7a9-5737ab809bf01-17128492845142178626-087f134b0de9e48f64;
// __uzmc=2218516373153; __uzmd=1712851463;
// __uzmf=7f6000621b1a18-3ddf-4153-b093-07ec6f2d5a9817128492845142178626-bba773cb05677bdb163")
// .addHeader("Referer", "https://www.yad2.co.il/realestate/forsale?city=4000")
// .addHeader("Sec-Fetch-Dest", "empty")
// .addHeader("Sec-Fetch-Mode", "cors")
// .addHeader("Sec-Fetch-Site", "same-origin")
// .addHeader("User-Agent",
// "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like
// Gecko) Chrome/123.0.0.0 Safari/537.36")
// .addHeader("baggage",
// "sentry-environment=prod,sentry-release=realestate-b74f9b75471818b28c88d5a8f5f272f9aa1bce13,sentry-public_key=fd7d23a8d868485399868caf5fd39b0d,sentry-trace_id=d76a8f1389ea45d88deff5cb73ed63cc,sentry-sample_rate=1")
// .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"123\",
// \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"")
// .addHeader("sec-ch-ua-mobile", "?0")
// .addHeader("sec-ch-ua-platform", "\"Windows\"")
// .addHeader("sentry-trace",
// "d76a8f1389ea45d88deff5cb73ed63cc-97c325500400edc6-1")
// .addHeader("uzlc", "true")
// .addHeader("x-nextjs-data", "1")
// .build();

// Request request = new
// Request.Builder().url("https://www.yad2.co.il/realestate/_next/data/brkZ6ISVI0DS6Gz3cZCE6/forsale.json?city=4000").method("GET",
// null).build();
// Response response = client.newCall(request).execute();
// RealEstateResponse res = _om.readValue(response.body().string(),
// RealEstateResponse.class);

// System.out.println(res.getData().get(0));
// System.out.println(response);
// System.out.println(response.body().string());
// JSONObject jsonObject =
// JsonParser.parseString(response.body()).getAsJsonObject();
// JSONObject jsonObject = new JSONObject(response.body().string());
// JSONObject pageProps = (JSONObject)
// jsonObject.getJSONObject("pageProps").getJSONObject("dehydratedState");
// JSONArray queries = pageProps.getJSONArray("queries");
// System.out.println(queries.get(1));
// System.out.println();
// System.out.println(queries.get(2));
// System.out.println(pageProps.get("dehydratedState"));
// System.out.println(jsonObject.get("pageProps"));
// System.out.println(jsonObject.getJSONObject("pageProps").getJSONObject("dehydratedState").getJSONObject("queries"));

// System.out.println(response.body().string());

// JSONObject data = (JSONObject)
// jsonObject.getJSONObject("pageProps").getJSONObject("dehydratedState").getJSONArray("queries").get(1);
// System.out.println(data);
// JSONArray results =
// data.getJSONObject("state").getJSONObject("data").getJSONObject("feed").getJSONArray("private");
// System.out.println(data.getJSONObject("state").getJSONObject("data").getJSONObject("feed"));
// System.out.println(data.getJSONObject("state").getJSONObject("data").getJSONObject("feed").getJSONArray("private").get(0));
// System.out.println(results);
// System.out.println(results.get(0));
// return results.get(0).toString();