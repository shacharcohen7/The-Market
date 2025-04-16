package com.example.application.Util;

import java.util.List;

public class StoreDTO {
    private String store_ID;
    private boolean isOpened;
    private double rating;
    private int numOfRatings;
    private String storeName;
    private String description;
    private List<String> products;
    // Constructor
    public StoreDTO(List<String> products,String store_ID, boolean isOpened, double rating, int numOfRatings, String storeName , String description) {
        this.store_ID = store_ID;
        this.isOpened = isOpened;
        this.rating = rating;
        this.description = description;
        this.numOfRatings = numOfRatings;
        this.storeName = storeName;
        this.products = products;
    }
    // Getters and Setters

    public StoreDTO(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public double getRating() {
        return rating;
    }

    public String getStore_ID() {
        return store_ID;
    }

    public int getNumOfRatings() {
        return numOfRatings;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStore_ID(String store_ID) {
        this.store_ID = store_ID;
    }

    public void setNumOfRatings(int numOfRatings) {
        this.numOfRatings = numOfRatings;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getProducts() {
        return products;
    }
}
