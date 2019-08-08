package com.wifyee.greenfields.models;

public class MyCreditModel {
    private String MerId,MerCompany,TotalAmount,MerTypeId,MerTypeName,
            MerchantProfileImage,MerCreditId,MerchantsDistZipcode,DueAmount;

    public MyCreditModel(String merId, String merCompany, String totalAmount, String merTypeId,
                         String merTypeName,String merchantProfileImage,String merCreditId,
                         String merchantsDistZipcode,String dueAmount) {
        MerId = merId;
        MerCompany = merCompany;
        TotalAmount = totalAmount;
        MerTypeId = merTypeId;
        MerTypeName = merTypeName;
        MerchantProfileImage = merchantProfileImage;
        MerCreditId = merCreditId;
        MerchantsDistZipcode = merchantsDistZipcode;
        DueAmount = dueAmount;
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

    public String getDueAmount() {
        return DueAmount;
    }
}
