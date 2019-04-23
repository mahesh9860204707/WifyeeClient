package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class ClientStatusResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String clientStatus;
    public String clientId;
    public String clientMobile;
    public String loginDate;
    public String msg;
}