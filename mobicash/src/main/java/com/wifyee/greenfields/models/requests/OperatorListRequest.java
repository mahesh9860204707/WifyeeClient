package com.wifyee.greenfields.models.requests;

import org.json.JSONObject;

import java.io.Serializable;

public class OperatorListRequest extends JSONObject implements Serializable {

    public String moduleType;
    public String rechargeType;
}