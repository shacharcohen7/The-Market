package com.example.application.Util;

public class StoreManagerDTO {


    private boolean inventoryPermissions;
    private boolean purchasePermissions;
    private String nominatorMemberId;
    private String storeId;
    private String memberId;


    public StoreManagerDTO(String member_ID, String store_ID, boolean inventoryPermissions, boolean purchasePermissions, String nominatorMemberId)
    {
        this.storeId = store_ID;
        this.memberId = member_ID;
        this.inventoryPermissions = inventoryPermissions;
        this.purchasePermissions = purchasePermissions;
        this.nominatorMemberId = nominatorMemberId;
    }

    public StoreManagerDTO() {

    }

    public void setPermissions(boolean inventoryPermissions, boolean purchasePermissions){
        this.inventoryPermissions = inventoryPermissions;
        this.purchasePermissions = purchasePermissions;
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

    public boolean isInventoryPermissions() {
        return inventoryPermissions;
    }

    public void setInventoryPermissions(boolean inventoryPermissions) {
        this.inventoryPermissions = inventoryPermissions;
    }

    public boolean isPurchasePermissions() {
        return purchasePermissions;
    }

    public void setPurchasePermissions(boolean purchasePermissions) {
        this.purchasePermissions = purchasePermissions;
    }

    public String getNominatorMemberId() {
        return nominatorMemberId;
    }

    public void setNominatorMemberId(String nominatorMemberId) {
        this.nominatorMemberId = nominatorMemberId;
    }
}
