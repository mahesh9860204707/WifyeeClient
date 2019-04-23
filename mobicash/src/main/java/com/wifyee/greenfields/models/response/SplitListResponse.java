package com.wifyee.greenfields.models.response;

import java.io.Serializable;

/**
 * Created by amit on 7/20/2017.
 */

public class SplitListResponse implements Serializable
{
    public String splitMoneyAmount;
    public String splitMoneyReferenceNumber;
    public String splitMoneyNumberOfParticipents;
    public String splitMoneyStatus;
    public String splitMoneyCreatedDate;

    public String getSplitMoneyId() {
        return splitMoneyId;
    }

    public void setSplitMoneyId(String splitMoneyId) {
        this.splitMoneyId = splitMoneyId;
    }

    public String splitMoneyId;

    public String getSplitMoneyAmount() {
        return splitMoneyAmount;
    }

    public void setSplitMoneyAmount(String splitMoneyAmount) {
        this.splitMoneyAmount = splitMoneyAmount;
    }

    public String getSplitMoneyReferenceNumber() {
        return splitMoneyReferenceNumber;
    }

    public void setSplitMoneyReferenceNumber(String splitMoneyReferenceNumber) {
        this.splitMoneyReferenceNumber = splitMoneyReferenceNumber;
    }

    public String getSplitMoneyNumberOfParticipents() {
        return splitMoneyNumberOfParticipents;
    }

    public void setSplitMoneyNumberOfParticipents(String splitMoneyNumberOfParticipents) {
        this.splitMoneyNumberOfParticipents = splitMoneyNumberOfParticipents;
    }

    public String getSplitMoneyStatus() {
        return splitMoneyStatus;
    }

    public void setSplitMoneyStatus(String splitMoneyStatus) {
        this.splitMoneyStatus = splitMoneyStatus;
    }

    public String getSplitMoneyCreatedDate() {
        return splitMoneyCreatedDate;
    }

    public void setSplitMoneyCreatedDate(String splitMoneyCreatedDate) {
        this.splitMoneyCreatedDate = splitMoneyCreatedDate;
    }


}
