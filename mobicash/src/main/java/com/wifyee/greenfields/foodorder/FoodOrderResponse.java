package com.wifyee.greenfields.foodorder;

import com.wifyee.greenfields.models.response.operators.OperatorList;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 12/6/2017.
 */

public class FoodOrderResponse  extends JSONObject implements Serializable {

    public String status;
    public List<FoodOderList> FoodOderList;
    public List<VegORNonVegList> VegOrNonVegFoodOderList;
    public String msg;
}
