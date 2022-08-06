package com.example.mastersrgamerz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class internet_receiver extends BroadcastReceiver {
    private getConnection connection;
    int value=0;
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        value=0;
        if(status.equals("0")) {
            status = "No internet connection";
            connection.getNoConnectionValue(status);
        }

        else
        {
            status="Online";
            connection.getYesConnectionValue(status);
        }

    }
        public internet_receiver(getConnection connection){
        this.connection=connection;
        }
   public interface getConnection{
        void getNoConnectionValue(String text);
        void getYesConnectionValue(String text);
   }
}

