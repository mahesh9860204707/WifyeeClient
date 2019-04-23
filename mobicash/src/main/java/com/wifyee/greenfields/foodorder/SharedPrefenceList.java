package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 12/10/2017.
 */

public class SharedPrefenceList extends JSONObject implements Serializable {
    public String name;
    public String price;
    public String description;
    public String quantiy;
    public String foodImage;
    public String itemId;

    public String getMercantId() {
        return mercantId;
    }

    public void setMercantId(String mercantId) {
        this.mercantId = mercantId;
    }

    public String mercantId;

    public SharedPrefenceList(String name, String price, String description,  String foodImage,String merchant_id) {

        this.name = name;
        this.price=price;
        this.description=description;
        this.foodImage=foodImage;
        this.mercantId=merchant_id;

    }
public SharedPrefenceList()
{}
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
