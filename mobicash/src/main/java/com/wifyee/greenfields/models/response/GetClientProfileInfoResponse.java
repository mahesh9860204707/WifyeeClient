package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class GetClientProfileInfoResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String clientId;
    public GetClientProfileInfo clientProfileInfo;
    public String format;
    public String msg;
}