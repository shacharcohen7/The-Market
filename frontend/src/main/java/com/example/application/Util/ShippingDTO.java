package com.example.application.Util;

import java.util.Date;

public class ShippingDTO {
    private String shipping_id;
    private String memberId;
    private String country;
    private String city;
    private String address;
    private String zip;
    private Date date;
    private String acquisitionId;
    private int transactionId;

    // Constructor
    public ShippingDTO(String shipping_id , int transactionId , String memberId, String country, String city,
                       String address , String acquisitionId ) {
        this.shipping_id = shipping_id;
        this.memberId = memberId;
        this.country = country;
        this.city = city;
        this.address = address;
        this.date = new Date(); // Current date and time
        this.acquisitionId = acquisitionId;
        this.transactionId = transactionId;
    }

    // No-argument constructor required by JPA
    public ShippingDTO() {
    }

    public String getShipping_id() {
        return shipping_id;
    }

    // Getters
    public String getMemberId() {
        return memberId;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getZip() {
        return zip;
    }

    public Date getDate() {
        return date;
    }

    public String getAcquisitionId() {
        return acquisitionId;
    }

    public void setAcquisitionId(String acquisitionId) {
        this.acquisitionId = acquisitionId;
    }

    public int getTransactionId() {
        return transactionId;
    }
}
