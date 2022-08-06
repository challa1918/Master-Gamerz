package com.example.mastersrgamerz;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.example.mastersrgamerz.Model.RegisterData;
import com.example.mastersrgamerz.Notification.Api;
import com.example.mastersrgamerz.Notification.NotficationHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.SnackbarContentLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.Telephony.BaseMmsColumns.MESSAGE_ID;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    static GoogleSignInClient googleSignInClient;
    private final int REQUESTCODE = 2;
    TextView signin;
    Button login;
    EditText email, pass;
    ProgressBar loading;

    SignInButton signInButton;
    int RC_SIGN_IN = 1;
    String token_id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String verificationcheck;
    private FirebaseAuth mAuth;
    public static final String CHANNEL_ID="Client";
    public static final String CHANNEL_NAME="Client name";
    public static final String CHANNEL_DESC="Client notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setTheme(R.style.AppTheme_Launcher);

        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        loading=findViewById(R.id.progressBarhorizontal);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);


            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if(activeNetwork==null ||!activeNetwork.isConnected())
                Snackbar.make(findViewById(R.id.b),"No internet connection",Snackbar.LENGTH_INDEFINITE)
                        .setActionTextColor(ContextCompat.getColor(MainActivity.this, R.color.halfFilled)).setBackgroundTint(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark)).show();;








        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }



        mAuth = FirebaseAuth.getInstance();
        signInButton = findViewById(R.id.gsign);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        tokenId();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.setVisibility(View.VISIBLE);
                        signIn();
            }

            private void signIn() {
                //getting the google signin intent
                Intent signInIntent = googleSignInClient.getSignInIntent();

                //starting the activity for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(MainActivity.this, "Eroor"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");

                            final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
                            final RegisterData registerData = new RegisterData();
                            registerData.UID=account.getId();
                            registerData.EMAIL=account.getEmail();
                            registerData.NAME=account.getDisplayName();
                            registerData.tokenid=token_id;
                            registerData.profilepic=account.getPhotoUrl().toString();
                            registerData.block="false";
                            registerData.verification="Pending";
                            registerData.phnno="0";
                            registerData.appdemostatus=0;
                            registerData.redeemdem=0;


                            DatabaseReference db= FirebaseDatabase.getInstance().getReference("Users_Admin").child(account.getId()).child("verification");
                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.exists()) {
                                        String verification = dataSnapshot.getValue(String.class);
                                        System.out.println("Name:   " + verification + dataSnapshot.getValue());
                                        if (account.getEmail().equals("rameshyadavchalla@gmail.com") || verification.equals("Success")||account.getEmail().equals("radhakrishna2888@gmail.com")) {
                                            final DatabaseReference fire = FirebaseDatabase.getInstance().getReference("Users_Admin");
                                            Intent i = new Intent(MainActivity.this, Admin_Interface.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }else{
                                        System.out.println("Logged in with no previous data");
                                            final DatabaseReference fire = FirebaseDatabase.getInstance().getReference("Users_Admin");
                                            fire.child(registerData.UID).setValue(registerData);
                                        if (account.getEmail().equals("rameshyadavchalla@gmail.com")||account.getEmail().equals("radhakrishna2888@gmail.com")) {
                                                fire.child(registerData.UID).child("verification").setValue("Success");
                                            Intent i = new Intent(MainActivity.this, Admin_Interface.class);
                                            startActivity(i);

                                            finish();
                                        }
                                                    DatabaseReference db=FirebaseDatabase.getInstance().getReference("SuperAdmintoken");
                                                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            sendNotification(dataSnapshot.getValue().toString(),registerData.NAME);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                            Intent i= new Intent(MainActivity.this,PendingUsersWaiting.class);
                                            startActivity(i);
                                            finish();
                                            // sendNotification(registerData.NAME);
                                        }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });









                            //Toast.makeText(MainActivity.this, "User Signed In "+user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    private void sendNotification(String token,String name) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dbtry-c5fbe.web.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.sendNotification(token, "Account Verification", name+" is waiting for accout verification");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void tokenId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Token Error", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token_id = task.getResult().getToken();
                        // Log and toast
                        Log.d("Token", token_id);
                     //   Toast.makeText(MainActivity.this, token_id, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        loading.animate();


        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            signInButton.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users_Admin").child(account.getId());
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (account != null && dataSnapshot.exists()) {



                        // hload.setVisibility(View.VISIBLE);

                        //   intro.setVisibility(View.INVISIBLE);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users_Admin").child(account.getId()).child("verification");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                System.out.println("OnStart:  " + dataSnapshot.getValue());

                                if (dataSnapshot.exists()) {

                                    String verification = dataSnapshot.getValue(String.class);
                                    if (verification.equals("Success") || account.getEmail().equals("rameshyadavchalla@gmail.com")||account.getEmail().equals("radhakrishna2888@gmail.com")) {
                                        //        Toast.makeText(getApplicationContext(),"Onstart", Toast.LENGTH_LONG).show();
                                        databaseReference.setValue("Success");
                                        Intent intent = new Intent(MainActivity.this, Admin_Interface.class);
                                        startActivity(intent);
                                        finish();


                                    } else {

                                        Intent i = new Intent(MainActivity.this, PendingUsersWaiting.class);
                                        startActivity(i);
                                        finish();


                                    }
                                } else {
                             //       Toast.makeText(getApplicationContext(), "Signin With new Account", Toast.LENGTH_LONG).show();


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        loading.setVisibility(View.INVISIBLE);
                        signInButton.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }
}
