package com.wifyee.greenfields.models.requests;

import org.json.JSONObject;

import java.io.Serializable;

public class LoginRequest extends JSONObject implements Serializable {

    public String clientmobile;
    public String pincode;
    public String hash;
}