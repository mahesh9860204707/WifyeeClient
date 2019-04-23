package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 12/9/2017.
 */

public class MenuByParentSlugResponse   extends JSONObject implements Serializable {

    public String status;
    public List<CategoryList> categoryList;
    public String msg;

}