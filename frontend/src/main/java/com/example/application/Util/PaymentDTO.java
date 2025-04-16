package com.example.application.Util;

public class PaymentDTO {
    private String holderId;
    private String holderName;
    private String currency;
    private String creditCardNumber;
    private int cvv;
    private int month;
    private int year;

    public PaymentDTO(String holderId,String holderName, String currency, String creditCardNumber, int cvv, int month, int year) {
        this.holderId = holderId;
        this.holderName = holderName;
        this.currency = currency;
        this.creditCardNumber = creditCardNumber;
        this.cvv = cvv;
        this.month = month;
        this.year = year;
    }

    public String getHolderId() {
        return holderId;
    }
    public String getHolderName() {
        return holderName;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public int getCvv() {
        return cvv;
    }
    public String getCurrency() {
        return currency;
    }


    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void setHolderId(String holderId) {
        this.holderId = holderId;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public PaymentDTO(){

    }
}
