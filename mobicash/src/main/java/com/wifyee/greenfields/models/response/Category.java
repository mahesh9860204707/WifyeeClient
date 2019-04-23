package com.wifyee.greenfields.models.response;

import java.io.Serializable;

/**
 * Created by amit on 12/4/2017.
 */

public class Category implements Serializable
{
    public String cat_id;
    public String cat_name;
    public String cat_slug;

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_slug() {
        return cat_slug;
    }

    public void setCat_slug(String cat_slug) {
        this.cat_slug = cat_slug;
    }


}
