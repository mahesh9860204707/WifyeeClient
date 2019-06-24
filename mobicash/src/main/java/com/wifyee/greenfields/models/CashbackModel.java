package com.wifyee.greenfields.models;

public class CashbackModel {
    private String OrderId,OrderAmount,CashbackAmt,OrderOn,CashbackStatus,CashbackCreditedDate;

    public CashbackModel(String orderId, String orderAmount, String cashbackAmt,
                         String orderOn, String cashbackStatus, String cashbackCreditedDate) {
        OrderId = orderId;
        OrderAmount = orderAmount;
        CashbackAmt = cashbackAmt;
        OrderOn = orderOn;
        CashbackStatus = cashbackStatus;
        CashbackCreditedDate = cashbackCreditedDate;
    }

    public String getOrderId() {
        return OrderId;
    }

    public String getOrderAmount() {
        return OrderAmount;
    }

    public String getCashbackAmt() {
        return CashbackAmt;
    }

    public String getOrderOn() {
        return OrderOn;
    }

    public String getCashbackStatus() {
        return CashbackStatus;
    }

    public String getCashbackCreditedDate() {
        return CashbackCreditedDate;
    }
}
