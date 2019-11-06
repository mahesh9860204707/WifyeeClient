package com.wifyee.greenfields.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 12/6/2017.
 */

public class MerchantCategoryList extends JSONObject implements Serializable, Parcelable {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("item_category")
    @Expose
    private String item_category;
    @SerializedName("image")
    @Expose
    private String image;

    protected MerchantCategoryList(Parcel in) {
        id = in.readString();
        item_category = in.readString();
        image = in.readString();

    }

    public static final Creator<MerchantCategoryList> CREATOR = new Creator<MerchantCategoryList>() {
        @Override
        public MerchantCategoryList createFromParcel(Parcel in) {
            return new MerchantCategoryList(in);
        }

        @Override
        public MerchantCategoryList[] newArray(int size) {
            return new MerchantCategoryList[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getitem_category() {
        return item_category;
    }

    public void setitem_category(String item_category) {
        this.item_category = item_category;
    }

    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
    }


       public MerchantCategoryList(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(item_category);
        dest.writeString(image);

    }
}

