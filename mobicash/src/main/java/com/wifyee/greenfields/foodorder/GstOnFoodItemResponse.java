package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 12/15/2017.
 */

public class GstOnFoodItemResponse extends JSONObject implements Serializable {

    public String status;

    public String gst;
    public String msg;
}
