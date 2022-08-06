package com.example.mastersrgamerz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mastersrgamerz.Model.RegisterData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PendingUsersWaiting extends AppCompatActivity {
Button logout;
DatabaseReference databaseReference;
GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_users_waiting);
        logout=findViewById(R.id.logdif);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(PendingUsersWaiting.this);
        databaseReference= FirebaseDatabase.getInstance().getReference("Users_Admin").child(account.getId()).child("verification");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    if (dataSnapshot.getValue().equals("Success")) {
                        Intent i = new Intent(PendingUsersWaiting.this, Admin_Interface.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "Your Account is Verified", Toast.LENGTH_LONG).show();

                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                      Intent i=new Intent(PendingUsersWaiting.this,MainActivity.class);
                      startActivity(i);
                      finish();
                  }
              })  ;
            }
        });
    }
}
