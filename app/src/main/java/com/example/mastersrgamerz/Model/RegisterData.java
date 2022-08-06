package com.example.mastersrgamerz.Model;

public class RegisterData {
 public   String NAME;
    public String EMAIL;
    public String UID;
    public String tokenid;
    public String verification;
    public String block;
    public String profilepic;
    public String phnno;
    public int appdemostatus,redeemdem;

    public RegisterData() {

    }

    public RegisterData(String NAME, String EMAIL, String uid, String tokenid,String profilepic) {
        this.NAME = NAME;
        this.EMAIL = EMAIL;
        this.tokenid = tokenid;
        this.UID = uid;
        verification="pending";
        block="false";
        this.profilepic=profilepic;
        phnno="";
    }

}
