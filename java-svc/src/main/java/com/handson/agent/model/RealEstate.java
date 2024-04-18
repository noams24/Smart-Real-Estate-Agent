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
    private Integer price;
    private Integer rooms;
    private Integer area;
    private Integer floor;
    
    // @Indexed(unique = true)
    private String houseUrl;


    public RealEstate() {}

    public RealEstate(String type, String neighborhood, String street, String city, String imgUrl, Integer price,
            Integer rooms, Integer area, Integer floor, String houseUrl) {

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
    }

    public RealEstate(String type, String city, JSONObject jsonObject) {
        this.type = type;
        this.neighborhood = jsonObject.getString("neighborhood");
        this.street = jsonObject.getString("street");
        this.city = city;
        this.imgUrl = jsonObject.getString("imgUrl");
        this.price = jsonObject.getInt("price");
        this.rooms = jsonObject.getInt("rooms");
        this.area = jsonObject.getInt("area");
        this.floor = jsonObject.getInt("floor");
        this.houseUrl = jsonObject.getString("houseUrl");
    }


    @Override
    public String toString() {
        return "RealEstate [type=" + type + ", neighborhood=" + neighborhood + ", city=" + city + ", imgUrl=" + imgUrl
                + ", price=" + price + ", rooms=" + rooms + ", area=" + area + ", floor=" + floor + ", houseUrl="
                + houseUrl + ", createdAt=" + createdAt + "]";
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
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

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
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
