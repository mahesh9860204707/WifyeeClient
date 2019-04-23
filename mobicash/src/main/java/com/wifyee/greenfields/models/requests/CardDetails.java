package com.wifyee.greenfields.models.requests;

import java.io.Serializable;

/**
 * Created by amit on 8/17/2017.
 */

public class CardDetails implements Serializable {

    private String cardName;
    private String cardType;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }


}
