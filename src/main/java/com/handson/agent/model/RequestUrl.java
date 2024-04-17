package com.handson.agent.model;

import java.util.List;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;

@Document(collection = "request_url")
public class RequestUrl {

    @Id
    private String id;

    // @Indexed(unique = true)
    // private String requestUrl;

    private List<RealEstate> realEstates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // public String getRequestUrl() {
    //     return requestUrl;
    // }

    // public void setRequestUrl(String requestUrl) {
    //     this.requestUrl = requestUrl;
    // }

    public List<RealEstate> getRealEstates() {
        return realEstates;
    }

    public void setRealEstates(List<RealEstate> realEstates) {
        this.realEstates = realEstates;
    }

    // public RequestUrl(String requestUrl) {
    //     this.requestUrl = requestUrl;
    //     this.realEstates = new ArrayList<RealEstate>();
    // }

    public static final class urlBuilder {
        private String id;
        // private String requestUrl;
        private List<RealEstate> realEstates;

        private urlBuilder() {
        }

        public static urlBuilder aRequestUrl() {
            return new urlBuilder();
        }

        public urlBuilder withId(String id) {
            this.id = id;
            return this;
        }

        // public urlBuilder withRequestUrl(String requestUrl) {
        //     this.requestUrl = requestUrl;
        //     return this;
        // }

        public urlBuilder withRealEstates(List<RealEstate> realEstates) {
            this.realEstates = realEstates;
            return this;
        }

        public RequestUrl build() {
            RequestUrl requestUrl = new RequestUrl();
            requestUrl.setId(this.id);
            // requestUrl.setRequestUrl(this.requestUrl);
            requestUrl.setRealEstates(this.realEstates);
            return requestUrl;
            // return new RequestUrl(requestUrl);
        }

    }

}
