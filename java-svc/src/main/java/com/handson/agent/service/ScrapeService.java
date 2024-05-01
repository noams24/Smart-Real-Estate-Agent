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
                                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                                .addHeader("Accept-Language", "en-US,en;q=0.9,he;q=0.8")
                                .addHeader("Cache-Control", "max-age=0")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Cookie", "__ssds=3; y2018-2-cohort=65; __uzmb=1713095548; __uzma=a4064816-43aa-483e-8487-d3f343c599e4; __uzme=9015; __uzmaj3=06aef554-5bd1-4c1f-8973-8705bd787281; __uzmbj3=1713095548; __uzmlj3=pmDOwewlAi5qdJb72E7RoZBRcy/PWf62V582GS/Krgs=; _gcl_au=1.1.2081713043.1713095551; guest_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXlsb2FkIjp7InV1aWQiOiI5NjgwYTBkYS04NmIwLTQ0NzItOGNiZC02YTRkNzAwYzZmN2YifSwiaWF0IjoxNzEzMDk1NTQ5LCJleHAiOjE3NDYzNDQ2NDQ2NTN9.lxN8tnSx03zVhG7ocad-wmK0myEQ_5gsyGdP39FlWA4; _fbp=fb.2.1713095551626.607811564; _hjSessionUser_266550=eyJpZCI6Ijg2NTk0YjUyLTI5ODktNWY5OS05MWEzLTdiNmQxOTNjNDExOSIsImNyZWF0ZWQiOjE3MTMwOTU1NTE1MzcsImV4aXN0aW5nIjp0cnVlfQ==; leadSaleRentFree=37; refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VyTmFtZSI6Im5vYW1zMjk5QGdtYWlsLmNvbSIsIk1haWxpbmdFbWFpbCI6Im5vYW1zMjk5QGdtYWlsLmNvbSIsIkVtYWlsIjoibm9hbXMyOTlAZ21haWwuY29tIiwiVXNlcklEIjo3MTAyNDQxLCJGaXJzdE5hbWUiOiLXoNeV16LXnSIsIkxhc3ROYW1lIjoi16nXk9eUIiwiaXNDYXJUcmFkZXIiOm51bGwsImlzQ2FyQWNjZXNzb3J5VHJhZGVyIjowLCJpc1RvdXJpc21UcmFkZXIiOjAsImlzUmVhbEVzdGF0ZU1hcmtldGluZyI6MCwiWWFkMlRyYWRlIjowLCJFbWFpbFZlcmlmaWVkIjoiMjAyNC0wNC0xNlQyMDo1MzozMy4wMDBaIiwiaXNUd29XaGVlbGVkVHJhZGVyIjpudWxsLCJUaXYiOjAsIk1haWxpbmdMaXN0IjowLCJpYXQiOjE3MTMyOTAwMTMsImV4cCI6MTcyMTA2NjAxM30.TnmgSUFwS4Fs-bPyoDZuwyEfsJPVn93HsJuLg8EbnJQ; _hjSessionUser_2884988=eyJpZCI6ImE1MDgyYTIyLTA1YmMtNWI5Mi1hM2NiLWUzMDc2YzYzZDBlMCIsImNyZWF0ZWQiOjE3MTMyODk5OTk0NDcsImV4aXN0aW5nIjp0cnVlfQ==; _hjSessionUser_1413567=eyJpZCI6Ijg1YTgzNjk1LWI4ZDQtNTJlYS1hY2E2LTk2OWNhNmVjZmVhOCIsImNyZWF0ZWQiOjE3MTMyODk5OTgzMjcsImV4aXN0aW5nIjp0cnVlfQ==; _vwo_uuid_v2=D02C7D8F9A3E20CB2F8A76CDFE7883AC2|2e5700289d380a47506b5b28d332d635; _hjSessionUser_2695076=eyJpZCI6IjNkNjYwZTg2LTRlNGQtNTZlNC04MTViLWRiYjgzNjA1OWFlMSIsImNyZWF0ZWQiOjE3MTMyOTAxODYwOTIsImV4aXN0aW5nIjp0cnVlfQ==; divurNadlan_53499722=%7B%22o%22%3A53499722%2C%22smwp%22%3A1%7D; abTestKey=9; use_elastic_search=1; cohortGroup=D; __ssuzjsr3=a9be0cd8e; canary=never; favorites_userid=fvu7102441; _gid=GA1.3.252328064.1714475969; __gads=ID=4d19524338000a2b:T=1713095548:RT=1714481613:S=ALNI_MYwEP-_IA5hhE5mhDiMLMWx5yDWNQ; __gpi=UID=00000d5d2bd7ded4:T=1713095548:RT=1714481613:S=ALNI_MatlsKcKcDoEdBQDTay9HFOQI1kfQ; __eoi=ID=baa195149acc833c:T=1713095548:RT=1714481613:S=AA-Afjb7xizWPdkcdMRDMJ0sYsoE; AMP_302b804cae=JTdCJTIyZGV2aWNlSWQlMjIlM0ElMjJhMTgwZjRhZS00NDdmLTRmYzQtYmNkMS1lYTQ4N2I0N2JlMjAlMjIlMkMlMjJzZXNzaW9uSWQlMjIlM0ExNzE0NDgxNjIwNjI4JTJDJTIyb3B0T3V0JTIyJTNBZmFsc2UlMkMlMjJsYXN0RXZlbnRUaW1lJTIyJTNBMTcxNDQ4MTYyMDYzNiUyQyUyMmxhc3RFdmVudElkJTIyJTNBMjYlMkMlMjJwYWdlQ291bnRlciUyMiUzQTAlN0Q=; y2session=391ca0b990a51708424330f42e2e6026; y2_cohort_2020=8; _hjSession_1413567=eyJpZCI6IjM3NWYwMWE1LTY4ZmItNGUzYy1iZWI4LTAxYzU4YjljMTNhMiIsImMiOjE3MTQ0ODE2MjYwMzgsInMiOjAsInIiOjAsInNiIjowLCJzciI6MCwic2UiOjAsImZzIjowLCJzcCI6MX0=; _ga_GQ385NHRG1=GS1.1.1714481616.33.1.1714481688.51.0.0; _ga=GA1.1.2050923833.1713095551; __uzmcj3=2830749371791; __uzmdj3=1714481688; __uzmfj3=7f6000dfd32e8c-2178-4149-a600-1eacec73d14217130955486821386139434-2023b08bca3800e3493; __uzmc=75086196681957; __uzmd=1714482438; __uzmf=7f6000252bb914-8a09-41a2-8ef2-eb7cf312038117130955485571386889972-8d1ee90708dbba071966; uzmx=7f90009e14d5ca-c54d-4d74-9d83-32cf6f77634312-17130955475101386891019-9505bda178a9263b2695; uzmx=7f90001624387b-805c-4e23-b7a9-5737ab809bf09-17128492845141633280052-4493fa43eb6581d9142; __uzma=f24ff31a-2a25-462e-98a4-d77bc7ed2f61; __uzmb=1713942178; __uzmc=69369196994838; __uzmd=1714482564; __uzme=8062; __uzmf=7f6000252bb914-8a09-41a2-8ef2-eb7cf312038117130955485571387016009-c7c5c778e9bbf6651969")
                                .addHeader("If-None-Match", "W/\"37d97-PjJjpnB2QMeUU/l2AGKU7GFfhm8\"")
                                .addHeader("Sec-Fetch-Dest", "document")
                                .addHeader("Sec-Fetch-Mode", "navigate")
                                .addHeader("Sec-Fetch-Site", "none")
                                .addHeader("Sec-Fetch-User", "?1")
                                .addHeader("Upgrade-Insecure-Requests", "1")
                                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                                .addHeader("sec-ch-ua", "\"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"")
                                .addHeader("sec-ch-ua-mobile", "?0")
                                .addHeader("sec-ch-ua-platform", "\"Windows\"")
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