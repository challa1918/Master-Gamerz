package com.example.mastersrgamerz.Model;

public class Match_details {
    String MatchId;
    String Date;
    String Time;
    String FPrize;
    String PerKill;
    String EntryFee;
    String Type;
    String Version;
    String Map;
    String RoomId;
    String Password;

    public Match_details() {

    }

    public Match_details(String matchId, String date, String time, String FPrize, String perKill, String entryFee, String type, String version, String map) {
        MatchId = matchId;
        Date = date;
        Time = time;
        this.FPrize = FPrize;
        PerKill = perKill;
        EntryFee = entryFee;
        Type = type;
        Version = version;
        Map = map;
        RoomId="id";
        Password="password";
    }

    public String getMatchId() {
        return MatchId;
    }

    public void setMatchId(String matchId) {
        MatchId = matchId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getFPrize() {
        return FPrize;
    }

    public void setFPrize(String FPrize) {
        this.FPrize = FPrize;
    }

    public String getPerKill() {
        return PerKill;
    }

    public void setPerKill(String perKill) {
        PerKill = perKill;
    }

    public String getEntryFee() {
        return EntryFee;
    }

    public void setEntryFee(String entryFee) {
        EntryFee = entryFee;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getMap() {
        return Map;
    }

    public void setMap(String map) {
        Map = map;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
