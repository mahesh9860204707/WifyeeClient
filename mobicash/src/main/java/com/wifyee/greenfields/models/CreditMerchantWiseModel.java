package com.wifyee.greenfields.models;

public class CreditMerchantWiseModel {
    private String McdId,MerchantCreditId,ReceivedCredit,PartialPaidCredit,Description;

    public CreditMerchantWiseModel(String mcdId, String merchantCreditId, String receivedCredit, String partialPaidCredit, String description) {
        McdId = mcdId;
        MerchantCreditId = merchantCreditId;
        ReceivedCredit = receivedCredit;
        PartialPaidCredit = partialPaidCredit;
        Description = description;
    }

    public String getMcdId() {
        return McdId;
    }

    public String getMerchantCreditId() {
        return MerchantCreditId;
    }

    public String getReceivedCredit() {
        return ReceivedCredit;
    }

    public String getPartialPaidCredit() {
        return PartialPaidCredit;
    }

    public String getDescription() {
        return Description;
    }
}
