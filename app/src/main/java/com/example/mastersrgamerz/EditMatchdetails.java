package com.example.mastersrgamerz;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SymbolTable;
import android.os.Build;
import android.os.Bundle;

import com.example.mastersrgamerz.Model.Match_details;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
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

import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class EditMatchdetails extends AppCompatActivity implements MyTimePicker.TimepickerListener {
Intent i;
DatabaseReference editmatchref;
Match_details match_details;
    TextInputLayout MatchId,mfprize,mperkill,mfee;
    Spinner mmap;
    RadioGroup type,version;
    TextView display,timeview;
    TextView matchid;

    ArrayList<String> radiolist,maps;
    String selectedmap="none";
    String maplist[];
    Button mmcalendar,mmtime;
    DatabaseReference mapsref;
    CollapsingToolbarLayout screen;
    int mid;

    ImageView back;

    RadioButton solo,duo,squad,tpp,fpp;
    Button create;
    DatabaseReference databaseReference,matchidreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_matchdetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseReference= FirebaseDatabase.getInstance().getReference("Matches");
        matchidreference=FirebaseDatabase.getInstance().getReference("MATCH_ID");
        MatchId=findViewById(R.id.editmid);
        mfee=findViewById(R.id.editfee);
        mfprize=findViewById(R.id.editfprize);
        mperkill=findViewById(R.id.editperkill);
        mmap=findViewById(R.id.editspinner);
        solo=findViewById(R.id.editsolo);
        duo=findViewById(R.id.editduo);
        squad=findViewById(R.id.editsquad);
        tpp=findViewById(R.id.edittpp);
        fpp=findViewById(R.id.editfpp);
        type=findViewById(R.id.edittype);
        version=findViewById(R.id.editversion);
        display= findViewById(R.id.editdateview);
        timeview=findViewById(R.id.edittimeview);
        back=findViewById(R.id.editback);
        create=findViewById(R.id.editcreate);
        mmcalendar=findViewById(R.id.editcalendar);
        mmtime=findViewById(R.id.edittime);
        screen=findViewById(R.id.screen);
        setScreenImage();
        i=getIntent();
type.check(R.id.editsquad);

        maps=new ArrayList<>();
        mapsref=FirebaseDatabase.getInstance().getReference("MAPS");
        editmatchref= FirebaseDatabase.getInstance().getReference("Matches").child(i.getStringExtra("matchid"));

        editmatchref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                match_details=dataSnapshot.getValue(Match_details.class);
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
                                EditMatchdetails.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                maps
                        );
                        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);

                        mmap.setAdapter(adapter);
                        mmap.setSelection(maps.indexOf(match_details.getMap()));


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                MatchId.getEditText().setText(match_details.getMatchId());
                mfee.getEditText().setText(match_details.getEntryFee());
                mfprize.getEditText().setText(match_details.getFPrize());
                mperkill.getEditText().setText(match_details.getPerKill());
                display.setText(match_details.getDate());
                timeview.setText(match_details.getTime());
                System.out.println("Match Type and Version : "+match_details.getVersion()+match_details.getType());
               switch(match_details.getType()){
                    case "SOLO":  type.check(solo.getId());
                    //Toast.makeText(getApplicationContext(),"Solo selected",Toast.LENGTH_LONG).show();         break;
                    case "DUO":type.check(R.id.editduo);
                   // Toast.makeText(getApplicationContext(),"Duo selected",Toast.LENGTH_LONG).show();break;
                    case "SQUAD":type.check(R.id.editsquad);break;
                    default:
                 //       Toast.makeText(getApplicationContext(),"Nothing selected",Toast.LENGTH_LONG).show();
                }
                switch(match_details.getVersion()){
                    case "TPP":version.check(R.id.edittpp);break;
                    case "FPP":version.check(R.id.editfpp);break;

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mmtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTimePicker mytimepicker=new MyTimePicker();
                mytimepicker.setCancelable(false);
                mytimepicker.show(getSupportFragmentManager(),"TimePicker");
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
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditMatchdetails.this);
                    alertDialog.setTitle("Add New Map");
                    final EditText input = new EditText(EditMatchdetails.this);
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
                    AlertDialog.Builder alert = new AlertDialog.Builder(EditMatchdetails.this);
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
                Calendar calendar=Calendar.getInstance();
                final int year,date,month;
                year=calendar.get(Calendar.YEAR);
                date=calendar.get(Calendar.DATE);
                month=calendar.get(Calendar.MONTH);
                DatePickerDialog datePickerDialog= new DatePickerDialog(EditMatchdetails.this, new DatePickerDialog.OnDateSetListener() {
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
                    Toast.makeText(EditMatchdetails.this, "Fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    String types = "";
                    switch (type.getCheckedRadioButtonId()) {
                        case R.id.editsolo:
                            types = "SOLO";
                            break;
                        case R.id.editduo:
                            types = "DUO";
                            break;
                        case R.id.editsquad:
                            types = "SQUAD";
                            break;


                    }

                    String versions = "";
                    switch (version.getCheckedRadioButtonId()) {
                        case R.id.edittpp:
                            versions = "TPP";
                            break;
                        case R.id.editfpp:
                            versions = "FPP";
                            break;

                    }




                    String fprize = mfprize.getEditText().getText().toString();
                    String perkill = mperkill.getEditText().getText().toString();
                    String fee = mfee.getEditText().getText().toString();


                    String map = mmap.getSelectedItem().toString().trim();


                    Match_details match_details = new Match_details();
                    match_details.setMatchId(MatchId.getEditText().getText().toString());
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
                            Toast.makeText(getApplicationContext(),"Match details updated",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to update match",Toast.LENGTH_LONG).show();
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

    public void OnTimeSet(TimePicker timePicker, int hour, int min) {
        String h=String.valueOf(hour);
        String m=String.valueOf(min);
        if(h.length()==1)
            h="0"+h;
        if(m.length()==1)
            m="0"+m;

        timeview.setText("      "+h+":"+m);
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
