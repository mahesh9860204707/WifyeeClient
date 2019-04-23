package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 12/10/2017.
 */

public class VegORNonVegList extends JSONObject implements Serializable {

    public String name;
    public String category;
    public String price;
    public String description;
    public String merchantId;
    public String itemId;
    public String foodImage;
    public  boolean checkvalue;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public VegORNonVegList(String category, String merchantId, String name, String price, String description, String foodImage, boolean checked) {
        this.category=category;
        this.merchantId=merchantId;
        this.name = name;
        this.price=price;
        this.description=description;
        this.foodImage=foodImage;
        this.checkvalue = checked;
    }
    public VegORNonVegList(){}





}
