package com.wifyee.greenfields.models;

import com.wifyee.greenfields.foodorder.FoodOderList;
import com.wifyee.greenfields.foodorder.VegORNonVegList;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 12/6/2017.
 */

public class CountriesResponse extends JSONObject implements Serializable {

    public String status;
    public List<CountriesList> data;

    public String msg;
}
