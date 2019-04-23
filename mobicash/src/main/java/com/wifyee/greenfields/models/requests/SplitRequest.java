package com.wifyee.greenfields.models.requests;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by amit on 7/19/2017.
 */


public class SplitRequest implements Serializable
{
    public String userId;
    public String referenceNumber;
    public String amount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNumberOfParticipents() {
        return numberOfParticipents;
    }

    public void setNumberOfParticipents(String numberOfParticipents) {
        this.numberOfParticipents = numberOfParticipents;
    }

    public ArrayList<Participent> getParticipents() {
        return participents;
    }

    public void setParticipents(ArrayList<Participent> participents) {
        this.participents = participents;
    }

    public String numberOfParticipents;
    public ArrayList<Participent> participents = null;
}
