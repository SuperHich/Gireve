package com.dotit.gireve.entity;

/**
 * Created by Hichem Laroussi @SH on 03/04/2015.
 */
public class EVSEProperty implements Comparable {

    private int id;
    private int nameId;
    private String nameChar;

    public EVSEProperty(int id, int nameId){
        this.id = id;
        this.nameId = nameId;
    }

    public EVSEProperty(int id, String nameChar){
        this.id = id;
        this.nameChar = nameChar;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public String getNameChar() {
        return nameChar;
    }

    public void setNameChar(String nameChar) {
        this.nameChar = nameChar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Object another) {
        EVSEProperty a = (EVSEProperty) another;
        return getNameChar().compareToIgnoreCase(a.getNameChar());
    }
}
