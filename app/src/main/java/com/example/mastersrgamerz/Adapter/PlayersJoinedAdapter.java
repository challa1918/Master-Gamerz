package com.example.mastersrgamerz.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;

import java.time.LocalDate;
import java.time.LocalDateTime;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastersrgamerz.Model.Match_details;
import com.example.mastersrgamerz.Model.Players;
import com.example.mastersrgamerz.Model.User;
import com.example.mastersrgamerz.Model.User_transactions;
import com.example.mastersrgamerz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PlayersJoinedAdapter extends RecyclerView.Adapter<PlayersJoinedAdapter.ViewHolder> {
    ArrayList<Players> list;
    Context context;
    View view;
    int newkills,count=0;
    String MatchId, fprize, type, perkill,time,date;

    public PlayersJoinedAdapter(ArrayList<Players> list, Context context, String matchId, String fprize, String type, String perkill,String time,String date) {
        this.list = list;
        this.context = context;
        this.time=time;
        this.date=date;
        MatchId = matchId;
        this.fprize = fprize;
        this.type = type;
        this.perkill = perkill;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.playersdisplay, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Players player = list.get(position);
        holder.pname.setText("  "+(position+1)+"."+player.PUBG_USERNAME);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Dialog d;
        Button save;
        View pop;
        TextView pname, title, amount_display, dwin, dtype, dperkill;
        EditText kills;
        RadioGroup radioGroup;
        RadioButton yes, no;
        Players player;
MaterialCardView cardView;




        @RequiresApi(api = Build.VERSION_CODES.O)
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            pop = LayoutInflater.from(itemView.getContext()).inflate(R.layout.activity_post__match__updates, null);
            kills = pop.findViewById(R.id.ppp);
            amount_display = pop.findViewById(R.id.amount_display);
            pname = itemView.findViewById(R.id.pname);
            title = pop.findViewById(R.id.titl);
            save = pop.findViewById(R.id.save);
            cardView=itemView.findViewById(R.id.PRV);
            d = new Dialog(itemView.getContext());
            radioGroup = pop.findViewById(R.id.radioGroup);
            yes = pop.findViewById(R.id.yes);
            no = pop.findViewById(R.id.no);
            dwin = pop.findViewById(R.id.dfprize);
            dtype = pop.findViewById(R.id.dtype);
            dperkill = pop.findViewById(R.id.dperkill);
            dwin.setText("Winning Prize : " + fprize + "/-");
            dtype.setText("Type : " + type);
            dperkill.setText("PerKil : " + perkill + "/-");


String splitdate[]=date.split("-");

    Calendar calendar=  Calendar.getInstance();
    final int hour =calendar.getTime().getHours();
    final int min=calendar.getTime().getMinutes();

    final String mtime[]=time.split(":");

            LocalDate matchdate= LocalDate.of(Integer.parseInt(splitdate[2]),Integer.parseInt(splitdate[1]),Integer.parseInt(splitdate[0]));
            LocalTime matchtime=LocalTime.of(Integer.parseInt(mtime[0]),Integer.parseInt(mtime[1]));
            final LocalDateTime matchDateTime=LocalDateTime.of(matchdate,matchtime).plusMinutes(25);
            final LocalDateTime curretDateTime=LocalDateTime.now();
System.out.println("MatchDateTime:   "+matchDateTime);
            System.out.println("CurrentDateTime:   "+curretDateTime);
         //   int mi=Integer.parseInt(mtime[1]);
    if((Integer.parseInt(mtime[1])+30)>=60)
                mtime[0]=String.valueOf(Integer.parseInt(mtime[0])+1);
            mtime[1]=String.valueOf((Integer.parseInt(mtime[1])+30)-60);



            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            /*        System.out.println("Given Time:   "+time);
                    System.out.println("Current Time: "+hour+":"+min);
                    System.out.println("Calculated TIme : "+mtime[0]+":"+mtime[1]);*/





                    if(curretDateTime.compareTo(matchDateTime)>0){
                        player = list.get(getAdapterPosition());
                        if (!player.rewarded) {

                            d.setContentView(pop);
                            title.setText(list.get(getAdapterPosition()).PUBG_USERNAME);
                            kills.setText("");
                            radioGroup.clearCheck();
                            amount_display.setVisibility(View.INVISIBLE);
                            d.show();
                        } else {
                            Toast.makeText(itemView.getContext(), "Rewards already updated", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(itemView.getContext(),"Please Come after the completion of the  match",Toast.LENGTH_LONG).show();
                    }
                    kills.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            radioGroup.clearCheck();

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(!kills.getText().toString().equals("")) {
                                amount_display.setVisibility(View.VISIBLE);

                                Toast.makeText(itemView.getContext(), "Yes", Toast.LENGTH_SHORT).show();


                                DatabaseReference calcdb = FirebaseDatabase.getInstance().getReference("Matches").child(MatchId);
                                calcdb.addValueEventListener(new ValueEventListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        int total_amount = 0;

                                        Match_details match_details = dataSnapshot.getValue(Match_details.class);

                                        assert match_details != null;
                                        if(dataSnapshot.getChildrenCount()!=0) {
                                            total_amount = Integer.parseInt(kills.getText().toString()) * Integer.parseInt(match_details.getPerKill());
                                            //        Toast.makeText(itemView.getContext(), "matchdetails winningprize:  " + match_details.getFPrize(), Toast.LENGTH_SHORT).show();
                                            switch (match_details.getType()) {
                                                case "SOLO":
                                                    amount_display.setText("Total Money Earned:" + (total_amount + Integer.parseInt(match_details.getFPrize())));
                                                    break;
                                                case "DUO":
                                                    amount_display.setText("Total Money Earned:" + (total_amount + Integer.parseInt(match_details.getFPrize()) / 2));
                                                    break;
                                                case "SQUAD":
                                                    amount_display.setText("Total Money Earned:" + (total_amount + Integer.parseInt(match_details.getFPrize()) / 4));
                                                    break;
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            else{
                                radioGroup.clearCheck();
                          Toast.makeText(itemView.getContext(),"First Enter Number of kills",Toast.LENGTH_LONG).show();
                            }


                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {


                            @Override
                            public void onClick (View v){

                                if(!kills.getText().toString().equals("")) {
                                    amount_display.setVisibility(View.VISIBLE);
                                    Toast.makeText(itemView.getContext(), "No", Toast.LENGTH_SHORT).show();
                                    DatabaseReference calcdb = FirebaseDatabase.getInstance().getReference("Matches").child(MatchId);
                                    calcdb.addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            int total_amount;

                                            Match_details match_details = dataSnapshot.getValue(Match_details.class);
                                            if (match_details != null) {
                                                total_amount = Integer.parseInt(kills.getText().toString()) * Integer.parseInt(match_details.getPerKill());
                                                amount_display.setText("Total Money Earned:" + total_amount);
                                            }
                                         //   Toast.makeText(itemView.getContext(), "matchdetails perKill:  " + match_details.getPerKill(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                                else{
                                    radioGroup.clearCheck();
                                    Toast.makeText(itemView.getContext(),"First Enter Number of kills",Toast.LENGTH_LONG).show();
                                }
                        }

                    });


                    save.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void onClick(View v) {
                            if ((kills.getText().toString().equals("") )||(radioGroup.getCheckedRadioButtonId()<0) ) {
                                Toast.makeText(itemView.getContext(), "Fill all the details", Toast.LENGTH_SHORT).show();
                            } else {
                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users_Client").child("Users");
                                databaseReference.orderByChild("PUBG_ID").equalTo(list.get(getAdapterPosition()).PUBG_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User user = null;
                                        for (DataSnapshot ds :
                                                dataSnapshot.getChildren()) {
                                            user = ds.getValue(User.class);
                                            System.out.println("IN loop: " + user.Name);

                                        }

                                        if (user.PUBG_ID.equals(player.PUBG_ID)) {

                                            String args[]=amount_display.getText().toString().split(":");
                                          //  Toast.makeText(itemView.getContext(),args[1],Toast.LENGTH_LONG).show();
                                            final int newbal = Integer.parseInt(args[1].trim());

                                            FirebaseFirestore dbtransact=FirebaseFirestore.getInstance();

                                            final ProgressDialog[] progressDialog = new ProgressDialog[1];
                                            progressDialog[0] = new ProgressDialog(view.getContext());
                                            progressDialog[0].setMessage("Uploading Results");
                                            progressDialog[0].setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                            progressDialog[0].setIndeterminate(true);
                                            progressDialog[0].show();
                                            final Double[] newWinningsValue = new Double[1];
                                            final Double[] newWalletValue = new Double[1];
                                            final DocumentReference documentReference;
                                            documentReference = dbtransact.collection("UsersBalance").document(player.PUBG_ID);
                                            final User finalUser = user;
                                            dbtransact.runTransaction(new Transaction.Function<Double>() {
                                                @Nullable
                                                @Override
                                                public Double apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                                                    DocumentSnapshot documentSnapshot=transaction.get(documentReference);
                                                    newWinningsValue[0] =documentSnapshot.getDouble("Winnings")+newbal;
                                                    transaction.update(documentReference,"Winnings", newWinningsValue[0]);


                                                    return null;
                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<Double>() {
                                                @Override
                                                public void onSuccess(Double aDouble) {

                                                    databaseReference.child(player.PUBG_ID).child("total_money_earned").setValue(finalUser.total_money_earned+newbal);
                                                    databaseReference.child(player.PUBG_ID).child("winning_money").setValue(newWinningsValue[0]);
                                                    databaseReference.child(player.PUBG_ID).child("Kills").setValue(String.valueOf(newkills));
                                                    databaseReference.child(player.PUBG_ID).child("no_of_matches").setValue(finalUser.no_of_matches+1);
                                                    progressDialog[0].cancel();



                                                    Date time=new Date();
                                                    DatabaseReference de=FirebaseDatabase.getInstance().getReference("Users_Client");
                                                    SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy kk:mm");
                                                    User_transactions transactions=new User_transactions("MatchId:"+MatchId,String.valueOf(newbal),"0",dateFormat.format(time));
                                                    if(!transactions.amount.equals("0"))
                                                        de.child("Transactions").child(finalUser.PUBG_ID).push().setValue(transactions);
                                                    newkills = (Integer.parseInt(kills.getText().toString()) + Integer.parseInt(finalUser.Kills));




                                                    if (radioGroup.getCheckedRadioButtonId() == R.id.yes) {

                                                        int newwins = Integer.parseInt(finalUser.Wins) + 1;

                                                        databaseReference.child(finalUser.PUBG_ID).child("Wins").setValue(String.valueOf(newwins)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                //    Toast.makeText(itemView.getContext(), "Wins Updated", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                    }

                                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Matches_Going").child(MatchId).child(player.PUBG_ID);
                                                    db.removeValue();
                                                    //player.rewarded=true;
                                                    radioGroup.clearCheck();
                                                    d.cancel();


                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog[0].dismiss();
                                               Toast.makeText(context,"Details upload failed retry",Toast.LENGTH_LONG).show();
                                                    System.out.println("meeeee"+e.getMessage());
                                                }
                                            });



                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    });

                }
            });
        }
    }
}

