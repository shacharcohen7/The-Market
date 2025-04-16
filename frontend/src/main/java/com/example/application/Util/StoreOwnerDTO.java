package com.example.application.Util;

public class StoreOwnerDTO {
    private String storeId;
    private String memberId;
    private boolean founder;
    private String nominatorId;

    public StoreOwnerDTO(String member_ID, String store_ID, boolean founder, String nominatorId) {
        this.storeId =  store_ID;
        this.memberId = member_ID;
        this.founder = founder;
        this.nominatorId = nominatorId;
    }

    public StoreOwnerDTO() {

    }


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public boolean isFounder() {
        return founder;
    }

    public void setFounder(boolean founder) {
        this.founder = founder;
    }

    public String getNominatorId() {
        return nominatorId;
    }

    public void setNominatorId(String nominatorId) {
        this.nominatorId = nominatorId;
    }

}
