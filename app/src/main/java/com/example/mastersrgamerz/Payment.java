package com.example.mastersrgamerz;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mastersrgamerz.Model.User;
import com.example.mastersrgamerz.Model.User_transactions;
import com.example.mastersrgamerz.Model.redeemtransaction;
import com.example.mastersrgamerz.Notification.Api;
import com.example.mastersrgamerz.ui.gallery.GalleryFragment;
import com.example.mastersrgamerz.ui.gallery.redeemlist;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
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

import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Payment extends AppCompatActivity {
    final int UPI_PAYMENT = 123;
    TextInputLayout name, upi, amount;
    ImageView transfer,payback;
    Intent i;
    String pubgid,pubgusername;
    int accountbal;
    ImageView paytm,gpay;
    String title, token, body;


    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable();
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        paytm=findViewById(R.id.paytm);
        gpay=findViewById(R.id.gpay);
        name = findViewById(R.id.payname);
        upi = findViewById(R.id.payupi);
        amount=findViewById(R.id.payamount);

        title = "Redemption done!!!";
        payback=findViewById(R.id.playersback);
        i = getIntent();
        name.getEditText().setText(i.getStringExtra("name"));
        upi.getEditText().setText(i.getStringExtra("upi"));
        amount.getEditText().setText(i.getStringExtra("amount"));
        pubgid = i.getStringExtra("pid");
        pubgusername = i.getStringExtra("pun");
        System.out.println("Username Intent :"+pubgusername);
        //Toast.makeText(getApplicationContext(),pubgusername,Toast.LENGTH_LONG).show();

        payback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        gpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getEditText().toString().trim())) {
                    Toast.makeText(Payment.this, " Name is invalid", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(upi.getEditText().getText().toString().trim())) {
                    Toast.makeText(Payment.this, " UPI ID is invalid", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(amount.getEditText().getText().toString().trim())) {
                    Toast.makeText(Payment.this, " Amount is invalid", Toast.LENGTH_SHORT).show();
                }else {
                    payUsingUpi(name.getEditText().getText().toString(), upi.getEditText().getText().toString(),
                            pubgid, amount.getEditText().getText().toString(),1,v);
                }


            }
        });
        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getEditText().toString().trim())) {
                    Toast.makeText(Payment.this, " Name is invalid", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(upi.getEditText().getText().toString().trim())) {
                    Toast.makeText(Payment.this, " UPI ID is invalid", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(amount.getEditText().getText().toString().trim())) {
                    Toast.makeText(Payment.this, " Amount is invalid", Toast.LENGTH_SHORT).show();
                }else {
                    payUsingUpi(name.getEditText().getText().toString(), upi.getEditText().getText().toString(),
                            pubgid, amount.getEditText().getText().toString(),2,v);
                }
            }
        });

    }



    void payUsingUpi(String name, String upiId, String note, String amount,int upi,View v) {
        Log.e("main ", "name " + name + "--up--" + upiId + "--" + note + "--" + amount);
        final Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("refUrl", "https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid=YOUR_MID_HERE&orderId=ORDERID_98765")
                .build();




        switch(upi){

            case 1:
                String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
                int GOOGLE_PAY_REQUEST_CODE = 123;
                boolean isAppInstalled = appInstalledOrNot(GOOGLE_PAY_PACKAGE_NAME);
                if(isAppInstalled) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
                    startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
                }else{
                    Snackbar.make(v,"Gpay is not installed in your phone", Snackbar.LENGTH_SHORT).setBackgroundTint(ContextCompat.getColor(Payment.this,R.color.colorPrimaryDark)).show();

                }

                break;
            case 2:

                String PAYTM_PACKAGE_NAME="net.one97.paytm";
                boolean isappInstalled = appInstalledOrNot(PAYTM_PACKAGE_NAME);
                int PAYTM_REQUEST_CODE = 123;
if(isappInstalled) {
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(uri);
    i.setPackage(PAYTM_PACKAGE_NAME);
    startActivityForResult(i, PAYTM_REQUEST_CODE);
}else{
    Snackbar.make(v,"Paytm is not installed in your phone", Snackbar.LENGTH_SHORT).setBackgroundTint(ContextCompat.getColor(Payment.this,R.color.colorPrimaryDark)).show();

}
                break;


        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response " + resultCode);
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(Payment.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String[] response = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String[] equalStr = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                SuccessTasks();



                Toast.makeText(Payment.this, "Transaction successful.", Toast.LENGTH_SHORT).show();

                Log.e("UPI", "payment successfull: " + approvalRefNo);

                //finish();
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(Payment.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                Toast.makeText(Payment.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(Payment.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void SuccessTasks() {


        FirebaseFirestore db=FirebaseFirestore.getInstance();

        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        progressDialog[0] = new ProgressDialog(Payment.this);
        progressDialog[0].setMessage("Transferring");
        progressDialog[0].setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog[0].setIndeterminate(true);
        progressDialog[0].show();
        final Double[] newWinningsValue = new Double[1];
        final Double[] newWalletValue = new Double[1];
        final DocumentReference documentReference;
        documentReference = db.collection("UsersBalance").document(pubgid);
        db.runTransaction(new Transaction.Function<Double>() {
            @Nullable
            @Override
            public Double apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                DocumentSnapshot documentSnapshot=transaction.get(documentReference);
                newWinningsValue[0] =documentSnapshot.getDouble("Winnings")-Integer.parseInt(amount.getEditText().getText().toString());
                transaction.update(documentReference,"Winnings", newWinningsValue[0]);


                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Double>() {
            @Override
            public void onSuccess(Double aDouble) {

                DatabaseReference transactionsref=FirebaseDatabase.getInstance().getReference("Admin_Redeem_Transactions");
                final redeemtransaction transaction= new redeemtransaction();
                GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(Payment.this);
                transaction.from=account.getDisplayName();
                transaction.to=pubgusername;
                transaction.money=amount.getEditText().getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy kk:mm");
                String format = simpleDateFormat.format(new Date());
                Log.d("MainActivity", "Current Timestamp: " + format);
                transaction.date=format;
                transaction.profilepic=String.valueOf(account.getPhotoUrl());
                transactionsref.push().setValue(transaction);


                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users_Client").child("Users").child(pubgid);
                databaseReference.child("winning_money").setValue(newWinningsValue[0]);

                final DatabaseReference dataReference = FirebaseDatabase.getInstance().getReference("Users_Client").child("Users");
                dataReference.orderByChild("PUBG_ID").equalTo(pubgid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = null;
                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {
                            user = ds.getValue(User.class);
                            System.out.println("IN loop: " + user.Name);
                        }
                        if (user.email.equals(name.getEditText().getText().toString())) {

                            final int newbal = Integer.parseInt(amount.getEditText().getText().toString());
                            final User finalUser = user;
                            final User finalUser1 = user;
                            dataReference.child(user.PUBG_ID).child("winning_money").setValue(user.winning_money - newbal).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), " Bal Updated", Toast.LENGTH_LONG).show();
                                    body =  finalUser.Name + " your redemption amount is successfully credited in your bank account";
                                    token = finalUser.Token_Id;
                                    SendNotification();
                                    Date time=new Date();
                                    DatabaseReference ds= FirebaseDatabase.getInstance().getReference("Users_Client").child("Transactions");
                                    SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy kk:mm");
                                    User_transactions transactions=new User_transactions("0",String.valueOf(newbal),"Withdrawal from wallet ",dateFormat.format(time));
                                    ds.child(finalUser1.PUBG_ID).push().setValue(transactions);
                                }
                            });

                        }




                        final DatabaseReference datadelete= FirebaseDatabase.getInstance().getReference("Redemption_Requests").child(pubgid);
                 datadelete.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         progressDialog[0].cancel();
                                     finish();


                     }
                 });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog[0].dismiss();
                Snackbar.make(payback,"Money transaction successfull value updation failed retry to update ", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SuccessTasks();
                    }
                }).show();
                System.out.println("meeeee"+e.getMessage());
            }
        });




    }


    void SendNotification() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dbtry-c5fbe.web.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.sendNotification(token, title, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(getApplicationContext(), response.body().string(), Toast.LENGTH_SHORT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

}



