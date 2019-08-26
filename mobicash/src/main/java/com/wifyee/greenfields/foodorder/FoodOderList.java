package com.wifyee.greenfields.foodorder;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;



import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by user on 12/6/2017.
 */

public class FoodOderList extends JSONObject implements Serializable {

    public String itemID;
    public String name;
    public String price;
    public String description;
    public String quantiy;
    public String foodImage;
    public String discountPrice;
    public String category;
    public String wifyeeCommision;
    public String distCommision;
    public  boolean checkvalue;

    public String getMerchantId() {
        return itemID;
    }

    public void setMerchantId(String merchantId) {
        this.itemID = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantiy() {
        return quantiy;
    }

    public void setQuantiy(String quantiy) {
        this.quantiy = quantiy;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public boolean isCheckvalue() {
        return checkvalue;
    }

    public void setCheckvalue(boolean checkvalue) {
        this.checkvalue = checkvalue;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWifyeeCommision() {
        return wifyeeCommision;
    }

    public void setWifyeeCommision(String wifyeeCommision) {
        this.wifyeeCommision = wifyeeCommision;
    }

    public String getDistCommision() {
        return distCommision;
    }

    public void setDistCommision(String distCommision) {
        this.distCommision = distCommision;
    }

    public FoodOderList(String itemID, String name, String price, String description,
                        String quantiy, String foodImage, String discountPrice, String category,
                        boolean checked,String wifyeeCommision,String distCommision) {
        this.itemID=itemID;
        this.name = name;
        this.price=price;
        this.description=description;
        this.quantiy=quantiy;
        this.foodImage=foodImage;
        this.checkvalue = checked;
        this.discountPrice = discountPrice;
        this.category = category;
        this.wifyeeCommision = wifyeeCommision;
        this.distCommision = distCommision;
    }
    public FoodOderList(){}

}

