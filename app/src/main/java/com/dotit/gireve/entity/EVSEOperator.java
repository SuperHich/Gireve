package com.dotit.gireve.entity;

/**
 * Created by Hichem Laroussi @SH on 06/04/2015.
 */
public class EVSEOperator {

    private String name;
    private String value;

    public EVSEOperator(String name, String value){
        this.name = name;
        this.value = value;
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
}
