package com.wifyee.greenfields.models.response.operators;

import org.json.JSONObject;

import java.io.Serializable;

public class ElectricityOrGas extends JSONObject implements Serializable {

    public Electricity electricityList;
    public Gas gasList;
}