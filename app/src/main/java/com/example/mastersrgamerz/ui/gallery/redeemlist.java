package com.example.mastersrgamerz.ui.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.format.DateUtils;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.example.mastersrgamerz.Adapter.RequestlistAdapter;
import com.example.mastersrgamerz.Model.Message;
import com.example.mastersrgamerz.Model.Redemption;
import com.example.mastersrgamerz.Payment;
import com.example.mastersrgamerz.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;

import static com.google.android.material.color.MaterialColors.ALPHA_FULL;

public class redeemlist extends Fragment {
    View root;
    EditText pid,amt;
    Button update;
    ImageView nodatafound;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    Timestamp timestamp;
    TextView nodatatext,time;
    RequestlistAdapter recyclerViewAdapter;
    ArrayList<Redemption> redemptionArrayList;
LottieAnimationView loading,lottieAnimationView;
    ItemTouchHelper itemTouchHelper;
    Redemption deletedrequest;
    View view;
    DatabaseReference redeemdemo,ss;
    Snackbar snackbar;

    public redeemlist(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.redeemlist,null);
        redemptionArrayList=new ArrayList<>();
        ss=FirebaseDatabase.getInstance().getReference("Date");
        //ss.setValue(Timestamp.now());
        //Timestamp s=Timestamp.now();
        databaseReference= FirebaseDatabase.getInstance().getReference("Redemption_Requests");
 loading=view.findViewById(R.id.rloading);
        recyclerView=view.findViewById(R.id.reqlist);

        lottieAnimationView=view.findViewById(R.id.redeemlistnodata);
        nodatatext=view.findViewById(R.id.listnodatafound);


        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(view.getContext());
        redeemdemo = FirebaseDatabase.getInstance().getReference("Users_Admin").child(account.getId()).child("redeemdem");
        redeemdemo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if(dataSnapshot.getValue(Integer.class)==1)
                  RedeemDemo();

            }

            private void RedeemDemo() {
                new MaterialTapTargetSequence()
                        .addPrompt(new MaterialTapTargetPrompt.Builder((Activity) view.getContext())
                                .setTarget(view.findViewById(R.id.payimg))
                                .setPrimaryText("Pay User")
                                .setSecondaryText("Swipe right to initiate redeemption to the user")

                                .create(), 4000)
                        .addPrompt(new MaterialTapTargetPrompt.Builder((Activity) view.getContext())
                                .setPrimaryText("Delete Request")

                                .setSecondaryText("Swipe left to delete the user redeem request ")
                                .setTarget(view.findViewById(R.id.deletimg))
                                .create(), 4000)



                        .show();
            redeemdemo.setValue(2);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                redemptionArrayList.clear();
                for (DataSnapshot ds:
                        dataSnapshot.getChildren()) {
                    Redemption redemption=ds.getValue(Redemption.class);
                    redemptionArrayList.add(redemption);
                }

                Collections.sort(redemptionArrayList);

                recyclerViewAdapter=new RequestlistAdapter(redemptionArrayList,view.getContext());
                ItemTouchHelper.SimpleCallback simpleCallback= new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                        View itemView = viewHolder.itemView;
                        MaterialCardView card=itemView.findViewById(R.id.cc);
                        card.setCardBackgroundColor(ColorStateList.valueOf(Color.BLACK));
                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        if (direction == ItemTouchHelper.LEFT) {
                            View reasondialog=LayoutInflater.from(view.getContext()).inflate(R.layout.reasoning_dialog,null);
                            int position = viewHolder.getAdapterPosition();
                            deletedrequest = redemptionArrayList.get(position);
                            AlertDialog.Builder dialog= new AlertDialog.Builder(view.getContext());
                            dialog.setView(reasondialog);
                            final EditText reason=reasondialog.findViewById(R.id.reason);
                            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseReference.child(deletedrequest.PUBGID).removeValue();
                                    Date date1 = new Date();
                                    SimpleDateFormat dateFormat;
                                    dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                    Message message=new Message("Request Deleted","A withdrawl request of "+deletedrequest.amount+"/- has been rejected due to following reason :\n"+reason.getText().toString(),dateFormat.format(date1),false);
                                    DatabaseReference reasonref=FirebaseDatabase.getInstance().getReference("Users_Client").child("Messages");
                                    reasonref.child(deletedrequest.PUBGID).push().setValue(message);
                                    snackbar.show();

                                }
                            });
                            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        reload();
                                }
                            });
                            dialog.setIcon(R.drawable.error);
                            dialog.setTitle("Reason");
                        dialog.setCancelable(false).create().show();



                           snackbar= Snackbar.make(recyclerView, "A request has been deleted", Snackbar.LENGTH_LONG);
                           snackbar.setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    databaseReference.child(deletedrequest.PUBGID).setValue(deletedrequest);
                                    DatabaseReference reasonref=FirebaseDatabase.getInstance().getReference("Users_Client").child("Messages");
                                    reasonref.child(deletedrequest.PUBGID).removeValue();
                                    recyclerViewAdapter.notifyDataSetChanged();
                                }
                            }).setBackgroundTintList(ColorStateList.valueOf(Color.BLACK)).setActionTextColor(Color.YELLOW);

                        }

                        if (direction == ItemTouchHelper.RIGHT) {

                            int position = viewHolder.getAdapterPosition();
                            Redemption redemption = redemptionArrayList.get(position);
                            Intent intent = new Intent(view.getContext(), Payment.class);
                       //     ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(),payButton, ViewCompat.getTransitionName(payButton));
                            GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(view.getContext());
                            intent.putExtra("name", redemption.email);
                            intent.putExtra("upi", redemption.BHIM_ID);
                            intent.putExtra("amount", redemption.amount);
                            intent.putExtra("pid", redemption.PUBGID);
                            intent.putExtra("pun", redemption.PUBG_USERNAME);
                            startActivity(intent);



                        }
                    }

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                        new RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                                .addSwipeLeftBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.AboutToFull))
                                .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                                .addSwipeLeftLabel("Delete")
                                .setSwipeLeftLabelColor(ContextCompat.getColor(view.getContext(),R.color.check))
                                .setSwipeLeftActionIconTint(ContextCompat.getColor(view.getContext(),R.color.check))
                                .setSwipeRightActionIconTint(ContextCompat.getColor(view.getContext(),R.color.check))
                               .addSwipeRightActionIcon(R.drawable.ic_attach_money_black_24dp)
                                .addSwipeRightLabel("Pay")
                                .setSwipeRightLabelColor(ContextCompat.getColor(view.getContext(),R.color.colorPrimaryDark))
                                .addSwipeRightBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.halfFilled))
                                .setSwipeRightActionIconTint(ContextCompat.getColor(view.getContext(),R.color.colorPrimaryDark))
                                .create()
                                .decorate();
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                    }



                };
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

                new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
                recyclerViewAdapter.notifyDataSetChanged();
          //     recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(15));
                recyclerView.setAdapter(recyclerViewAdapter);
                lottieAnimationView.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.INVISIBLE);
                if(redemptionArrayList.size()==0){
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    nodatatext.setVisibility(View.VISIBLE);
                //    nodatafound.setVisibility(View.VISIBLE);
                }else{
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    nodatatext.setVisibility(View.INVISIBLE);
                }




            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
;
  reload();
    }

    private void reload() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                redemptionArrayList.clear();
                for (DataSnapshot ds:
                        dataSnapshot.getChildren()) {
                    Redemption redemption=ds.getValue(Redemption.class);
                    redemptionArrayList.add(redemption);
                }
                Collections.sort(redemptionArrayList);
                recyclerViewAdapter=new RequestlistAdapter(redemptionArrayList,view.getContext());
                recyclerView.setAdapter(recyclerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
