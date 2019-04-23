package com.wifyee.greenfields.models.response;


import org.json.JSONObject;

import java.io.Serializable;

public class BankTransferResponse extends JSONObject implements Serializable {

    public String bankTransferTransactionDate;
    public String bankTransferType;
    public String bankTransferAccountNo;
    public String bankTransferIFSC;
    public String bankTransferBankName;
    public String bankTransferBranchName;
    public String bankTransferAmount;
    public String bankTransferFees;
    public String bankTransferFirstName;
    public String bankTransferLastName;
    public String bankTransferBeneficiaryMobile;
    public String bankTransferStatus;
    public String bankTransferReferenceId;
    public String bankTransferTransactionId;
    public String bankTransferDescription;
}