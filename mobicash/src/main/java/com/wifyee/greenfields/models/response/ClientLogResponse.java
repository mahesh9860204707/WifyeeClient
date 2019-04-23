package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class ClientLogResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String clientId;
    public LogList logList;
    public String format;
    public String msg;
}