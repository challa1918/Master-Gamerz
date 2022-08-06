package com.example.mastersrgamerz;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import com.airbnb.lottie.L;
import com.example.mastersrgamerz.Model.Match_details;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.text.InputType;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FinalMatchDetails extends AppCompatActivity implements MyTimePicker.TimepickerListener {



    TextInputLayout MatchId,mfprize,mperkill,mfee;
Spinner mmap;
    RadioGroup type,version;
    TextView display,timeview;
    TextView matchid;
    ArrayList<String>radiolist;
    String selectedmap="none";
    String maplist[];
    Button mmcalendar,mmtime;
    DatabaseReference mapsref;
    CollapsingToolbarLayout collapsingToolbarLayout;
int mid;

    ImageView back;

    RadioButton solo,duo,squad,tpp,fpp;
    Button create;
    DatabaseReference databaseReference,matchidreference;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_match_detais);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setExitTransition(new Slide(Gravity.BOTTOM));
        mmcalendar=findViewById(R.id.finalcalendar);
        mmtime=findViewById(R.id.finaltime);
        MatchId=findViewById(R.id.finalmid);
        matchid=findViewById(R.id.matchid);
        collapsingToolbarLayout=findViewById(R.id.toolbar_layout);
setScreenImage();
      mapsref=FirebaseDatabase.getInstance().getReference("MAPS");





        back=findViewById(R.id.finalback);

        type=findViewById(R.id.finaltype);
        version=findViewById(R.id.finalversion);

        display=findViewById(R.id.finaldateview);
        solo=findViewById(R.id.finalsolo);
        duo=findViewById(R.id.finalduo);
        squad=findViewById(R.id.finalsquad);
        tpp=findViewById(R.id.finaltpp);
        fpp=findViewById(R.id.finalfpp);
        timeview=findViewById(R.id.finaltimeview);


        databaseReference= FirebaseDatabase.getInstance().getReference("Matches");
matchidreference=FirebaseDatabase.getInstance().getReference("MATCH_ID");
matchidreference.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    //    Toast.makeText(getApplicationContext(),dataSnapshot.getValue().toString(),Toast.LENGTH_LONG).show();
        matchid.setText(Objects.requireNonNull(dataSnapshot.getValue()).toString());
        MatchId.getEditText().setText(matchid.getText());
        MatchId.getEditText().setEnabled(false);
        mid=Integer.parseInt(MatchId.getEditText().getText().toString());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});





        create=findViewById(R.id.finalcreate);


        mfprize=findViewById(R.id.finalfprize);
        mfee=findViewById(R.id.finalfee);
        mperkill=findViewById(R.id.finalperkill);
        mmap=findViewById(R.id.finalspinner);
        final ArrayList<String>maps=new ArrayList<>();


mapsref.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        maps.clear();

        for (DataSnapshot ds:
             dataSnapshot.getChildren()) {
            System.out.println(ds.getValue());
            maps.add((String) ds.getValue());

        }

        maplist=new String[maps.size()];
        int i=0;
        for (String s:
                maps) {
            maplist[i]=s;
            i++;
        }
        maps.add(0,"Select a map");

        maps.add("add another map");
        maps.add("remove a map");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                FinalMatchDetails.this,
                android.R.layout.simple_spinner_dropdown_item,
                maps
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);

        mmap.setAdapter(adapter);

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
mmap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(maps.get(position));
        if(maps.get(position).equalsIgnoreCase("TDM")) {
            type.check(squad.getId());
            mperkill.getEditText().setText(String.valueOf(0));
        }
        else
            type.clearCheck();
        if(maps.get(position).equals("add another map")){

            AlertDialog dialog;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(FinalMatchDetails.this);
            alertDialog.setTitle("Add New Map");
            final EditText input = new EditText(FinalMatchDetails.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setSelectAllOnFocus(true);
            alertDialog.setView(input);
            alertDialog.setIcon(R.drawable.ic_add_circle_black_24dp);
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(!input.getText().toString().equalsIgnoreCase(""))
                    mapsref.child(input.getText().toString().toUpperCase()).setValue(input.getText().toString().toUpperCase());
                    else
                        Toast.makeText(getApplicationContext(),"No map added",Toast.LENGTH_LONG).show();
                    mmap.setSelection(0);
                    dialog.cancel();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                        mmap.setSelection(0);
                }
            });
            dialog = alertDialog.create();
            dialog.setCancelable( false );
            dialog.show();

        }




        if(maps.get(position).equals("remove a map")){
            AlertDialog mapdialog;
            AlertDialog.Builder alert = new AlertDialog.Builder(FinalMatchDetails.this);
            alert.setIcon(R.drawable.remove);

            alert.setTitle("Remove a map");
            alert.setSingleChoiceItems(maplist, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedmap=maplist[which];
                }
            });
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(selectedmap.equalsIgnoreCase("none")) {
                        Toast.makeText(getApplicationContext(), "No map selected", Toast.LENGTH_LONG).show();
                    }
                    else {
                        mapsref.child(selectedmap).removeValue();

                        Toast.makeText(getApplicationContext(), selectedmap+" successfully removed..", Toast.LENGTH_LONG).show();
                    }
                    mmap.setSelection(0);
                }
            });
            alert.setCancelable(false);

            mapdialog=alert.create();
            mapdialog.show();

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});
        // Create an ArrayAdapter using the string array and a default spinner layout

// Apply the adapter to the spinner



        Calendar calendar=Calendar.getInstance();
        final int year,date,month;
        year=calendar.get(Calendar.YEAR);
        date=calendar.get(Calendar.DATE);
        month=calendar.get(Calendar.MONTH);


        mmtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTimePicker mytimepicker=new MyTimePicker();
                mytimepicker.setCancelable(false);
                mytimepicker.show(getSupportFragmentManager(),"TimePicker");
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mmcalendar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(FinalMatchDetails.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String day=String.valueOf(dayOfMonth);
                        String monthfinal=String.valueOf(month+1);

                        if(dayOfMonth<10){
                            day="0"+day;
                        }

                        if(month<10){
                            monthfinal="0"+monthfinal;
                        }

                        display.setText(day+"-"+monthfinal+"-"+year);
                        //display.setVisibility(View.VISIBLE);
                    }
                },year,month,date);
                datePickerDialog.show();
            }
        });





        create.setOnClickListener(new View.OnClickListener() {


            @SuppressLint("ResourceType")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if (MatchId.getEditText().getText().toString().equals("")||mfprize.getEditText().getText().toString().equals("")||mfee.getEditText().getText().toString().equals("")||

                        display.getText().toString().trim().equals("Date")||mperkill.getEditText().getText().toString().equals("")||timeview.getText().toString().trim().equalsIgnoreCase("Time")
                        ||mmap.getSelectedItem().toString().equals("Select a map")||type.getCheckedRadioButtonId()<0|| (version.getCheckedRadioButtonId() <0)) {
                   Toast.makeText(FinalMatchDetails.this, "Fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    String types = "";
                    switch (type.getCheckedRadioButtonId()) {
                        case R.id.finalsolo:
                            types = "SOLO";
                            break;
                        case R.id.finalduo:
                            types = "DUO";
                            break;
                        case R.id.finalsquad:
                            types = "SQUAD";
                            break;


                    }

                    String versions = "";
                    switch (version.getCheckedRadioButtonId()) {
                        case R.id.finaltpp:
                            versions = "TPP";
                            break;
                        case R.id.finalfpp:
                            versions = "FPP";
                            break;

                    }




                    String fprize = mfprize.getEditText().getText().toString();
                    String perkill = mperkill.getEditText().getText().toString();
                    String fee = mfee.getEditText().getText().toString();


                    String map = mmap.getSelectedItem().toString().trim();


                    Match_details match_details = new Match_details();
                    match_details.setMatchId(String.valueOf(mid));
                    match_details.setDate(display.getText().toString());
                    match_details.setTime(timeview.getText().toString().trim());
                    match_details.setFPrize(fprize);
                    match_details.setPerKill(perkill);
                    match_details.setEntryFee(fee);
                    match_details.setType(types);
                    match_details.setVersion(versions);
                    match_details.setMap(map);
                    match_details.setRoomId("");
                    match_details.setPassword("");
                    System.out.println("Type,version:  "+types+versions);

                    databaseReference.child(match_details.getMatchId()).setValue(match_details).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Match added",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to add match",Toast.LENGTH_LONG).show();
                            System.out.print("Match Failed Exception : "+e.getMessage());
                        }
                    });
                    matchidreference.setValue(++mid);

                    MatchId.getEditText().setText("");
                    display.setVisibility(View.INVISIBLE);

                    mfee.getEditText().setText("");
                    mfprize.getEditText().setText("");
                    mperkill.getEditText().setText("");
                    type.clearCheck();
                    version.clearCheck();
                    onBackPressed();
                }
            }
        });
    }

    private void setScreenImage() {



        Random random=new Random();

        int rand = random.nextInt(13);
        System.out.println("Random NUmber"+rand);
        int array[]={R.mipmap.pone,R.mipmap.ptwo,R.mipmap.pthree,R.mipmap.pfour,R.mipmap.pfive,R.mipmap.psix,R.mipmap.pseven,R.mipmap.peight,R.mipmap.pten
                ,R.mipmap.peleven,R.mipmap.ptwelve,R.mipmap.pthirteen,R.mipmap.pnine};
        //  Toast.makeText(getApplicationContext(),rand,Toast.LENGTH_LONG).show();
        collapsingToolbarLayout.setBackgroundResource(array[rand]);
    }




    @SuppressLint("SetTextI18n")
    @Override
    public void OnTimeSet(TimePicker timePicker, int hour, int min) {
        String h=String.valueOf(hour);
        String m=String.valueOf(min);
        if(h.length()==1)
            h="0"+h;
        if(m.length()==1)
            m="0"+m;

        timeview.setText("      "+h+":"+m);
    }


}


