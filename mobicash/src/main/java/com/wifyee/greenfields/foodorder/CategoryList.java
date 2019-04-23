package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 12/9/2017.
 */

public class CategoryList extends JSONObject implements Serializable {

    public String categoryId;
    public String categoryName;
    public String categorySlug;

}