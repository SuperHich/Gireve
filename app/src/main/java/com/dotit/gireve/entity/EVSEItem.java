package com.dotit.gireve.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hichem Laroussi @SH on 26/03/2015.
 */
public class EVSEItem implements Serializable{

    public static final String SOAP_EVSEIdType                  = "EVSEIdType";
    public static final String SOAP_EVSEId                      = "EVSEId";
    public static final String SOAP_chargingStationIdType       = "chargingStationIdType";
    public static final String SOAP_chargingStationId           = "chargingStationId";
    public static final String SOAP_chargingPoolIdType          = "chargingPoolIdType";
    public static final String SOAP_chargingPoolId              = "chargingPoolId";
    public static final String SOAP_chargingPoolBrandName       = "chargingPoolBrandName";
    public static final String SOAP_chargingPoolName            = "chargingPoolName";
    public static final String SOAP_chargingPoolAddress         = "chargingPoolAddress";
    public static final String SOAP_entrancelatitude            = "entrancelatitude";
    public static final String SOAP_entrancelongitude           = "entrancelongitude";
    public static final String SOAP_chargingStationlatitude     = "chargingStationlatitude";
    public static final String SOAP_chargingStationlongitude    = "chargingStationlongitude";
    public static final String SOAP_phoneNumber                 = "phoneNumber";
    public static final String SOAP_connectorType               = "connectorType";
    public static final String SOAP_power                       = "power";
    public static final String SOAP_speed                       = "speed";
    public static final String SOAP_authorisationInformation    = "authorisationInformation";
    public static final String SOAP_paymentInformation          = "paymentInformation";
    public static final String SOAP_authorisationMode           = "authorisationMode";
    public static final String SOAP_paymentMode                 = "paymentMode";
    public static final String SOAP_accessibility               = "accessibility";
    public static final String SOAP_isOpen24_7                  = "isOpen24_7";
    public static final String SOAP_openTimesListList           = "openTimesListList";
    public static final String SOAP_bookable                    = "bookable";
    public static final String SOAP_availabilityStatus          = "availabilityStatus";
    public static final String SOAP_availabilityStatusUntil     = "availabilityStatusUntil";
    public static final String SOAP_busyStatus                  = "busyStatus";
    public static final String SOAP_busyStatusUntil             = "busyStatusUntil";
    public static final String SOAP_useabilityStatus            = "useabilityStatus";
    public static final String SOAP_useabilityStatusUntil       = "useabilityStatusUntil";


    private String EVSEIdType;
    private String EVSEId;
    private String chargingStationIdType;
    private String chargingStationId;
    private String chargingPoolIdType;
    private String chargingPoolId;
    private String chargingPoolBrandName;
    private String chargingPoolName;
    private ChargingPoolAddress chargingPoolAddress;
    private double entrancelatitude;
    private double entrancelongitude;
    private double chargingStationlatitude;
    private double chargingStationlongitude;
    private String phoneNumber;
    private ArrayList<Integer> connectorTypes;
    private double power;
    private String speed;
    private String authorisationInformation;
    private String paymentInformation;
    private int authorisationMode;
    private int paymentMode;
    private int accessibility;
    private boolean isOpen24_7;
    private ArrayList<OpenTime> openTimesListList;
    private boolean bookable;
    private int availabilityStatus;
    private String availabilityStatusUntil;
    private int busyStatus;
    private String busyStatusUntil;
    private int useabilityStatus;
    private String useabilityStatusUntil;
    private int nbEVSE = 0;

    public EVSEItem() {
        connectorTypes = new ArrayList<>();
        openTimesListList = new ArrayList<>();
    }

    public String getEVSEIdType() {
        return EVSEIdType;
    }

    public void setEVSEIdType(String EVSEIdType) {
        this.EVSEIdType = EVSEIdType;
    }

    public String getEVSEId() {
        return EVSEId;
    }

    public void setEVSEId(String EVSEId) {
        this.EVSEId = EVSEId;
    }

    public String getChargingStationIdType() {
        return chargingStationIdType;
    }

    public void setChargingStationIdType(String chargingStationIdType) {
        this.chargingStationIdType = chargingStationIdType;
    }

    public String getChargingStationId() {
        return chargingStationId;
    }

    public void setChargingStationId(String chargingStationId) {
        this.chargingStationId = chargingStationId;
    }

    public String getChargingPoolIdType() {
        return chargingPoolIdType;
    }

    public void setChargingPoolIdType(String chargingPoolIdType) {
        this.chargingPoolIdType = chargingPoolIdType;
    }

    public String getChargingPoolId() {
        return chargingPoolId;
    }

    public void setChargingPoolId(String chargingPoolId) {
        this.chargingPoolId = chargingPoolId;
    }

    public String getChargingPoolBrandName() {
        return chargingPoolBrandName;
    }

    public void setChargingPoolBrandName(String chargingPoolBrandName) {
        this.chargingPoolBrandName = chargingPoolBrandName;
    }

    public String getChargingPoolName() {
        return chargingPoolName;
    }

    public void setChargingPoolName(String chargingPoolName) {
        this.chargingPoolName = chargingPoolName;
    }

    public ChargingPoolAddress getChargingPoolAddress() {
        return chargingPoolAddress;
    }

    public void setChargingPoolAddress(ChargingPoolAddress chargingPoolAddress) {
        this.chargingPoolAddress = chargingPoolAddress;
    }

    public double getEntrancelatitude() {
        return entrancelatitude;
    }

    public void setEntrancelatitude(double entrancelatitude) {
        this.entrancelatitude = entrancelatitude;
    }

    public double getEntrancelongitude() {
        return entrancelongitude;
    }

    public void setEntrancelongitude(double entrancelongitude) {
        this.entrancelongitude = entrancelongitude;
    }

    public double getChargingStationlatitude() {
        return chargingStationlatitude;
    }

    public void setChargingStationlatitude(double chargingStationlatitude) {
        this.chargingStationlatitude = chargingStationlatitude;
    }

    public double getChargingStationlongitude() {
        return chargingStationlongitude;
    }

    public void setChargingStationlongitude(double chargingStationlongitude) {
        this.chargingStationlongitude = chargingStationlongitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<Integer> getConnectorTypes() {
        return connectorTypes;
    }

    public void setConnectorTypes(ArrayList<Integer> connectorTypes) {
        this.connectorTypes = connectorTypes;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getAuthorisationInformation() {
        return authorisationInformation;
    }

    public void setAuthorisationInformation(String authorisationInformation) {
        this.authorisationInformation = authorisationInformation;
    }

    public String getPaymentInformation() {
        return paymentInformation;
    }

    public void setPaymentInformation(String paymentInformation) {
        this.paymentInformation = paymentInformation;
    }

    public int getAuthorisationMode() {
        return authorisationMode;
    }

    public void setAuthorisationMode(int authorisationMode) {
        this.authorisationMode = authorisationMode;
    }

    public int getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(int paymentMode) {
        this.paymentMode = paymentMode;
    }

    public int getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(int accessibility) {
        this.accessibility = accessibility;
    }

    public boolean isOpen24_7() {
        return isOpen24_7;
    }

    public void setOpen24_7(boolean isOpen24_7) {
        this.isOpen24_7 = isOpen24_7;
    }

    public ArrayList<OpenTime> getOpenTimesListList() {
        return openTimesListList;
    }

    public void setOpenTimesListList(ArrayList<OpenTime> openTimesListList) {
        this.openTimesListList = openTimesListList;
    }

    public boolean isBookable() {
        return bookable;
    }

    public void setBookable(boolean bookable) {
        this.bookable = bookable;
    }

    public int getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(int availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getAvailabilityStatusUntil() {
        return availabilityStatusUntil;
    }

    public void setAvailabilityStatusUntil(String availabilityStatusUntil) {
        this.availabilityStatusUntil = availabilityStatusUntil;
    }

    public int getBusyStatus() {
        return busyStatus;
    }

    public void setBusyStatus(int busyStatus) {
        this.busyStatus = busyStatus;
    }

    public String getBusyStatusUntil() {
        return busyStatusUntil;
    }

    public void setBusyStatusUntil(String busyStatusUntil) {
        this.busyStatusUntil = busyStatusUntil;
    }

    public int getUseabilityStatus() {
        return useabilityStatus;
    }

    public void setUseabilityStatus(int useabilityStatus) {
        this.useabilityStatus = useabilityStatus;
    }

    public String getUseabilityStatusUntil() {
        return useabilityStatusUntil;
    }

    public void setUseabilityStatusUntil(String useabilityStatusUntil) {
        this.useabilityStatusUntil = useabilityStatusUntil;
    }

    public int getNbEVSE() {
        return nbEVSE;
    }

    public void setNbEVSE(int nbEVSE) {
        this.nbEVSE = nbEVSE;
    }
}
