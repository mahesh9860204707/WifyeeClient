package com.wifyee.greenfields.models.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sumanta on 3/19/2017.
 */

public class BankListResponseModel implements Parcelable {

    private String bankCode;
    private String bankName;


    public BankListResponseModel(){}

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(getBankCode());
        dest.writeString(getBankName());
    }

    private BankListResponseModel(Parcel in) {
        setBankCode(in.readString());
        setBankName(in.readString());
    }

    public static final Parcelable.Creator<BankListResponseModel> CREATOR = new Parcelable.Creator<BankListResponseModel>() {

        @Override
        public BankListResponseModel createFromParcel(Parcel source) {
            return new BankListResponseModel(source);
        }

        @Override
        public BankListResponseModel[] newArray(int size) {
            return new BankListResponseModel[size];
        }
    };
}
