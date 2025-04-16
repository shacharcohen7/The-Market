package com.example.application.Util;

public class ProductDTO {
    private String productName;
    private int price;
    private int quantity;
    private String description;
    private String categoryStr;

    // Constructor
    public ProductDTO(String productName, int price, int quantity, String description, String categoryStr) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.categoryStr = categoryStr;
    }

    public ProductDTO(){

    }

    // Getters and Setters
    public String getName() {
        return productName;
    }

    public void setName(String name) {
        this.productName = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryStr() {
        return categoryStr;
    }

    public void setCategoryStr(String category) {
        this.categoryStr = category;
    }
}
