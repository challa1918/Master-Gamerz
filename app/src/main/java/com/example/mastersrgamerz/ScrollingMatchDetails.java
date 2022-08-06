package com.example.mastersrgamerz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.mastersrgamerz.Adapter.PlayersJoinedAdapter;
import com.example.mastersrgamerz.Model.Match_details;
import com.example.mastersrgamerz.Model.Players;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class ScrollingMatchDetails extends AppCompatActivity {
    ArrayList<Players> listplayers;
    DatabaseReference reference;
    PlayersJoinedAdapter playersJoinedAdapter;
    RecyclerView recyclerView;
    DatabaseReference matchref;
    ImageView back;
CollapsingToolbarLayout screen;
    View pbutton;
    String map;
    int c;


    TextView count,match,disfprize,disperkill,disfee;
    ProgressBar countprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_match_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        back=findViewById(R.id.playersback);


        //    getWindow().setExitTransition(new Slide(Gravity.BOTTOM));
        reference = FirebaseDatabase.getInstance().getReference("Matches_Going");
        recyclerView = findViewById(R.id.joinedplayersview);
        listplayers = new ArrayList<>();
        count=findViewById(R.id.count);
        screen=findViewById(R.id.screen);
        setScreenImage();
        pbutton=findViewById(R.id.myprogressbutton);
        countprogress=findViewById(R.id.countprogress);
        final Intent i = getIntent();
        match=findViewById(R.id.match);
        disfprize=findViewById(R.id.disfprize);
        disperkill=findViewById(R.id.disperkill);
        disfee=findViewById(R.id.disfee);
        final String mid, mwin, mtype, mperkill,time,date;
        mid = i.getStringExtra("MatchId");
        mwin = i.getStringExtra("fprize");
        mtype = i.getStringExtra("type");
        mperkill = i.getStringExtra("perkill");
        time=i.getStringExtra("time");
        date=i.getStringExtra("date");
        map=i.getStringExtra("map");
        matchref=FirebaseDatabase.getInstance().getReference("Matches").child(mid);
        matchref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Match_details match_details= dataSnapshot.getValue(Match_details.class);
                match.setText("  Match   "+match_details.getMatchId());
                disfee.setText(match_details.getEntryFee()+"/-");
                disfprize.setText(match_details.getFPrize()+"/-");
                disperkill.setText(match_details.getPerKill()+"/-");
                if(!match_details.getMap().equals("TDM"))
                    countprogress.setMax(100);
                else
                    countprogress.setMax(8);
                count.setText(String.valueOf(c)+ "/"+countprogress.getMax());
               // Toast.makeText(getApplicationContext(),"MAtchdertailsUpdated",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final ProgressButton progressButton= new ProgressButton(ScrollingMatchDetails.this,pbutton);
        progressButton.buttonActivated();



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        reference.child(i.getStringExtra("MatchId")).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listplayers.clear();
                for (DataSnapshot ds :
                        dataSnapshot.getChildren()) {
                    Players player = ds.getValue(Players.class);
                    listplayers.add(player);


                }
                System.out.println(listplayers);

               c=listplayers.size();


                countprogress.setProgress(c);
                if(c<=50) {
                    count.setTextColor(ColorStateList.valueOf(Color.parseColor("#69BA13")));

                }
                else if(c>50&&c<90) {
                    count.setTextColor(ColorStateList.valueOf(Color.parseColor("#FDD835")));

                }else
                    count.setTextColor(ColorStateList.valueOf(Color.parseColor("#F30D08")));
                countprogress.setVisibility(View.VISIBLE);
                count.setText(String.valueOf(c)+ "/"+countprogress.getMax());


                playersJoinedAdapter = new PlayersJoinedAdapter(listplayers, ScrollingMatchDetails.this, mid, mwin, mtype, mperkill,time,date);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(ScrollingMatchDetails.this));

                playersJoinedAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(playersJoinedAdapter);
                progressButton.buttonFinished();
                recyclerView.setVisibility(View.VISIBLE);
                pbutton.setEnabled(false);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setScreenImage() {



        Random random=new Random();

        int rand = random.nextInt(12);
        System.out.println("Random NUmber"+rand);
        int array[]={R.mipmap.pone,R.mipmap.ptwo,R.mipmap.pthree,R.mipmap.pfour,R.mipmap.pfive,R.mipmap.psix,R.mipmap.pseven,R.mipmap.peight,R.mipmap.pten
                ,R.mipmap.peleven,R.mipmap.ptwelve,R.mipmap.pthirteen};
        //  Toast.makeText(getApplicationContext(),rand,Toast.LENGTH_LONG).show();
        screen.setBackgroundResource(array[rand]);

    }


}