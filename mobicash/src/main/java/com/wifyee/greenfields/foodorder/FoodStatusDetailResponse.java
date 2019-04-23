package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 12/20/2017.
 */

public class FoodStatusDetailResponse   extends JSONObject implements Serializable {

    public String status;

    public OrderStatusDetailList orderStatusDetailList;

    public String msg;
}