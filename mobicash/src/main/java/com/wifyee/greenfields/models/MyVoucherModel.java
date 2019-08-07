package com.wifyee.greenfields.models;

public class MyVoucherModel {
    private String Id, VoucherId, VoucherNo, VoucherName, VoucherDetails, VoucherAmount, BalanceAmount,
            Quantity, ValidFrom, ValidUpto, ImageUrl, isVoucherExpired,MerId,MerName,MerType,MerCurrentStatus,MerchantsDistZipcode;

    public MyVoucherModel(String id, String voucherId, String voucherNo, String voucherName, String voucherDetails,
                          String voucherAmount, String balanceAmount, String quantity, String validFrom,
                          String validUpto, String imageUrl, String isVoucherExpired, String merId,
                          String merName,String merType, String merCurrentStatus,String merchantsDistZipcode) {
        Id = id;
        VoucherId = voucherId;
        VoucherNo = voucherNo;
        VoucherName = voucherName;
        VoucherDetails = voucherDetails;
        VoucherAmount = voucherAmount;
        BalanceAmount = balanceAmount;
        Quantity = quantity;
        ValidFrom = validFrom;
        ValidUpto = validUpto;
        ImageUrl = imageUrl;
        this.isVoucherExpired = isVoucherExpired;
        MerId = merId;
        MerName = merName;
        MerType = merType;
        MerCurrentStatus = merCurrentStatus;
        MerchantsDistZipcode = merchantsDistZipcode;
    }

    public String getId() {
        return Id;
    }

    public String getVoucherId() {
        return VoucherId;
    }

    public String getVoucherNo() {
        return VoucherNo;
    }

    public String getVoucherName() {
        return VoucherName;
    }

    public String getVoucherDetails() {
        return VoucherDetails;
    }

    public String getVoucherAmount() {
        return VoucherAmount;
    }

    public String getBalanceAmount() {
        return BalanceAmount;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getValidFrom() {
        return ValidFrom;
    }

    public String getValidUpto() {
        return ValidUpto;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getIsVoucherExpired() {
        return isVoucherExpired;
    }

    public String getMerId() {
        return MerId;
    }

    public String getMerName() {
        return MerName;
    }

    public String getMerType() {
        return MerType;
    }

    public String getMerCurrentStatus() {
        return MerCurrentStatus;
    }

    public String getMerchantsDistZipcode() {
        return MerchantsDistZipcode;
    }
}
