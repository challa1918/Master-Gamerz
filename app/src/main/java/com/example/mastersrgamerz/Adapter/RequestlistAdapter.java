package com.example.mastersrgamerz.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastersrgamerz.Model.Redemption;
import com.example.mastersrgamerz.Payment;
import com.example.mastersrgamerz.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class RequestlistAdapter extends RecyclerView.Adapter<RequestlistAdapter.ViewHolder> {

    View view;
    ArrayList<Redemption> redemptionArrayList;
    Context context;


    public RequestlistAdapter(ArrayList<Redemption> redemptionArrayList, Context context) {
        this.redemptionArrayList = redemptionArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reqlistitem, null);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Redemption redemption = redemptionArrayList.get(position);
        holder.reqname.setText(redemption.PUBG_USERNAME);
        holder.payButton.setText(redemption.amount+"/-");

      //  Timestamp timestamp= new Timestamp(new Date(redemption.Date_of_req));

       String Timeago= (String) DateUtils.getRelativeTimeSpanString(redemption.seconds*1000);
        //   Toast.makeText(context,Timeago,Toast.LENGTH_LONG).show();
        //System.out.println("TIme ago :"+ Timeago);
        holder.time.setText(Timeago);


    }



    @Override
    public int getItemCount() {
        System.out.println("RedemptionDatasize : " + redemptionArrayList.size());
          return  redemptionArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reqname,time;
        MaterialCardView cardView;
        TextView payButton;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            reqname = itemView.findViewById(R.id.reqplayer);

time=itemView.findViewById(R.id.time);
           payButton=itemView.findViewById(R.id.payButton);


        }
    }



}
