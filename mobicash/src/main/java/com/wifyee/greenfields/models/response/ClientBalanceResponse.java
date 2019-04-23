package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class ClientBalanceResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String clientBalance;
    public String clientId;
    public String clientMobile;
    public String reqDate;
    public String msg;
}