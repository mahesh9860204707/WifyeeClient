package com.wifyee.greenfields.models.response.operators;

import org.json.JSONObject;

import java.io.Serializable;

public class Airtime extends JSONObject implements Serializable {

    public Prepaid prepaid;
    public DthList dthList;
    public Postpaid postpaidList;
    public Landline landlineList;
}