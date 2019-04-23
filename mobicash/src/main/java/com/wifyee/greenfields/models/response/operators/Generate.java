package com.wifyee.greenfields.models.response.operators;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class Generate extends JSONObject implements Serializable {

    public List<OperatorDetails> operatorDetailsList;
}