package com.example.mastersrgamerz;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mastersrgamerz.Adapter.UserPaymentsAdapter;
import com.example.mastersrgamerz.Model.redeemtransaction;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class fragment_transactiions extends Fragment {

    ProgressBar progressBar;
    ArrayList<redeemtransaction> transactionlist;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    TextView total,today;
    LottieAnimationView loading,nodata;
    ExtendedFloatingActionButton fab;
    Animation fabopen;
    float totalamount,todayamount;


    public fragment_transactiions() {
        transactionlist=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("User_Upi_Payments");
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_transactiions, container, false);
        loading=view.findViewById(R.id.tloading);
        today=view.findViewById(R.id.today);
        total=view.findViewById(R.id.total);
        fabopen= AnimationUtils.loadAnimation(getContext(),R.anim.amountfabanim);
        recyclerView=view.findViewById(R.id.userpayments);
        nodata=view.findViewById(R.id.transactionsnodatafound);
        LocalDate currentdate= LocalDate.now();

        System.out.println("Todays Date: "+currentdate);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                transactionlist.clear();
                for (DataSnapshot ds:
                     dataSnapshot.getChildren()) {
                    redeemtransaction transaction=ds.getValue(redeemtransaction.class);
                    String array[]=transaction.date.substring(0,11).split(" ");
                    int year=Integer.parseInt(array[2]);
                    int day=Integer.parseInt(array[0]);
                    int month;
                    //LocalDate transactiondate=LocalDate.parse(array[3],array);
                    switch (array[1]){
                        case "Jan": month=1; break;
                        case "Feb": month=2; break;
                        case "Mar": month=3; break;
                        case "Apr": month=4; break;
                        case "May": month=5; break;
                        case "Jun": month=6; break;
                        case "Jul": month=7; break;
                        case "Aug": month=8 ; break;
                        case "Sep": month=9 ; break;
                        case "Oct": month=10; break;
                        case "Nov": month=11; break;
                        case "Dec": month=12; break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + array[1]);
                    }
                    LocalDate transactiondate=LocalDate.of(year,month,day);
               if(currentdate.equals(transactiondate))
                   todayamount+=Float.parseFloat(transaction.money);
                    System.out.println("Todays Date: "+transactiondate);
                    assert transaction != null;
                    totalamount+=Float.parseFloat(transaction.money);
                    transactionlist.add(transaction);
                }
                Collections.reverse(transactionlist);
                UserPaymentsAdapter userPaymentsAdapter= new UserPaymentsAdapter(transactionlist,getContext());

                total.setText("    TOTAL : Rs "+(int)totalamount+"/-");
                today.setText("TODAY : Rs "+(int)todayamount+"/-");
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.setAdapter(userPaymentsAdapter);
                loading.setVisibility(View.INVISIBLE);
                if(transactionlist.size()==0)
                    nodata.setVisibility(View.VISIBLE);
                else
                    nodata.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Inflate the layout for this fragment
        return  view;
    }
}
