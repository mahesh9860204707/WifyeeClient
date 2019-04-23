package com.wifyee.greenfields.models.response;

import java.io.Serializable;

/**
 * Created by amit on 5/11/2018.
 */

public class OffersItem implements Serializable
{
    private String offer_id;
    private String offer_name;
    private String offer_type;
    private String offer_discount;
    private String valid_upto;
    private String merchant_id;
    private String status;

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public void setOffer_type(String offer_type) {
        this.offer_type = offer_type;
    }

    public String getOffer_discount() {
        return offer_discount;
    }

    public void setOffer_discount(String offer_discount) {
        this.offer_discount = offer_discount;
    }

    public String getValid_upto() {
        return valid_upto;
    }

    public void setValid_upto(String valid_upto) {
        this.valid_upto = valid_upto;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
