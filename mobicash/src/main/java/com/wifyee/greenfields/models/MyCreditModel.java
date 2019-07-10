package com.wifyee.greenfields.models;

public class MyCreditModel {
    private String MerId,MerCompany,TotalAmount,MerTypeId,MerTypeName;

    public MyCreditModel(String merId, String merCompany, String totalAmount, String merTypeId, String merTypeName) {
        MerId = merId;
        MerCompany = merCompany;
        TotalAmount = totalAmount;
        MerTypeId = merTypeId;
        MerTypeName = merTypeName;
    }

    public String getMerTypeId() {
        return MerTypeId;
    }

    public String getMerTypeName() {
        return MerTypeName;
    }

    public String getMerId() {
        return MerId;
    }

    public String getMerCompany() {
        return MerCompany;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }
}
