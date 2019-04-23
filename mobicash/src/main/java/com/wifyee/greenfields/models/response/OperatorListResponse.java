package com.wifyee.greenfields.models.response;

import com.wifyee.greenfields.models.response.operators.OperatorList;

import org.json.JSONObject;

import java.io.Serializable;

public class OperatorListResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public OperatorList operatorList;
    public String format;
    public String reqDate;
    public String msg;
}