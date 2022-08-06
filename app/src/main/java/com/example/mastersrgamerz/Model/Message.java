package com.example.mastersrgamerz.Model;

public class Message {
    public String  title,message,Date;
    public Boolean seen;

    public Message(String title, String message, String date, Boolean seen) {
        this.title = title;
        this.message = message;
        Date = date;
        this.seen = seen;
    }
}
