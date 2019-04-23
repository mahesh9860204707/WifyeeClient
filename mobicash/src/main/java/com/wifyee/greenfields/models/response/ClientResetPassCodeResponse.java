package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class ClientResetPassCodeResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String clientId;
    public String resetStatus;
    public String msg;
}