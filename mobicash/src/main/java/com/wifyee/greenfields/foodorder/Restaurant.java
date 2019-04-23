package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 12/19/2017.
 */

public  class Restaurant  extends JSONObject implements Serializable {

    public String merchant_id;
    public String restaurant_name;
    public String logo;

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }



}