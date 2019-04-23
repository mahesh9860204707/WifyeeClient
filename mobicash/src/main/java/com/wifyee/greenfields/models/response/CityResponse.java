package com.wifyee.greenfields.models.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by amit on 12/21/2017.
 */

public class CityResponse implements Parcelable
{
    private String id;
    private String name;

    public CityResponse()
    {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(getId());
        dest.writeString(getName());
    }

    private CityResponse(Parcel in) {
        setId(in.readString());
        setName(in.readString());
    }

    public static final Parcelable.Creator<CityResponse> CREATOR = new Parcelable.Creator<CityResponse>() {

        @Override
        public CityResponse createFromParcel(Parcel source) {
            return new CityResponse(source);
        }

        @Override
        public CityResponse[] newArray(int size) {
            return new CityResponse[size];
        }
    };
}
