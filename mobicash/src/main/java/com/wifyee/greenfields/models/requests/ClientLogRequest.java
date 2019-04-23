package com.wifyee.greenfields.models.requests;

import org.json.JSONObject;

import java.io.Serializable;

public class ClientLogRequest extends JSONObject implements Serializable {

    public String clientId;
    public String pincode;
    public String hash;
}