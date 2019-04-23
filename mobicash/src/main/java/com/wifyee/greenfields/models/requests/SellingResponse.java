package com.wifyee.greenfields.models.requests;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by amit on 5/21/2018.
 */

public class SellingResponse implements Serializable
{
    private String itemId;
    private String itemName;
    private String itemPrice;
    private String discountType;
    private String discount;
    private String mobicash_commission;
    private String commission_type;
    private String quentity;
    private String status;
    private String createdDate;
    private String updateddDate;
    private ArrayList<ImageObjects> imageObj;

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

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getMobicash_commission() {
        return mobicash_commission;
    }

    public void setMobicash_commission(String mobicash_commission) {
        this.mobicash_commission = mobicash_commission;
    }

    public String getCommission_type() {
        return commission_type;
    }

    public void setCommission_type(String commission_type) {
        this.commission_type = commission_type;
    }

    public String getQuentity() {
        return quentity;
    }

    public void setQuentity(String quentity) {
        this.quentity = quentity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdateddDate() {
        return updateddDate;
    }

    public void setUpdateddDate(String updateddDate) {
        this.updateddDate = updateddDate;
    }

    public ArrayList<ImageObjects> getImageObj() {
        return imageObj;
    }

    public void setImageObj(ArrayList<ImageObjects> imageObj) {
        this.imageObj = imageObj;
    }
}
