package com.wifyee.greenfields.models;

public class MyCreditModel {
    private String MerId,MerCompany,TotalAmount,MerTypeId,MerTypeName,
            MerchantProfileImage,MerCreditId,MerchantsDistZipcode;

    public MyCreditModel(String merId, String merCompany, String totalAmount, String merTypeId,
                         String merTypeName,String merchantProfileImage,String merCreditId,String merchantsDistZipcode) {
        MerId = merId;
        MerCompany = merCompany;
        TotalAmount = totalAmount;
        MerTypeId = merTypeId;
        MerTypeName = merTypeName;
        MerchantProfileImage = merchantProfileImage;
        MerCreditId = merCreditId;
        MerchantsDistZipcode = merchantsDistZipcode;
    }

    public String getMerchantsDistZipcode() {
        return MerchantsDistZipcode;
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

    public String getMerchantProfileImage() {
        return MerchantProfileImage;
    }

    public String getMerCreditId() {
        return MerCreditId;
    }


}
