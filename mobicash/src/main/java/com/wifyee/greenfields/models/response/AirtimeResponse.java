package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class AirtimeResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String moduleType;
    public String number;
    public String amount;
    public String rechargeStatus;
    public String referenceID;
    public String txnDate;
    public String msg;
    public String rechargeID;
    public String transactionId;
}