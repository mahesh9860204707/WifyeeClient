package com.wifyee.greenfields.models.requests;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sumanta on 3/28/2017.
 */

public class BankTransferNeftImpsRequestModel implements Parcelable{

    private String userType;
    private String userTerminal;
    private String userId;
    private String userMobile;
    private String transferType;
    private String accountNo;
    private String IFSC;
    private String bankName;
    private String branchName;
    private String amount;
    private String firstName;
    private String lastName;
    private String beneficiaryMobile;
    private String beneficiaryMMID;

    protected BankTransferNeftImpsRequestModel(Parcel in) {
        userType = in.readString();
        userTerminal = in.readString();
        userId = in.readString();
        userMobile = in.readString();
        transferType = in.readString();
        accountNo = in.readString();
        IFSC = in.readString();
        bankName = in.readString();
        branchName = in.readString();
        amount = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        beneficiaryMobile = in.readString();
        beneficiaryMMID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userType);
        dest.writeString(userTerminal);
        dest.writeString(userId);
        dest.writeString(userMobile);
        dest.writeString(transferType);
        dest.writeString(accountNo);
        dest.writeString(IFSC);
        dest.writeString(bankName);
        dest.writeString(branchName);
        dest.writeString(amount);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(beneficiaryMobile);
        dest.writeString(beneficiaryMMID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BankTransferNeftImpsRequestModel> CREATOR = new Creator<BankTransferNeftImpsRequestModel>() {
        @Override
        public BankTransferNeftImpsRequestModel createFromParcel(Parcel in) {
            return new BankTransferNeftImpsRequestModel(in);
        }

        @Override
        public BankTransferNeftImpsRequestModel[] newArray(int size) {
            return new BankTransferNeftImpsRequestModel[size];
        }
    };

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserTerminal() {
        return userTerminal;
    }

    public void setUserTerminal(String userTerminal) {
        this.userTerminal = userTerminal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIFSC() {
        return IFSC;
    }

    public void setIFSC(String IFSC) {
        this.IFSC = IFSC;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBeneficiaryMobile() {
        return beneficiaryMobile;
    }

    public void setBeneficiaryMobile(String beneficiaryMobile) {
        this.beneficiaryMobile = beneficiaryMobile;
    }

    public String getBeneficiaryMMID() {
        return beneficiaryMMID;
    }

    public void setBeneficiaryMMID(String beneficiaryMMID) {
        this.beneficiaryMMID = beneficiaryMMID;
    }
}
