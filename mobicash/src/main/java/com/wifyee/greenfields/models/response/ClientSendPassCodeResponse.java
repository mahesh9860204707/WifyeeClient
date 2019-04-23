package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class ClientSendPassCodeResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String clientMobile;
    public String sendStatus;
    public String msg;
}