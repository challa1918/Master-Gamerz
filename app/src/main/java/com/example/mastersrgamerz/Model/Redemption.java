package com.example.mastersrgamerz.Model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Redemption implements Comparable<Redemption>{
    public String BHIM_ID, amount, email,PUBG_USERNAME,PUBGID,Date_of_req;

    public long seconds;
    public Redemption() {
    }


    @Override
    public int compareTo(Redemption o) {
            return this.Date_of_req.compareTo(o.Date_of_req);

    }
}
