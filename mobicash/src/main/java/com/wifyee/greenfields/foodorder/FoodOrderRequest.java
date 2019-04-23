package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 12/6/2017.
 */

public class FoodOrderRequest extends JSONObject implements Serializable {

    public String FoodType;
    public String MerchantId;

}
