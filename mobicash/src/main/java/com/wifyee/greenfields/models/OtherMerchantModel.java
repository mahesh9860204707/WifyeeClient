package com.wifyee.greenfields.models;

public class OtherMerchantModel {
    private String Id,MerchantType,Image;

    public OtherMerchantModel(String id, String merchantType, String image) {
        Id = id;
        MerchantType = merchantType;
        Image = image;
    }

    public String getId() {
        return Id;
    }

    public String getMerchantType() {
        return MerchantType;
    }

    public String getImage() {
        return Image;
    }
}
