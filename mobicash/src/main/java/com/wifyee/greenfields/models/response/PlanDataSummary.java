package com.wifyee.greenfields.models.response;

import java.io.Serializable;

/**
 * Created by amit on 7/25/2017.
 */

public class PlanDataSummary implements Serializable
{
    private String plan_name;
    private String time_used;
    private String data_download;
    private String data_remaining;
    private String data_total;
    private String time_remaining;
    private String time_total;

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getTime_used() {
        return time_used;
    }

    public void setTime_used(String time_used) {
        this.time_used = time_used;
    }

    public String getData_download() {
        return data_download;
    }

    public void setData_download(String data_download) {
        this.data_download = data_download;
    }

    public String getData_remaining() {
        return data_remaining;
    }

    public void setData_remaining(String data_remaining) {
        this.data_remaining = data_remaining;
    }

    public String getData_total() {
        return data_total;
    }

    public void setData_total(String data_total) {
        this.data_total = data_total;
    }

    public String getTime_remaining() {
        return time_remaining;
    }

    public void setTime_remaining(String time_remaining) {
        this.time_remaining = time_remaining;
    }

    public String getTime_total() {
        return time_total;
    }

    public void setTime_total(String time_total) {
        this.time_total = time_total;
    }


}
