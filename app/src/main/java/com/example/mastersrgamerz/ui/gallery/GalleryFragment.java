package com.example.mastersrgamerz.ui.gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mastersrgamerz.Adapter.RecyclerViewAdapter;
import com.example.mastersrgamerz.Adapter.RequestlistAdapter;
import com.example.mastersrgamerz.Admin_Interface;
import com.example.mastersrgamerz.Model.Redemption;
import com.example.mastersrgamerz.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;

public class GalleryFragment extends Fragment {

View root;
DatabaseReference redeemdemo;
BottomNavigationView bottomNavigationView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        System.out.println("Galery Fragment OnCreate");
        root= inflater.inflate(R.layout.fragment_gallery, container, false);
        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(root.getContext());

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new redeemlist()).commit();

        redeemdemo = FirebaseDatabase.getInstance().getReference("Users_Admin").child(account.getId()).child("redeemdem");
redeemdemo.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue(Integer.class)==0)
            RedeemDemo();
    }

    private void RedeemDemo() {
        new MaterialTapTargetSequence()
                .addPrompt(new MaterialTapTargetPrompt.Builder((Activity) root.getContext())
                        .setPrimaryText("Redeem List")

                        .setSecondaryText("This option displays the list redemption requests made by users ")
                        .setTarget(root.findViewById(R.id.rt))
                        .create(), 2000)
                .addPrompt(new MaterialTapTargetPrompt.Builder((Activity) root.getContext())
                        .setTarget(root.findViewById(R.id.rh))
                        .setPrimaryText("Redeem History")
                        .setSecondaryText("This option displays the transactions redeemptions made to the user requests")

                        .create(), 2000)


                .show();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                redeemdemo.setValue(1);
            }
        },4000);


    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
       bottomNavigationView=root.findViewById(R.id.bottom_navigation);

       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               Fragment selected=null;
               switch (item.getItemId()){
                   case R.id.rh:
                       selected= new redeemhistory();
                       break;
                   case R.id.rt:
                     selected=new redeemlist();
                       break;
               }

               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,selected).commit();
               return true;
           }
       });


        return root;
    }


}
