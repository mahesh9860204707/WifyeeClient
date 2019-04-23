package com.wifyee.greenfields.models.requests;

import org.json.JSONObject;

import java.io.Serializable;

public class GetClientProfileRequest extends JSONObject implements Serializable {

    public String clientId;
    public String pincode;
    public String hash;
}