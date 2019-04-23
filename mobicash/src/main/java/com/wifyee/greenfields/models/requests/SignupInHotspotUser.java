package com.wifyee.greenfields.models.requests;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by amit on 12/8/2017.
 */

public class SignupInHotspotUser implements Serializable
{

    @SerializedName("req_firstname")
    public String firstName;

    @SerializedName("req_lastname")
    public String lastName;

    @SerializedName("req_username")
    public String userName;

    @SerializedName("req_password")
    public int password;

    @SerializedName("req_repassword")
    public int repassword;

    @SerializedName("req_planid")
    public int planid;

    @SerializedName("req_locationid")
    public int locationid;

    @SerializedName("req_customercode")
    public String customercode;

    @SerializedName("req_action")
    public String action;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getRepassword() {
        return repassword;
    }

    public void setRepassword(int repassword) {
        this.repassword = repassword;
    }

    public int getPlanid() {
        return planid;
    }

    public void setPlanid(int planid) {
        this.planid = planid;
    }

    public int getLocationid() {
        return locationid;
    }

    public void setLocationid(int locationid) {
        this.locationid = locationid;
    }

    public String getCustomercode() {
        return customercode;
    }

    public void setCustomercode(String customercode) {
        this.customercode = customercode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }




}
