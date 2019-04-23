package com.wifyee.greenfields.models;

/**
 * Created by amit on 1/23/2018.
 */

public class ItemQtyModel {
    public String ItemId, ItemQuantity;

    public ItemQtyModel(String itemId, String itemQuantity) {
        ItemId = itemId;
        ItemQuantity = itemQuantity;
    }

    public String getItemId() {
        return ItemId;
    }

    public String getItemQuantity() {
        return ItemQuantity;
    }
}
