package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 12/21/2017.
 */

public class ItemsDeatils extends JSONObject implements Serializable {
    public String Name;
    public String quantity;
    public String amount;

    public  ItemsDeatils()
    {

    }

    public String getId() {
        return Name;
    }

    public void setId(String id) {
        this.Name = Name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    public  ItemsDeatils(String Name,String quantity,String amount)
    {
        this.Name=Name;
        this.quantity=quantity;
        this.amount=amount;
    }


}
