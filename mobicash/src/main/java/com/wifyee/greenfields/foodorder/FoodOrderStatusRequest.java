package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 12/20/2017.
 */

public class FoodOrderStatusRequest extends JSONObject implements Serializable {

    public String UserType;
    public String UserId;

}
