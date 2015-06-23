package com.dotit.gireve.entity;

/**
 * Created by Hichem Laroussi @SH on 06/04/2015.
 */
public class EVSECriteria {

    private int id;
    private EVSEProperty property;
    private EVSEOperator operator;
    private String value;

    public EVSECriteria(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EVSEProperty getProperty() {
        return property;
    }

    public void setProperty(EVSEProperty property) {
        this.property = property;
    }

    public EVSEOperator getOperator() {
        return operator;
    }

    public void setOperator(EVSEOperator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
