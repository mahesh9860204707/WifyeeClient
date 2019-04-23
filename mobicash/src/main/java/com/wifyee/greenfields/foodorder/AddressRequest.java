package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 12/13/2017.
 */

public class AddressRequest extends JSONObject implements Serializable {
public String latitude;
public String longitude;
public String productId;

}
