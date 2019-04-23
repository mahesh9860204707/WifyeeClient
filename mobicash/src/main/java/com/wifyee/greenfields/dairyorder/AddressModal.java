package com.wifyee.greenfields.dairyorder;

public class AddressModal {
    private String Id,Name,MobileNo,AlternateNo,City,Locality,FlatNo,Pincode,State;
    boolean isChecked;

    public AddressModal(String id, String name, String mobileNo, String alternateNo, String city, String locality,
                        String flatNo, String pincode, String state) {
        Id = id;
        Name = name;
        MobileNo = mobileNo;
        AlternateNo = alternateNo;
        City = city;
        Locality = locality;
        FlatNo = flatNo;
        Pincode = pincode;
        State = state;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public String getAlternateNo() {
        return AlternateNo;
    }

    public String getCity() {
        return City;
    }

    public String getLocality() {
        return Locality;
    }

    public String getFlatNo() {
        return FlatNo;
    }

    public String getPincode() {
        return Pincode;
    }

    public String getState() {
        return State;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
