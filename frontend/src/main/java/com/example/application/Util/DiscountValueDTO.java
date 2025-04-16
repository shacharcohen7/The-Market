package com.example.application.Util;

import java.util.List;

public class DiscountValueDTO {
    private int percentage;
    private String category;
    private boolean isStoreDiscount;
    private List<String> productsNames;

    public DiscountValueDTO(int percentage, String category, boolean isStoreDiscount, List<String> productsNames) {
        this.percentage = percentage;
        this.category = category;
        this.isStoreDiscount = isStoreDiscount;
        this.productsNames = productsNames;
    }

    public DiscountValueDTO(){

    }

    public int getPercentage() {
        return percentage;
    }

    public String getCategory() {
        return category;
    }

    public boolean getIsStoreDiscount() {
        return isStoreDiscount;
    }

    public List<String> getProductsNames() {
        return productsNames;
    }
}
