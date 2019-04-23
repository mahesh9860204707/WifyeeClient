package com.wifyee.greenfields.foodorder;



import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 12/20/2017.
 */

public class OrderStatusDetailList extends JSONObject implements Serializable {
    public String order_id;
    public String order_amount  ;
    public String order_on;
    public String order_status;
    public List<ItemsDeatils> orderStatusItemsDetailList;
    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public String getOrder_amount() {
        return order_amount;
    }
    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }
    public String getOrder_on() {
        return order_on;
    }
    public void setOrder_on(String order_on) {
        this.order_on = order_on;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }



}