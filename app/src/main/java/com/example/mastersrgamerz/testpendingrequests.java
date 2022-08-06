package com.example.mastersrgamerz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mastersrgamerz.Adapter.AccountRequestAdapter;
import com.example.mastersrgamerz.Model.RegisterData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class testpendingrequests extends AppCompatActivity {

    ImageView back;
    RecyclerView requestslist;
    LottieAnimationView nodatafound;
    TextView nodata;
    ArrayList<RegisterData> registerDataArrayList;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testpendingrequests);
        back=findViewById(R.id.acountrequestback);
        nodatafound=findViewById(R.id.nomoreaccountrequest);
        requestslist=findViewById(R.id.accountrequestslist);
        //      nodatafound=findViewById(R.id.testnodata);
        nodata=findViewById(R.id.nodatatext);
        //  nodatafound.playAnimation();
        registerDataArrayList= new ArrayList<>();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        db= FirebaseDatabase.getInstance().getReference("Users_Admin");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                registerDataArrayList.clear();
                for (DataSnapshot ds:
                        dataSnapshot.getChildren() ) {
                    RegisterData data=ds.getValue(RegisterData.class);
                    System.out.println("Name:    "+data.NAME);
                    if( data.verification.equals("Success"))
                        continue;
                    registerDataArrayList.add(data);

                }
                System.out.println("Pending Requests       :\n"+  registerDataArrayList);

                if(registerDataArrayList.size()==0){

                    nodatafound.setVisibility(View.VISIBLE);
                    nodata.setVisibility(View.VISIBLE);


                }
                else{
                    nodata.setVisibility(View.INVISIBLE);
                    nodatafound.setVisibility(View.INVISIBLE);
                }
                AccountRequestAdapter accountRequestAdapter = new AccountRequestAdapter(registerDataArrayList, testpendingrequests.this);
                requestslist.setHasFixedSize(true);
                StaggeredGridLayoutManager staggeredGridLayoutManager= new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                requestslist.setLayoutManager(staggeredGridLayoutManager);
                requestslist.setAdapter(accountRequestAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
