package com.wifyee.greenfields.models.response.operators;

import org.json.JSONObject;

import java.io.Serializable;

public class OperatorList extends JSONObject implements Serializable {

    public Airtime airtimeList;
    public ElectricityOrGas electricityGasList;
    public ICashCard icashcardList;
    public MoneyTransfer moneyTransferList;
}