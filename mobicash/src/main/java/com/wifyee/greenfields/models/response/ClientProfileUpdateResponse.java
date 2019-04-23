package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class ClientProfileUpdateResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String clientId;
    public ClientProfile clientProfile;
    public String clientProfileState;
    public String format;
    public String msg;
}