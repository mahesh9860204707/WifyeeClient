package com.wifyee.greenfields.dairyorder;

import android.os.Parcel;
import android.os.Parcelable;

public class DairyProductListItem implements Parcelable {

//    private Integer status;
//    private String message;
    private String merchantId;
    private String itemId;
    private String itemName;
    private String itemPrice;
    private String itemType;
    private String itemQuality;
    private String itemDiscountType;
    private String itemDiscount;
    private String itemMobCommission;
    private String itemCommissionType;
    private String itemQuantity;
    private String itemStatus;
    private String itemCreatedDate;
    private String itemUpdatedDate;
    private String itemImageId;
    private String itemImagePath;
    private String itemUnit;
    private String itemUnitQty;
    private String currentStatus;

    public DairyProductListItem() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getMerchantId());
        dest.writeString(getItemId());
        dest.writeString(getItemName());
        dest.writeString(getItemPrice());
        dest.writeString(getItemType());
        dest.writeString(getItemQuality());
        dest.writeString(getItemDiscountType());
        dest.writeString(getItemDiscount());
        dest.writeString(getItemMobCommission());
        dest.writeString(getItemCommissionType());
        dest.writeString(getItemQuantity());
        dest.writeString(getItemStatus());
        dest.writeString(getItemCreatedDate());
        dest.writeString(getItemUpdatedDate());
        dest.writeString(getItemImageId());
        dest.writeString(getItemImagePath());
        dest.writeString(getItemUnit());
        dest.writeString(getItemUnitQty());
        dest.writeString(getCurrentStatus());
    }

    /**
     * Retrieving Student data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    public DairyProductListItem(Parcel in){
        this.merchantId = in.readString();
        this.itemId = in.readString();
        this.itemName = in.readString();
        this.itemPrice = in.readString();
        this.itemType = in.readString();
        this.itemQuality = in.readString();
        this.itemDiscountType = in.readString();
        this.itemDiscount = in.readString();
        this.itemMobCommission = in.readString();
        this.itemCommissionType = in.readString();
        this.itemQuality = in.readString();
        this.itemStatus = in.readString();
        this.itemCreatedDate = in.readString();
        this.itemUpdatedDate = in.readString();
        this.itemImageId = in.readString();
        this.itemImagePath = in.readString();
        this.itemUnit = in.readString();
        this.itemUnitQty = in.readString();
        this.currentStatus = in.readString();
    }

    public static final Parcelable.Creator<DairyMerchantListModel> CREATOR = new Parcelable.Creator<DairyMerchantListModel>() {

        @Override
        public DairyMerchantListModel createFromParcel(Parcel source) {
            return new DairyMerchantListModel(source);
        }

        @Override
        public DairyMerchantListModel[] newArray(int size) {
            return new DairyMerchantListModel[size];
        }
    };

//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemQuality() {
        return itemQuality;
    }

    public void setItemQuality(String itemQuality) {
        this.itemQuality = itemQuality;
    }

    public String getItemDiscountType() {
        return itemDiscountType;
    }

    public void setItemDiscountType(String itemDiscountType) {
        this.itemDiscountType = itemDiscountType;
    }

    public String getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(String itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public String getItemMobCommission() {
        return itemMobCommission;
    }

    public void setItemMobCommission(String itemMobCommission) {
        this.itemMobCommission = itemMobCommission;
    }

    public String getItemCommissionType() {
        return itemCommissionType;
    }

    public void setItemCommissionType(String itemCommissionType) {
        this.itemCommissionType = itemCommissionType;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getItemCreatedDate() {
        return itemCreatedDate;
    }

    public void setItemCreatedDate(String itemCreatedDate) {
        this.itemCreatedDate = itemCreatedDate;
    }

    public String getItemUpdatedDate() {
        return itemUpdatedDate;
    }

    public void setItemUpdatedDate(String itemUpdatedDate) {
        this.itemUpdatedDate = itemUpdatedDate;
    }

    public String getItemImageId() {
        return itemImageId;
    }

    public void setItemImageId(String itemImageId) {
        this.itemImageId = itemImageId;
    }

    public String getItemImagePath() {
        return itemImagePath;
    }

    public void setItemImagePath(String itemImagePath) {
        this.itemImagePath = itemImagePath;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getItemUnitQty() {
        return itemUnitQty;
    }

    public void setItemUnitQty(String itemUnitQty) {
        this.itemUnitQty = itemUnitQty;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}


/**
 {
 "itemId": "6",
 "itemName": "milkq",
 "itemPrice": "120.00",
 "itemtype": "milk",
 "quality": "puremilk",
 "discountType": "",
 "discount": "10.00",
 "mobicash_commission": "10.00",
 "commission_type": "qw",
 "quentity": "0",
 "status": "Active",
 "createdDate": "2019-02-11 12:31:29",
 "updateddDate": "0000-00-00 00:00:00",
 "imageObj": [
 {
 "imageId": "8",
 "imagePath": "http://wifyeepay.com/user-selling-items/1549868490.jpg"
 }
 ]
 },
*/