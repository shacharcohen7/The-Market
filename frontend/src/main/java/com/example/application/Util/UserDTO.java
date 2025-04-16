package com.example.application.Util;

public class UserDTO {
    private String userName;
    private String birthday;
    private String country;
    private String city;
    private String address;
    private String name;
    private String userId;

    public UserDTO(String userId, String userName, String birthday, String country, String city, String address, String name){
        this.userName = userName;
        this.birthday = birthday;
        this.country = country;
        this.city = city;
        this.address = address;
        this.name = name;
        this.userId = userId;
    }

    public UserDTO(){

    }

    public String getUserName() {
        return userName;
    }

    public String getBirthday() {
        return birthday;
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

    public String getName() {
        return name;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }
}
