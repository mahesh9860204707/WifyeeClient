package com.wifyee.greenfields.models.requests;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by amit on 6/29/2018.
 */

public class TravelRequest extends JSONObject implements Serializable
{
    private String fromDate;
    private String toDate;
    private String mobile;
    private String departureDate;
    private String people;
    private String budget;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }


}
