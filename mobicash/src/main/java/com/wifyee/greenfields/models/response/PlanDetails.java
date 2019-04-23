package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by amit on 6/12/2017.
 */
public class PlanDetails extends JSONObject implements Serializable {

    public String planId;
    public static int COUNTRY_TYPE=1;
    public static int CITY_TYPE=0;
    public String planName;
    public String planCost;

    public String getPlanTimeBank() {
        return planTimeBank;
    }

    public void setPlanTimeBank(String planTimeBank) {
        this.planTimeBank = planTimeBank;
    }

    public String getPlanTrafficTotal() {
        return planTrafficTotal;
    }

    public void setPlanTrafficTotal(String planTrafficTotal) {
        this.planTrafficTotal = planTrafficTotal;
    }

    public String planTimeBank;
    public String planTrafficTotal;


    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }



    public String getPlanCost() {
        return planCost;
    }

    public void setPlanCost(String planCost) {
        this.planCost = planCost;
    }






}
