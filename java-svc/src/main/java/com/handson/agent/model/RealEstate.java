package com.handson.agent.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.handson.agent.utils.Dates;
import java.util.Date;
import org.joda.time.LocalDateTime;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "realEstate")
public class RealEstate {

    private String type;
    private String neighborhood;
    private String street;
    private String city;
    private String imgUrl;
    private String price;
    private Integer rooms;
    private Integer area;
    private String floor;
    private float latitude;
    private float longitude;
    private Integer predictedSalePrice;
    private Integer predictedRentPrice;
    private String predictedCapRate;

    public Integer getPredictedSalePrice() {
        return predictedSalePrice;
    }

    public void setPredictedSalePrice(Integer predictedSalePrice) {
        this.predictedSalePrice = predictedSalePrice;
    }

    public Integer getPredictedRentPrice() {
        return predictedRentPrice;
    }

    public void setPredictedRentPrice(Integer predictedRentPrice) {
        this.predictedRentPrice = predictedRentPrice;
    }

    public String getPredictedCapRate() {
        return predictedCapRate;
    }

    public void setPredictedCapRate(String predictedCapRate) {
        this.predictedCapRate = predictedCapRate;
    }

    // @Indexed(unique = true)
    private String houseUrl;

    public RealEstate() {
    }

    public RealEstate(String type, String neighborhood, String street, String city, String imgUrl, String price,
            Integer rooms, Integer area, String floor, String houseUrl, Integer predictedSalePrice,
            Integer predictedRentPrice, String predictedCapRate) {

        this.type = type;
        this.neighborhood = neighborhood;
        this.street = street;
        this.city = city;
        this.imgUrl = imgUrl;
        this.price = price;
        this.rooms = rooms;
        this.area = area;
        this.floor = floor;
        this.houseUrl = houseUrl;
        this.predictedSalePrice = predictedSalePrice;
        this.predictedRentPrice = predictedRentPrice;
        this.predictedCapRate = predictedCapRate;
    }

    public RealEstate(String type, String city, JSONObject jsonObject, JSONObject predictedData) {
        this.type = type;
        this.neighborhood = jsonObject.getString("neighborhood");
        this.street = jsonObject.getString("street");
        this.city = city;
        this.imgUrl = jsonObject.getString("imgUrl");
        this.price = jsonObject.getString("price");
        this.rooms = jsonObject.getInt("rooms");
        this.area = jsonObject.getInt("area");
        this.floor = jsonObject.getString("floor");
        this.houseUrl = jsonObject.getString("houseUrl");
        try {
            this.predictedSalePrice = predictedData.getInt("salePrice");
            this.predictedRentPrice = predictedData.getInt("rentPrice");
            this.predictedCapRate = predictedData.getString("capRate");
        } catch (Exception e) {

        }
    }

    public RealEstate(JSONObject data, JSONObject predictedData) {
        this.type = data.getString("type");
        this.neighborhood = data.getString("neighborhood");
        this.street = data.getString("street");
        this.city = data.getString("city");
        this.imgUrl = data.getString("imgUrl");
        this.price = data.getString("price");
        this.rooms = data.getInt("rooms");
        this.area = data.getInt("area");
        this.floor = data.getString("floor");
        this.houseUrl = data.getString("houseUrl");
        try {
            this.latitude = data.getJSONObject("coordinates").getFloat("latitude");
            this.longitude = data.getJSONObject("coordinates").getFloat("longitude");
        } catch (Exception e) {
        }
        try {
            this.predictedSalePrice = predictedData.getInt("salePrice");
            this.predictedRentPrice = predictedData.getInt("rentPrice");
            this.predictedCapRate = predictedData.getString("capRate");
        } catch (Exception e) {

        }
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "RealEstate [type=" + type + ", neighborhood=" + neighborhood + ", city=" + city + ", imgUrl=" + imgUrl
                + ", price=" + price + ", rooms=" + rooms + ", area=" + area + ", floor=" + floor + ", houseUrl="
                + houseUrl + ", createdAt=" + createdAt + "]" + "predictedPrice= " + predictedSalePrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getHouseUrl() {
        return houseUrl;
    }

    public void setHouseUrl(String houseUrl) {
        this.houseUrl = houseUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Column(nullable = false, updatable = false)
    private Date createdAt = Dates.nowUTC();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("createdAt")
    public LocalDateTime calcCreatedAt() {
        return Dates.atLocalTime(createdAt);
    }
}
