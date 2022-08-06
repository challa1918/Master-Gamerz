package com.example.mastersrgamerz.ui.gallery;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mastersrgamerz.Adapter.RedeemHistoryAdapter;
import com.example.mastersrgamerz.Model.redeemtransaction;
import com.example.mastersrgamerz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class redeemhistory extends Fragment {

    ArrayList<redeemtransaction> transactionlist;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    LottieAnimationView loading,nodatafound;
    TextView nodatatext,totalredeemed;
    float redtotal;


    redeemhistory(){
            transactionlist=new ArrayList<>();
            databaseReference=FirebaseDatabase.getInstance().getReference("Admin_Redeem_Transactions");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.redeemhistory,null);
        recyclerView=view.findViewById(R.id.historylist);
     loading=view.findViewById(  R.id.rhloading);
     totalredeemed=view.findViewById(R.id.redtotal);
nodatafound=view.findViewById(R.id.redeemhistorynodatafound);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                transactionlist.clear();
                for (DataSnapshot ds:
                     dataSnapshot.getChildren()) {
                    redeemtransaction transaction=ds.getValue(redeemtransaction.class);
                    System.out.println("Transaction:   "+transaction.from);
                    redtotal+=Float.parseFloat(transaction.money);
                    transactionlist.add(transaction);
                }
                Collections.reverse(transactionlist);
                totalredeemed.setText(" Total redeemed amount : "+ (int)redtotal);
                RedeemHistoryAdapter redeemHistoryAdapter= new RedeemHistoryAdapter(transactionlist,view.getContext());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.setAdapter(redeemHistoryAdapter);
                loading.setVisibility(View.INVISIBLE);
                if(transactionlist.size()==0)
                    nodatafound.setVisibility(View.VISIBLE);
                else
                    nodatafound.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }
}
