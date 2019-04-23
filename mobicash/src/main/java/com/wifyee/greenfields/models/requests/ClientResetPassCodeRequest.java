package com.wifyee.greenfields.models.requests;

import org.json.JSONObject;

import java.io.Serializable;

public class ClientResetPassCodeRequest extends JSONObject implements Serializable {

    public String clientId;
    public String pinCode;
    public String newPinCode;
    public String hash;
}