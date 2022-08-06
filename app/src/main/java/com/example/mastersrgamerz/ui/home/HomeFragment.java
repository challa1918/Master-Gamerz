package com.example.mastersrgamerz.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mastersrgamerz.Adapter.RecyclerViewAdapter;
import com.example.mastersrgamerz.Admin_Interface;
import com.example.mastersrgamerz.FinalMatchDetails;
import com.example.mastersrgamerz.MainActivity;
import com.example.mastersrgamerz.Model.Match_details;
import com.example.mastersrgamerz.Model.RegisterData;
import com.example.mastersrgamerz.R;
import com.example.mastersrgamerz.testpendingrequests;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;

public class HomeFragment extends Fragment  {

    private com.example.mastersrgamerz.ui.home.HomeViewModel homeViewModel;
    DatabaseReference databaseReference;
    ArrayList<Match_details>arrayList;
    private Dialog progressDialog;
    ProgressBar progressBar;
    Float translationy=180f;
    DatabaseReference appdemo;
    View root;
    Boolean isMenuOpen=false;
    FloatingActionButton addmatch,verifyuser;
    ArrayList<RegisterData > users_in_pending_list;
    LottieAnimationView loading,nodata;
    TextView nodatatext,badge;

    Animation fabopen,fabclose,rotateforward,rotatebackward;
    OvershootInterpolator overshootInterpolator= new OvershootInterpolator();
  FloatingActionButton floatingActionButton;




    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        arrayList=new ArrayList<>();
     root = inflater.inflate(R.layout.fragment_home, container, false);

loading=root.findViewById(R.id.loading);
nodata=root.findViewById(R.id.nodatanim);
nodatatext=root.findViewById(R.id.nodata);
floatingActionButton=root.findViewById(R.id.gg);
fabopen= AnimationUtils.loadAnimation(root.getContext(),R.anim.fab_open);
fabclose=AnimationUtils.loadAnimation(root.getContext(),R.anim.fab_close);
rotateforward=AnimationUtils.loadAnimation(root.getContext(),R.anim.rotation_forward);
rotatebackward=AnimationUtils.loadAnimation(root.getContext(),R.anim.rotation_backward);
        addmatch=root.findViewById(R.id.addMatchfab);
        verifyuser=root.findViewById(R.id.verifyuserfab);

//verify=root.findViewById(R.id.verify);

//badgecounter=root.findViewById(R.id.badgecounter);
    if (ActivityCompat.checkSelfPermission(root.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                int permission=1;
        ActivityCompat.requestPermissions((Activity)root.getContext(),new String[]{Manifest.permission.CALL_PHONE},permission);

                }








        databaseReference= FirebaseDatabase.getInstance().getReference("Matches");
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

floatingActionButton=root.findViewById(R.id.gg);

        final RecyclerView recyclerView=root.findViewById(R.id.MRV);

        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(root.getContext());
        appdemo=FirebaseDatabase.getInstance().getReference("Users_Admin").child(account.getId()).child("appdemostatus");
        appdemo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("AppdemoSttatus : "+dataSnapshot.getValue());
                if (dataSnapshot.getValue(Integer.class)==0)
                    AppDemo();
            }

            private void AppDemo() {
                new MaterialTapTargetSequence()
                        .addPrompt(new MaterialTapTargetPrompt.Builder((Activity) root.getContext())
                                .setPrimaryText("Open Options")

                                .setSecondaryText("Displays the options to add match and verify the user ")
                                .setTarget(root.findViewById(R.id.gg))
                                .create(), 4000)
                        .addPrompt(new MaterialTapTargetPrompt.Builder((Activity) root.getContext())
                                .setTarget(root.findViewById(R.id.addMatchfab))
                                .setPrimaryText("Add Match")
                                .setSecondaryText("Allows you to add match")

                                .create(), 4000)
                        .addPrompt(new MaterialTapTargetPrompt.Builder((Activity) root.getContext())
                                .setTarget(root.findViewById(R.id.verifyuserfab))
                                .setPrimaryText("Verify User")
                                .setSecondaryText("This feature is only enabled for super admin,which allows to verify othe app users")

                                .create(), 4000)


                        .show();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        for (DataSnapshot ds:
                             dataSnapshot.getChildren()) {
                            Match_details match_details=ds.getValue(Match_details.class);
                            arrayList.add(match_details);

                        }
                        RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter(arrayList,root.getContext());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                   //     recyclerView.addItemDecoration(new VerticalSpacingDecoration(20));
                        recyclerView.setAdapter(recyclerViewAdapter);
                 loading.setVisibility(View.INVISIBLE);
                 if (arrayList.size()==0){
                     nodata.setVisibility(View.VISIBLE);
                     nodatatext.setVisibility(View.VISIBLE);
                 }
                 else{
                     nodata.setVisibility(View.INVISIBLE);
                     nodatatext.setVisibility(View.INVISIBLE);
                 }




                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



              floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {

                        animateFab();

                    }
                });
              addmatch.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent i = new Intent(root.getContext(),FinalMatchDetails.class);
                   ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation((Activity) root.getContext(),addmatch, ViewCompat.getTransitionName(addmatch));
                      startActivity(i);
                  }
              });

              verifyuser.setOnClickListener(new View.OnClickListener() {
                  @SuppressLint("ResourceAsColor")
                  @Override
                  public void onClick(View v) {
                      if (!account.getEmail().equals("rameshyadavchalla@gmail.com")&&!account.getEmail().equals("radhakrishna2888@gmail.com")){
                          Snackbar.make(v,"You must be Super Admin to verify the user",Snackbar.LENGTH_SHORT).setAction("Logout", new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {

                                  logout();
                              }
                          }).setActionTextColor(ContextCompat.getColor(getActivity(), R.color.halfFilled)).setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)).show();;
                      }
                      else{
                      //    ActivityOptions  options=ActivityOptions.makeSceneTransitionAnimation((Activity)root.getContext(),verifyuser,ViewCompat.getTransitionName(verifyuser));
                          Intent i   =new Intent(root.getContext(), testpendingrequests.class);
                          startActivity(i);
                      }
                  }
              });


            }
        });
        return root;
    }
    private void logout() {

        GoogleSignInClient googleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(root.getContext(), gso);
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(root.getContext(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                    try {
                        HomeFragment.this.finalize();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                }
            }
        });
    }

    private void animateFab() {

      if(isMenuOpen){
          floatingActionButton.startAnimation(rotatebackward);
          addmatch.startAnimation(fabclose);
          verifyuser.startAnimation(fabclose);
          addmatch.setClickable(false);
          verifyuser.setClickable(false);
          isMenuOpen=false;
      }else {


          floatingActionButton.startAnimation(rotateforward);
          addmatch.startAnimation(fabopen);
          verifyuser.startAnimation(fabopen);
          addmatch.setClickable(true);
          verifyuser.setClickable(true);
          isMenuOpen=true;
      }



    }




}
