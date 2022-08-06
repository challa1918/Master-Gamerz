package com.example.mastersrgamerz.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mastersrgamerz.EditMatchdetails;
import com.example.mastersrgamerz.Model.Match_details;
import com.example.mastersrgamerz.Model.Players;
import com.example.mastersrgamerz.Model.User_transactions;
import com.example.mastersrgamerz.Notification.Api;
import com.example.mastersrgamerz.R;
import com.example.mastersrgamerz.ScrollingMatchDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<Match_details> list;
    Context context;
    FirebaseUser firebaseUser;
    int flag=0;

    View view;

    public RecyclerViewAdapter(ArrayList<Match_details> list, Context context) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.context = context;
        this.list = list;

        System.out.println("In COnstructor: " + list);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Match_details match_details = list.get(position);
        holder.cid.setText(match_details.getMatchId());
        holder.cdate.setText(match_details.getDate());
        holder.cfprize.setText(match_details.getFPrize());
        holder.cperkill.setText(match_details.getPerKill());
        holder.cfee.setText(match_details.getEntryFee());
        holder.ctype.setText(match_details.getType());
        holder.cversion.setText(match_details.getVersion());
        holder.cmap.setText(match_details.getMap());
        holder.ctime.setText(match_details.getTime());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editmatchintent=new Intent(view.getContext(), EditMatchdetails.class);
                editmatchintent.putExtra("matchid",match_details.getMatchId());
                view.getContext().startActivity(editmatchintent);
            }
        });



        Glide.with(context).load(R.drawable.hh).into(holder.circleImageView);
        view.setActivated(true);

    }


    @Override
    public int getItemCount() {

        System.out.println(list.size());

        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView cid, cdate, ctime, cfprize, cperkill, cfee, ctype, cversion, cmap;
        CircleImageView circleImageView;
    ImageView del,edit;
        MaterialCardView materialCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edit=itemView.findViewById(R.id.matchedit);
            cid = itemView.findViewById(R.id.cmatchidv);
            cdate = itemView.findViewById(R.id.cdatev);
            ctime = itemView.findViewById(R.id.ctimev);
            cfprize = itemView.findViewById(R.id.cprizev);
            cperkill = itemView.findViewById(R.id.cperkillv);
            cfee = itemView.findViewById(R.id.centryfeev);
            ctype = itemView.findViewById(R.id.ctypev);
            cmap = itemView.findViewById(R.id.cmapv);
            cversion = itemView.findViewById(R.id.cversionv);
            circleImageView = itemView.findViewById(R.id.civ);
            materialCardView = itemView.findViewById(R.id.Match);
            del=itemView.findViewById(R.id.del);
            del.setOnClickListener(this);
            materialCardView.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(final View v) {
            switch (v.getId()){
                case R.id.Match:

            final Match_details match_details = list.get(getAdapterPosition());
            Intent i = new Intent(view.getContext(), ScrollingMatchDetails.class);
           ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(),materialCardView, ViewCompat.getTransitionName(materialCardView));
            i.putExtra("MatchId", match_details.getMatchId());
            i.putExtra("fprize", match_details.getFPrize());
            i.putExtra("type", match_details.getType());
            i.putExtra("perkill", match_details.getPerKill());
            i.putExtra("time",match_details.getTime());
            i.putExtra("date",match_details.getDate());
            i.putExtra("map",match_details.getMap());
            i.putExtra("fee",match_details.getEntryFee());
            view.getContext().startActivity(i);
            break;
                case R.id.del:
                    System.out.println("InDelete");
                    final MaterialAlertDialogBuilder alertDialogBuilder=new MaterialAlertDialogBuilder(itemView.getContext());
                    alertDialogBuilder.setTitle("Are you sure you want to delete the match ?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final Match_details matchdetails = list.get(getAdapterPosition());
                            final String mid = matchdetails.getMatchId();
                            final String mentryfee = matchdetails.getEntryFee();

                            System.out.println("MatchID:" + mid);
                            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Matches_Going").child(mid);
                            final DatabaseReference refundref = FirebaseDatabase.getInstance().getReference("Users_Client").child("Users");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    System.out.println("PLayersList joiner: " + dataSnapshot.getChildrenCount());
                                    for (DataSnapshot ds :
                                            dataSnapshot.getChildren()) {
                                        final Players player = ds.getValue(Players.class);
                                        System.out.println("Players JOined in match: " + matchdetails.getMatchId() + player.PUBG_ID);
                                        assert player != null;



                                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                                        final ProgressDialog[] progressDialog = new ProgressDialog[1];
                                        progressDialog[0] = new ProgressDialog(view.getContext());
                                        progressDialog[0].setMessage("Deleting");
                                        progressDialog[0].setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressDialog[0].setIndeterminate(true);
                                        progressDialog[0].show();
                                        System.out.println("Player pubgid:  "+player.PUBG_ID);

                                        final Double[] newWalletValue = new Double[1];
                                        final DocumentReference documentReference;
                                        documentReference = db.collection("UsersBalance").document(player.PUBG_ID);
                                        db.runTransaction(new Transaction.Function<Double>() {
                                            @Nullable
                                            @Override
                                            public Double apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                                                DocumentSnapshot documentSnapshot = transaction.get(documentReference);
                                                newWalletValue[0] = documentSnapshot.getDouble("Balance") + Integer.parseInt(mentryfee);
                                                transaction.update(documentReference, "Balance", newWalletValue[0]);

                                                return null;
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<Double>() {
                                            @Override
                                            public void onSuccess(Double aDouble) {

                                                ref.child(player.PUBG_ID).removeValue();
                                                refundref.child(player.PUBG_ID).child("Balance").setValue(String.valueOf(newWalletValue[0])).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressDialog[0].dismiss();
                                                        Toast.makeText(itemView.getContext(), "Refund Success", Toast.LENGTH_LONG).show();
                                                        sendNotification(player.Token_Id, mid);
                                                        Date time = new Date();
                                                        DatabaseReference re = FirebaseDatabase.getInstance().getReference("Users_Client");
                                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm");
                                                        User_transactions transactions = new User_transactions("Cancelled matchId:" + matchdetails.getMatchId(), String.valueOf(mentryfee), "0", dateFormat.format(time));
                                                        re.child("Transactions").child(player.PUBG_ID).push().setValue(transactions);


                                                    }
                                                });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(context, "Failed to send refund delete again", Toast.LENGTH_LONG).show();
                                                flag = 1;
                                                System.out.println("meeeee" + e.getMessage());
                                            }
                                        });


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Matches").child(matchdetails.getMatchId());
                                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(itemView.getContext(), "Successfully Deleted", Toast.LENGTH_LONG).show();
                                    }
                                });

                        }
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialogBuilder.setIcon(R.drawable.delt);

                    AlertDialog alertDialog=alertDialogBuilder.create();
                    alertDialog.show();


                    break;


            }
            //     Toast.makeText(itemView.getContext(),"hfhgfjdh "+getAdapterPosition(),Toast.LENGTH_LONG).show();
        }
    }

    private void sendNotification(String token,String matchid) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dbtry-c5fbe.web.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.sendNotification(token,"Match "+matchid+" has been Cancelled!!!" ,"Refund is succesfully credited in your account");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

         //       Toast.makeText(view.getContext(), "Refund Notification Successfully sent!!", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
        //        Toast.makeText(view.getContext(), "Refund Notification Failed to send!! Retry Again...", Toast.LENGTH_LONG).show();
            }
        });

    }


}
