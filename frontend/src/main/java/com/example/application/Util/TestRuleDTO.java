package com.example.application.Util;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestRuleDTO {
    private String type;
    private String range;
    private String category;
    private String productName;
    private String description;
    private Boolean contains;

    private Integer age;
    private Integer quantity;
    private LocalDate date;
    private Integer price;
    private LocalTime time;

    public TestRuleDTO(String type, String range, String category, String productName, String description, Boolean contains, Integer age, Integer quantity, LocalDate date, Integer price, LocalTime time) {
        this.type = type;
        this.range = range;
        this.category = category;
        this.productName = productName;
        this.description = description;
        this.contains = contains;
        this.age = age;
        this.quantity = quantity;
        this.date = date;
        this.price = price;
        this.time = time;
    }

    public TestRuleDTO() {

    }

    public String getType() {
        return type;
    }

    public String getRange() {
        return range;
    }

    public String getCategory() {
        return category;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isContains() {
        return contains;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getPrice() {
        return price;
    }

    public LocalTime getTime() {
        return time;
    }
}
