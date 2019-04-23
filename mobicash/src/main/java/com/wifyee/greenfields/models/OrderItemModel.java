package com.wifyee.greenfields.models;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amit on 1/23/2018.
 */

public class OrderItemModel {
    public String ItemName, ItemQuantity, ItemPrice;

    public OrderItemModel(String itemName, String itemQuantity, String itemPrice) {
        ItemName = itemName;
        ItemQuantity = itemQuantity;
        ItemPrice = itemPrice;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getItemQuantity() {
        return ItemQuantity;
    }

    public String getItemPrice() {
        return ItemPrice;
    }
}
