package com.wifyee.greenfields.dairyorder;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaceOrderData implements Parcelable {

    private String itemId;
    private String quantity;
    private String amount;
    private String userId;
    private String orderPrice;
    private String orderDateTime;
    private String latitude;
    private String longitude;
    private String paymentMode;
    private String quantityUnit;
    private String itemName;
    private String itemType;
    private String itemImagePath;
    private String merchantId;
    private String calculatedAmt;
    private String itemDiscount;

    public PlaceOrderData() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getItemId());
        dest.writeString(getQuantity());
        dest.writeString(getAmount());
        dest.writeString(getUserId());
        dest.writeString(getOrderPrice());
        dest.writeString(getOrderDateTime());
        dest.writeString(getLatitude());
        dest.writeString(getLongitude());
        dest.writeString(getPaymentMode());
        dest.writeString(getQuantityUnit());
        dest.writeString(getItemName());
        dest.writeString(getItemType());
        dest.writeString(getItemImagePath());
        dest.writeString(getMerchantId());
        dest.writeString(getCalculatedAmt());
        dest.writeString(getItemDiscount());
        //dest.writeByte((byte) (this.isExceed ? 1 : 0));
    }

    /**
     * Retrieving Student data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    public PlaceOrderData(Parcel in){
        this.itemId = in.readString();
        this.quantity = in.readString();
        this.amount = in.readString();
        this.userId = in.readString();
        this.orderPrice = in.readString();
        this.orderDateTime = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.paymentMode = in.readString();
        this.quantityUnit = in.readString();
        this.itemName = in.readString();
        this.itemType = in.readString();
        this.itemImagePath = in.readString();
        this.merchantId = in.readString();
        this.calculatedAmt = in.readString();
        this.itemDiscount = in.readString();
        //this.isExceed = in.readByte() == 1;

    }

    public static final Parcelable.Creator<PlaceOrderData> CREATOR = new Parcelable.Creator<PlaceOrderData>() {

        @Override
        public PlaceOrderData createFromParcel(Parcel source) {
            return new PlaceOrderData(source);
        }

        @Override
        public PlaceOrderData[] newArray(int size) {
            return new PlaceOrderData[size];
        }
    };


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemImagePath() {
        return itemImagePath;
    }

    public void setItemImagePath(String itemImagePath) {
        this.itemImagePath = itemImagePath;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCalculatedAmt() {
        return calculatedAmt;
    }

    public void setCalculatedAmt(String calculatedAmt) {
        this.calculatedAmt = calculatedAmt;
    }

    public String getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(String itemDiscount) {
        this.itemDiscount = itemDiscount;
    }
}
