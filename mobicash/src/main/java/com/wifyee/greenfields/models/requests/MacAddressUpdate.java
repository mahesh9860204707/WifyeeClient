package com.wifyee.greenfields.models.requests;

import com.wifyee.greenfields.foodorder.Items;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class MacAddressUpdate extends JSONObject implements Serializable {

    public String mobileNumbers;
    public String hash;

}
