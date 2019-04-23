package com.wifyee.greenfields.models.requests;

import java.io.Serializable;

/**
 * Created by amit on 8/22/2017.
 */

public class RequestMoney implements Serializable
{
    private String clientsender;

    public String getClientsender() {
        return clientsender;
    }

    public void setClientsender(String clientsender) {
        this.clientsender = clientsender;
    }

    public String getClientreceiver() {
        return clientreceiver;
    }

    public void setClientreceiver(String clientreceiver) {
        this.clientreceiver = clientreceiver;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    private String clientreceiver;
    private String amount;
    private String hash;
}
