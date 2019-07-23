package com.wifyee.greenfields.models;

public class MyVoucherModel {
    private String Id, VoucherId, VoucherNo, VoucherName, VoucherDetails, VoucherAmount, BalanceAmount,
            Quantity, ValidFrom, ValidUpto, ImageUrl, isVoucherExpired;

    public MyVoucherModel(String id, String voucherId, String voucherNo, String voucherName, String voucherDetails,
                          String voucherAmount, String balanceAmount, String quantity, String validFrom,
                          String validUpto, String imageUrl, String isVoucherExpired) {
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
}
