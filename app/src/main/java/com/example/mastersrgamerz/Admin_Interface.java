package com.example.mastersrgamerz;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.example.mastersrgamerz.Adapter.RecyclerViewAdapter;
import com.example.mastersrgamerz.Model.Match_details;
import com.example.mastersrgamerz.Model.RegisterData;
import com.example.mastersrgamerz.ui.home.HomeFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;

public class Admin_Interface extends AppCompatActivity implements internet_receiver.getConnection {

    private static final String MY_PREFS ="AppDemo" ;
    FirebaseUser firebaseUser;
    CircleImageView Profile;
    ImageView bar;
    NavigationView navigationView;
    int internetcheck=-1;
    TextView mail, name;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    TextView textCartItemCount;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<String> arrayList;
    GoogleSignInClient googleSignInClient;
    private AppBarConfiguration mAppBarConfiguration;
    BroadcastReceiver internet_receiver=null;
    int flag=0;
    DatabaseReference appdemo;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin__interface);
        GoogleSignInAccount acount= GoogleSignIn.getLastSignedInAccount(Admin_Interface.this);
appdemo=FirebaseDatabase.getInstance().getReference("Users_Admin").child(acount.getId()).child("appdemostatus");
appdemo.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        System.out.println("AppdemoSttatus : "+dataSnapshot.getValue());
        if (dataSnapshot.getValue(Integer.class)==0)
            AppDemo();
    }




    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});








        DatabaseReference mapinsertion=FirebaseDatabase.getInstance().getReference("MAPS");
        ArrayList<String>Maps=new ArrayList<>();
        Maps.add("ERANGLE");
        Maps.add("SANHOK");
        Maps.add("MIRAMAR");
        Maps.add("VIKENDI");
        Maps.add("ARCADE");
        Maps.add("TDM");
        for (String s:
             Maps) {
            mapinsertion.child(s).setValue(s);

        }
        internet_receiver = new internet_receiver(this);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internet_receiver, intentFilter);




        tokenId();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseReference redeeemref=FirebaseDatabase.getInstance().getReference("Redemption_Requests");

        redeeemref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                View view=MenuItemCompat.getActionView(navigationView.getMenu().getItem(1));
                final TextView count=view.findViewById(R.id.cart_badge);
                if (dataSnapshot.exists())
                {

                    if(dataSnapshot.getChildrenCount()>0) {

                        count.setText(String.valueOf((int) dataSnapshot.getChildrenCount()));
                        count.setVisibility(View.VISIBLE);
                    }
                    else{
                        count.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    count.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        databaseReference=FirebaseDatabase.getInstance().getReference("Users_Admin").child(account.getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    RegisterData registerData = dataSnapshot.getValue(RegisterData.class);

                    if (registerData.block.equals("true")) {

                        Intent i = new Intent(Admin_Interface.this, Blocked.class);
                        startActivity(i);
                        finish();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Match_Players_Joined");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
 navigationView= findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setActionView(R.layout.badge_action_layout);
        Profile = navigationView.getHeaderView(0).findViewById(R.id.profile);
        name = navigationView.getHeaderView(0).findViewById(R.id.dname);
        mail = navigationView.getHeaderView(0).findViewById(R.id.dmail);
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(Profile);
        name.setText(firebaseUser.getDisplayName());
        mail.setText(firebaseUser.getEmail());
        Menu me = null;


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
              R.id.nav_home,R.id.nav_gallery,R.id.nav_slideshow,R.id.nav_transactiions,R.id.nav_users)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin__interface, menu);


                return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.report:
                Intent emailIntent;


                emailIntent = new Intent(Intent.ACTION_SENDTO);


                Uri data = Uri.parse("mailto:");
                emailIntent.setData(data);
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"srgamerz1432@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "*Report Issues");

                startActivity(Intent.createChooser(emailIntent, "Insert title for dialog box."));break;


               case R.id.logout:
                logout();

                break;
            case R.id.share:
                Toast.makeText(getApplicationContext(),"Sharing",Toast.LENGTH_LONG).show();
                Intent share= new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TITLE,"Zero Gaming");
             share.putExtra(Intent.EXTRA_TEXT,"Hello Guys check Out My application @:  "+"https://drive.google.com/file/d/1ujdJgPK3llKaaqnmIklD4MZjf3Eix2-r/view?usp=sharing");
                startActivity(Intent.createChooser(share,"Select  the app to share MasterSRGamerz"));




                break;
            case R.id.changephnno:
                final DatabaseReference df= FirebaseDatabase.getInstance().getReference("BHIM_UPI");
                final String[] bhim = new String[1];
                final GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(Admin_Interface.this);
                final DatabaseReference db= FirebaseDatabase.getInstance().getReference("Users_Admin").child(account.getId()).child("phnno");
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        View view=LayoutInflater.from(Admin_Interface.this).inflate(R.layout.popup,null);
                        final EditText phn,upi;
                        phn= view.findViewById(R.id.popphn);
                        upi=view.findViewById(R.id.popupi);
                        phn.setSelectAllOnFocus(true);
                        upi.setSelectAllOnFocus(true);
                        df.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               upi.setText(dataSnapshot.getValue(String.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        AlertDialog dialog = null;
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Admin_Interface.this);
                        if(account.getEmail().equals("rameshyadavchalla@gmail.com")){
                          upi.setEnabled(true);
                                               }
                        alertDialog.setIcon(R.drawable.ic_assignment_black_24dp);
                        alertDialog.setTitle(Html.fromHtml("<font color='#04000C' ><b>Basic Details</b></font>"));
                         phn.setText(dataSnapshot.getValue(String.class));
                        alertDialog.setView(view);

                       alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                db.setValue(phn.getText().toString());

                                df.setValue(upi.getText().toString());
                                Toast.makeText(getApplicationContext(),"Data updated",Toast.LENGTH_LONG).show();
                         //       dialog.cancel();
                            }
                        });
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"Data remained unchanged",Toast.LENGTH_LONG).show();
                            }
                        });
                        dialog = alertDialog.create();
                        dialog.show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;





        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    private void logout() {
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(Admin_Interface.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }
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

                        else {
                                                     String token_id = task.getResult().getToken();
                            GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(Admin_Interface.this);
                            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Admin_Tokens");
                            reference.child(account.getId()).setValue(token_id);
                            if(account.getEmail().equals("rameshyadavchalla@gmail.com")) {
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference("SuperAdmintoken");
                                db.setValue(token_id).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                      //  Toast.makeText(getApplicationContext(),"Tokent inserted",Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                        }
                        // Log and toast
                        //   Toast.makeText(MainActivity.this, token_id, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void getNoConnectionValue(String text) {



                Snackbar.make(findViewById(R.id.drawer_layout),"Offline",Snackbar.LENGTH_INDEFINITE)
                 .setActionTextColor(ContextCompat.getColor(Admin_Interface.this, R.color.halfFilled)).setBackgroundTint(ContextCompat.getColor(Admin_Interface.this, R.color.colorPrimaryDark)).show();;




    }

    @Override
    public void getYesConnectionValue(String text) {
        internetcheck=1;

            Snackbar.make(findViewById(R.id.drawer_layout),"Online",Snackbar.LENGTH_SHORT)
                  .setActionTextColor(ContextCompat.getColor(Admin_Interface.this, R.color.halfFilled)).setBackgroundTint(ContextCompat.getColor(Admin_Interface.this, R.color.colorPrimaryDark)).show();;



    }
    @SuppressLint("ResourceType")
    private void AppDemo() {
        new MaterialTapTargetSequence()
                .addPrompt(new MaterialTapTargetPrompt.Builder(Admin_Interface.this)
                        .setPrimaryText("App Sharing")

                        .setSecondaryText("Share allows to share MasterSRGamerz app through different apps ")
                        .setTarget(findViewById(R.id.share))
                        .create(), 5000)
                .addPrompt(new MaterialTapTargetPrompt.Builder(Admin_Interface.this)
                        .setTarget(findViewById(R.id.report))
                        .setPrimaryText("Report")
                        .setSecondaryText("You can report any issue using this option  ")

                        .create(), 5000)


                .show();
        appdemo.setValue(1);
    }

}
