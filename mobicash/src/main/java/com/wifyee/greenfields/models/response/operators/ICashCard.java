package com.wifyee.greenfields.models.response.operators;

import org.json.JSONObject;

import java.io.Serializable;

public class ICashCard extends JSONObject implements Serializable {

    public Generate generateList;
    public Prepaid prepaidList;
}