package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 12/19/2017.
 */

public class AddressResponse extends JSONObject implements Serializable {

    public String status;
   public List<Restaurant> restaurant;
    public String msg;

}