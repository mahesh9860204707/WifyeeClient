package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by amit on 6/18/2018.
 */

public class MerchantListBean extends JSONObject implements Serializable
{
    public String mer_id;
    public String mer_name;
    public String mer_company;
    public String mer_type;
    public String merchant_profile_image;
    public String idt_id;
    public String idt_name;
    public String idt_role;
    public String idt_email;
    public String idt_address;
    public String idt_city;
    public String idt_contactphone;

    public String getMer_id() {
        return mer_id;
    }

    public void setMer_id(String mer_id) {
        this.mer_id = mer_id;
    }

    public String getMer_name() {
        return mer_name;
    }

    public void setMer_name(String mer_name) {
        this.mer_name = mer_name;
    }

    public String getMer_company() {
        return mer_company;
    }

    public void setMer_company(String mer_company) {
        this.mer_company = mer_company;
    }

    public String getMer_type() {
        return mer_type;
    }

    public void setMer_type(String mer_type) {
        this.mer_type = mer_type;
    }

    public String getMerchant_profile_image() {
        return merchant_profile_image;
    }

    public void setMerchant_profile_image(String merchant_profile_image) {
        this.merchant_profile_image = merchant_profile_image;
    }

    public String getIdt_id() {
        return idt_id;
    }

    public void setIdt_id(String idt_id) {
        this.idt_id = idt_id;
    }

    public String getIdt_name() {
        return idt_name;
    }

    public void setIdt_name(String idt_name) {
        this.idt_name = idt_name;
    }

    public String getIdt_role() {
        return idt_role;
    }

    public void setIdt_role(String idt_role) {
        this.idt_role = idt_role;
    }

    public String getIdt_email() {
        return idt_email;
    }

    public void setIdt_email(String idt_email) {
        this.idt_email = idt_email;
    }

    public String getIdt_address() {
        return idt_address;
    }

    public void setIdt_address(String idt_address) {
        this.idt_address = idt_address;
    }

    public String getIdt_city() {
        return idt_city;
    }

    public void setIdt_city(String idt_city) {
        this.idt_city = idt_city;
    }

    public String getIdt_contactphone() {
        return idt_contactphone;
    }

    public void setIdt_contactphone(String idt_contactphone) {
        this.idt_contactphone = idt_contactphone;
    }


}
