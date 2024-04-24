package com.handson.agent.service;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class ScrapeService {

        public static final int TOTAL_REAL_ESTATES_TO_SCRAPE = 15;
        public static final Pattern PRODUCT_PATTERN = Pattern.compile(
                        "neighborhood\":\\{\"text\":\"([^\"]+)\"},\"street\":\\{\"text\":\"([^\"]+)\"[^0-9]+[^:]+:([0-9]).*?(?=price)price\":([0-9]+),\"token\":\"([^\"]+).*?(?=property)property\":\\{\"text\":\"([א-ת]+)[^:]+:([0-9])[^:]+:([0-9]+).*?(?=Image)Image\":\"([^\"]+)");
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        public JSONObject scrapeYad2(String url)
                        throws IOException {

                return parseProductHtml(getHouseHtml(url));
        }

        private JSONObject parseProductHtml(String html) {
                Matcher matcher = PRODUCT_PATTERN.matcher(html);
                JSONObject houses = new JSONObject();
                for (int i = 0; i < TOTAL_REAL_ESTATES_TO_SCRAPE; i++) {
                        if (matcher.find()) {
                                if (!houses.has(matcher.group(5))) {
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
                                }
                        } else
                                break;
                }
                return houses;
        }

        public String getHouseHtml(String url)
                        throws IOException {

                OkHttpClient client = new OkHttpClient().newBuilder().build();

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
                                .addHeader("sec-ch-ua",
                                                "\"Google Chrome\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"")
                                .addHeader("sec-ch-ua-mobile", "?0")
                                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                                .build();
                Response response = client.newCall(request).execute();
                return response.body().string();
        }

        public JSONObject getHouseData(String url, String type, String city)
                        throws IOException {
                OkHttpClient client = new OkHttpClient().newBuilder().build();

                Request request = new Request.Builder()
                                .url(url)
                                .method("GET", null)
                                .build();
                Response response = client.newCall(request).execute();
                JSONObject rawData = new JSONObject(response.body().string());
                JSONArray realEstates = rawData.getJSONObject("data").getJSONObject("feed").getJSONArray("feed_items");
                JSONObject result = new JSONObject();
                for (int i = 0; i < 15; i++) {
                        try {
                                JSONObject singleResult = new JSONObject();
                                JSONObject realEstate = realEstates.getJSONObject(i);
                                singleResult.put("city", city);
                                // singleResult.put("city", realEstate.getString("city"));
                                try {
                                        singleResult.put("neighborhood", realEstate.getString("neighborhood"));
                                } catch (Exception e) {
                                        singleResult.put("neighborhood", "");
                                }
                                try {
                                        singleResult.put("street", realEstate.getString("street"));
                                } catch (Exception e) {
                                        singleResult.put("street", "");
                                }
                                try {
                                        singleResult.put("floor", String.valueOf(realEstate.getJSONArray("row_4")
                                                        .getJSONObject(1).getNumber("value")));
                                } catch (Exception e) {
                                        singleResult.put("floor",
                                                        realEstate.getJSONArray("row_4").getJSONObject(1)
                                                                        .getString("value"));
                                }
                                singleResult.put("price", realEstate.getString("price"));
                                singleResult.put("houseUrl", realEstate.getString("link_token"));
                                singleResult.put("type", type);
                                singleResult.put("rooms",
                                                realEstate.getJSONArray("row_4").getJSONObject(0).getNumber("value"));
                                singleResult.put("area",
                                                realEstate.getJSONArray("row_4").getJSONObject(2).getNumber("value"));

                                singleResult.put("imgUrl", realEstate.getString("img_url"));
                                try {
                                        singleResult.put("coordinates", realEstate.getJSONObject("coordinates"));
                                } catch (Exception e) {
                                        singleResult.put("coordinates", "");
                                }
                                result.put(singleResult.getString("houseUrl"), singleResult);
                        } catch (Exception e) {
                        }
                }
                return result;
        }
}