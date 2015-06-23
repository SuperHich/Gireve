package com.dotit.gireve.entity;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Hichem Laroussi @SH on 20/04/2015.
 */
public class ChargingPoolItem implements Serializable {

    private String id;
    private String name;
    private String type;
    private String address;
    private double latitude;
    private double longitude;
    private LinkedList<EVSEItem> evseItems;

    public ChargingPoolItem(String id, String name, String type, String address, double latitude, double longitude){
        this.id = id;
        this.name= name;
        this.type = type;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        evseItems = new LinkedList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LinkedList<EVSEItem> getEvseItems() {
        return evseItems;
    }

    public void setEvseItems(LinkedList<EVSEItem> evseItems) {
        this.evseItems = evseItems;
    }
}
