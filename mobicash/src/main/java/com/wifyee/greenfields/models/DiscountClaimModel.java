package com.wifyee.greenfields.models;

public class DiscountClaimModel {
    private String Id,VoucherNo,VoucherName,VoucherDetails,VoucherAmount,DiscountAmount;

    public DiscountClaimModel(String id, String voucherNo, String voucherName, String voucherDetails,
                              String voucherAmount, String discountAmount) {
        Id = id;
        VoucherNo = voucherNo;
        VoucherName = voucherName;
        VoucherDetails = voucherDetails;
        VoucherAmount = voucherAmount;
        DiscountAmount = discountAmount;
    }

    public String getId() {
        return Id;
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

    public String getDiscountAmount() {
        return DiscountAmount;
    }
}
