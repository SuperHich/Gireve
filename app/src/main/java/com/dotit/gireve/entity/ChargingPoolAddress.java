package com.dotit.gireve.entity;

import java.io.Serializable;

/**
 * Created by Hichem Laroussi @SH on 01/04/2015.
 */
public class ChargingPoolAddress implements Serializable{

    public static final String SOAP_StreetNumber                = "StreetNumber";
    public static final String SOAP_StreetName                  = "StreetName";
    public static final String SOAP_PostCode                    = "PostCode";
    public static final String SOAP_City                        = "City";
    public static final String SOAP_Province                    = "Province";
    public static final String SOAP_Country                     = "Country";

    private String StreetNumber;
    private String StreetName;
    private String PostCode;
    private String City;
    private String Province;
    private String Country;

    public String getStreetName() {
        return StreetName;
    }

    public void setStreetName(String streetName) {
        StreetName = streetName;
    }

    public String getStreetNumber() {
        return StreetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        StreetNumber = streetNumber;
    }

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String postCode) {
        PostCode = postCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

}
