package com.wifyee.greenfields.models.requests;

import org.json.JSONObject;

import java.io.Serializable;

public class AirtimeRequest extends JSONObject implements Serializable {

    public String clientmobile;
    public String pincode;
    public String number;
    public String amount;
    public String cyclenumber;
    public String processingcyclenumber;
    public String operator;
    public String type;
    public String hash;
}