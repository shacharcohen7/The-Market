package com.example.application.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptDTO {
    private String receiptId;
    private String storeId;
    private String memberId;
    private Map<String, List<Integer>> productList = new HashMap<>(); //<productName, price>

    public ReceiptDTO(String receiptId, String storeId, String memberId, Map<String, List<Integer>> productList)
    {
        this.receiptId = receiptId;
        this.storeId = storeId;
        this.memberId = memberId;
        this. productList = productList;
    }

    public ReceiptDTO(){

    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setProductList(Map<String, List<Integer>> productList) {
        this.productList = productList;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getMemberId() {
        return memberId;
    }

    public Map<String, List<Integer>> getProductList() {
        return productList;
    }


}
