package com.handson.agent.model;

import java.util.List;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "request_url")
public class RequestUrl {

    @Id
    private String id;

    private List<RealEstate> realEstates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RealEstate> getRealEstates() {
        return realEstates;
    }

    public void setRealEstates(List<RealEstate> realEstates) {
        this.realEstates = realEstates;
    }

    public static final class urlBuilder {
        private String id;
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

        public urlBuilder withRealEstates(List<RealEstate> realEstates) {
            this.realEstates = realEstates;
            return this;
        }

        public RequestUrl build() {
            RequestUrl requestUrl = new RequestUrl();
            requestUrl.setId(this.id);
            requestUrl.setRealEstates(this.realEstates);
            return requestUrl;
        }

    }

}
