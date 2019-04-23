package com.wifyee.greenfields.dairyorder;

import android.os.Parcel;
import android.os.Parcelable;

public class AddOrderModel implements Parcelable {

    private  String orderId;
    private  String items;
    private  String item_id ;
    private  String quantity;
    private  String amount;
    private  String userId;
    private  String userType;
    private  String orderPrice;
    private  String orderDateTime;
    private  String lat;
    private  String longg;
    private  String payment_mode;

    public AddOrderModel() {}

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getOrderId());
        dest.writeString(getItems());dest.writeString(getItem_id());
        dest.writeString(getQuantity()); dest.writeString(getAmount()); dest.writeString(getUserId());
        dest.writeString(getUserType());  dest.writeString(getOrderPrice());dest.writeString(getOrderDateTime());
        dest.writeString(getLat());
        dest.writeString(getLongg());
        dest.writeString(getPayment_mode());
    }


    /**
     * Retrieving Student data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    public AddOrderModel(Parcel in){
        this.orderId = in.readString();
        this.items = in.readString();
        this.item_id = in.readString();
        this.quantity = in.readString();
        this.amount = in.readString();
        this.userId = in.readString();
        this.userType = in.readString();
        this.orderPrice = in.readString();
        this.orderDateTime = in.readString();
        this.lat = in.readString();
        this.longg = in.readString();
        this.payment_mode = in.readString();
    }

    public static final Parcelable.Creator<AddOrderModel> CREATOR = new Parcelable.Creator<AddOrderModel>() {

        @Override
        public AddOrderModel createFromParcel(Parcel source) {
            return new AddOrderModel(source);
        }

        @Override
        public AddOrderModel[] newArray(int size) {
            return new AddOrderModel[size];
        }
    };



    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongg() {
        return longg;
    }

    public void setLongg(String longg) {
        this.longg = longg;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }
}
