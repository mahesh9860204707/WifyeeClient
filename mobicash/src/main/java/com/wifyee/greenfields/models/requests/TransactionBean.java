package com.wifyee.greenfields.models.requests;

import java.io.Serializable;

/**
 * Created by amit on 4/9/2018.
 */

public class TransactionBean implements Serializable
{
    private String TXN_ID;
    private String TRANSACTION_DATE;
    private String STATUS;
    private String AMOUNT;
    private String BENEFICIARY_MOBILE;
    private String ACCOUNT_NUMBER;
    private String IFSC_CODE;
    private String REFERENCE_NO;

    public String getTXN_ID() {
        return TXN_ID;
    }

    public void setTXN_ID(String TXN_ID) {
        this.TXN_ID = TXN_ID;
    }

    public String getTRANSACTION_DATE() {
        return TRANSACTION_DATE;
    }

    public void setTRANSACTION_DATE(String TRANSACTION_DATE) {
        this.TRANSACTION_DATE = TRANSACTION_DATE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getBENEFICIARY_MOBILE() {
        return BENEFICIARY_MOBILE;
    }

    public void setBENEFICIARY_MOBILE(String BENEFICIARY_MOBILE) {
        this.BENEFICIARY_MOBILE = BENEFICIARY_MOBILE;
    }

    public String getACCOUNT_NUMBER() {
        return ACCOUNT_NUMBER;
    }

    public void setACCOUNT_NUMBER(String ACCOUNT_NUMBER) {
        this.ACCOUNT_NUMBER = ACCOUNT_NUMBER;
    }

    public String getIFSC_CODE() {
        return IFSC_CODE;
    }

    public void setIFSC_CODE(String IFSC_CODE) {
        this.IFSC_CODE = IFSC_CODE;
    }

    public String getREFERENCE_NO() {
        return REFERENCE_NO;
    }

    public void setREFERENCE_NO(String REFERENCE_NO) {
        this.REFERENCE_NO = REFERENCE_NO;
    }


}
