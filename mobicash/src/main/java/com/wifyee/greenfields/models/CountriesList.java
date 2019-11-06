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

public class CountriesList extends JSONObject implements Serializable, Parcelable {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("code3l")
    @Expose
    private String code3l;
    @SerializedName("code2l")
    @Expose
    private String code2l;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_official")
    @Expose
    private String nameOfficial;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("zoom")
    @Expose
    private String zoom;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("phone_code")
    @Expose
    private String phoneCode;
    @SerializedName("flag_32")
    @Expose
    private String flag32;
    @SerializedName("flag_128")
    @Expose
    private String flag128;

    protected CountriesList(Parcel in) {
        id = in.readString();
        code3l = in.readString();
        code2l = in.readString();
        name = in.readString();
        nameOfficial = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        zoom = in.readString();
        countryCode = in.readString();
        phoneCode = in.readString();
        flag32 = in.readString();
        flag128 = in.readString();
    }

    public static final Creator<CountriesList> CREATOR = new Creator<CountriesList>() {
        @Override
        public CountriesList createFromParcel(Parcel in) {
            return new CountriesList(in);
        }

        @Override
        public CountriesList[] newArray(int size) {
            return new CountriesList[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode3l() {
        return code3l;
    }

    public void setCode3l(String code3l) {
        this.code3l = code3l;
    }

    public String getCode2l() {
        return code2l;
    }

    public void setCode2l(String code2l) {
        this.code2l = code2l;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameOfficial() {
        return nameOfficial;
    }

    public void setNameOfficial(String nameOfficial) {
        this.nameOfficial = nameOfficial;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getFlag32() {
        return flag32;
    }

    public void setFlag32(String flag32) {
        this.flag32 = flag32;
    }

    public String getFlag128() {
        return flag128;
    }

    public void setFlag128(String flag128) {
        this.flag128 = flag128;
    }
       public CountriesList(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(code3l);
        dest.writeString(code2l);
        dest.writeString(name);
        dest.writeString(nameOfficial);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(zoom);
        dest.writeString(countryCode);
        dest.writeString(phoneCode);
        dest.writeString(flag32);
        dest.writeString(flag128);
    }
}

