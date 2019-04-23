package com.wifyee.greenfields.models.response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by amit on 12/4/2017.
 */

public class MenuSubCategory implements Serializable
{
    public String status;
    public ArrayList<Category> category;
    public String msg;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Category> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<Category> category) {
        this.category = category;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



}
