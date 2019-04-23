package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class SignupResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String clientId;
    public String clientMobile;
    public String pincode;
    public String enrolledDate;
    public String msg;
}