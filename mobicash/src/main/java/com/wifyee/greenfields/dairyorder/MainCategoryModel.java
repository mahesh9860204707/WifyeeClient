package com.wifyee.greenfields.dairyorder;

import android.os.Parcel;
import android.os.Parcelable;

public class MainCategoryModel implements Parcelable {

    private String catId;
    private String catName;
    private String catImageUrl;

    public MainCategoryModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getCatId());
        dest.writeString(getCatName());
        dest.writeString(getCatImageUrl());
    }

    public MainCategoryModel(Parcel in) {
        this.catId = in.readString();
        this.catName = in.readString();
        this.catImageUrl = in.readString();
    }

    public static final Parcelable.Creator<MainCategoryModel> CREATOR = new Parcelable.Creator<MainCategoryModel>() {

        @Override
        public MainCategoryModel createFromParcel(Parcel source) {
            return new MainCategoryModel(source);
        }

        @Override
        public MainCategoryModel[] newArray(int size) {
            return new MainCategoryModel[size];
        }
    };

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImageUrl() {
        return catImageUrl;
    }

    public void setCatImageUrl(String catImageUrl) {
        this.catImageUrl = catImageUrl;
    }
}
