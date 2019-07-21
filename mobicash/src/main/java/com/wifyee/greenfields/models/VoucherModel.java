package com.wifyee.greenfields.models;

public class VoucherModel {
    private String Id,VoucherNo,VoucherName,VoucherDetails,VoucherAmount,DiscountAmount,ValidFrom,ValidUpto,ImageUrl;

    public VoucherModel(String id, String voucherNo, String voucherName, String voucherDetails, String voucherAmount,
                        String discountAmount, String validFrom, String validUpto, String imageUrl) {
        Id = id;
        VoucherNo = voucherNo;
        VoucherName = voucherName;
        VoucherDetails = voucherDetails;
        VoucherAmount = voucherAmount;
        DiscountAmount = discountAmount;
        ValidFrom = validFrom;
        ValidUpto = validUpto;
        ImageUrl = imageUrl;
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

    public String getValidFrom() {
        return ValidFrom;
    }

    public String getValidUpto() {
        return ValidUpto;
    }

    public String getImageUrl() {
        return ImageUrl;
    }
}
