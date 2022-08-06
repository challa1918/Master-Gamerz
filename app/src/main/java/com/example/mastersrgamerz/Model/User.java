package com.example.mastersrgamerz.Model;

public class User {
    public String email, Name, PUBG_ID, Balance, Kills, Wins, PUBG_USERNAME, BHIM_ID, Token_Id,block,photo_url;
    public int no_of_matches,winning_money,total_money_earned;

    public User() {
    }

    public User(String email, String name, String PUBG_ID, String balance, String kills, String wins, String PUBG_USERNAME, String BHIM_ID, String token_Id, String block, String photo_url, int no_of_matches, int winning_money, int total_money_earned) {
        this.email = email;
        Name = name;
        this.PUBG_ID = PUBG_ID;
        Balance = balance;
        Kills = kills;
        Wins = wins;
        this.PUBG_USERNAME = PUBG_USERNAME;
        this.BHIM_ID = BHIM_ID;
        Token_Id = token_Id;
        this.block = block;
        this.photo_url = photo_url;
        this.no_of_matches = no_of_matches;
        this.winning_money = winning_money;
        this.total_money_earned = total_money_earned;
    }
}
