package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 12/12/2017.
 */

public class Items  extends JSONObject implements Serializable {
    public String id;
    public String quantity;
    public String amount;

    public  Items()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public  Items(String id,String quantity,String amount)
    {
        this.id=id;
        this.quantity=quantity;
        this.amount=amount;
    }


}
