package com.example.mastersrgamerz.Model;

public class Players {
    public String PUBG_USERNAME, email, Balance, UID, Token_Id,PUBG_ID;
    public boolean rewarded;

    public Players(String PUBG_USERNAME, String email, String UID, String token_Id, String PUBG_ID, boolean rewarded) {
        this.PUBG_USERNAME = PUBG_USERNAME;
        this.email = email;

        this.UID = UID;
        Token_Id = token_Id;
        this.PUBG_ID = PUBG_ID;
        this.rewarded = rewarded;
    }

    Players() {
    }

}
