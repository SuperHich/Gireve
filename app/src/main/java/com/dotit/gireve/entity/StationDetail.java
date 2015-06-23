package com.dotit.gireve.entity;

/**
 * Created by Hichem Laroussi @SH on 06/04/2015.
 */
public class StationDetail {

    private String name;
    private String value;
    private boolean boolValue;
    private EVSETypeEnum type;

    public StationDetail(String name, String value, EVSETypeEnum type){
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public StationDetail(String name, boolean boolValue, EVSETypeEnum type){
        this.name = name;
        this.boolValue = boolValue;
        this.type = type;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isBoolValue() {
        return boolValue;
    }

    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }

    public EVSETypeEnum getType() {
        return type;
    }

    public void setType(EVSETypeEnum type) {
        this.type = type;
    }
}
