package com.example.mastersrgamerz.ui.slideshow;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.mastersrgamerz.Model.Message;
import com.example.mastersrgamerz.Model.Players;
import com.example.mastersrgamerz.Model.User;
import com.example.mastersrgamerz.Notification.Api;
import com.example.mastersrgamerz.Payment;
import com.example.mastersrgamerz.ProgressButton;
import com.example.mastersrgamerz.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class notificationfragment extends Fragment {
    View view;
    static int f=0;
    RadioGroup inappmsg;
    boolean sendinappmsg;
    DatabaseReference msginput;
    TextInputLayout mtitle,mbody;
    MaterialButton msend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.notification_fragment,null);
      mtitle=view.findViewById(R.id.title);
          mbody=view.findViewById(R.id.body);
        msend=view.findViewById(R.id.msend);
        inappmsg=view.findViewById(R.id.inappmessage);
       msginput=FirebaseDatabase.getInstance().getReference("Users_Client").child("Messages");
        inappmsg.check(R.id.msgno);
        Date date1 = new Date();
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        mtitle.getEditText().requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mtitle.getEditText(), InputMethodManager.SHOW_IMPLICIT);
        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ProgressDialog progressDialog=new ProgressDialog(getContext());
                progressDialog.setMessage("Sending Message");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                if (mtitle.getEditText().getText().toString().equals("") || mbody.getEditText().getText().toString().equals("")) {
                    Toast.makeText(view.getContext(), "Fill all the details", Toast.LENGTH_SHORT).show();

                } else {

                    progressDialog.show();
                    /*final ProgressDialog[] progressDialog = new ProgressDialog[1];
                    progressDialog[0] = new ProgressDialog(view.getContext());
                    progressDialog[0].setMessage("Transferring");
                    progressDialog[0].setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog[0].setIndeterminate(true);
                    progressDialog[0].show();*/

                    if(inappmsg.getCheckedRadioButtonId()==R.id.msgyes){
                        DatabaseReference drf=FirebaseDatabase.getInstance().getReference("Registered_Clients");
                        drf.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                System.out.println("InAppMessaging : "+dataSnapshot.getValue());
                                for (DataSnapshot ds:
                                     dataSnapshot.getChildren()) {
                                    String token = ds.getValue(String.class);
                                    sendNotification(token);
                                    Message notificationinappmsg= new Message(mtitle.getEditText().getText().toString(),mbody.getEditText().getText().toString(),dateFormat.format(date1),false);
                                    System.out.println("User Id :  "+ds.getKey());
                                    msginput.child(ds.getKey()).push().setValue(notificationinappmsg);
                                }
                                Toast.makeText(view.getContext(), "Notification Sent Successfully ", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });




                    }else{
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Clients_tokens");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                System.out.println(dataSnapshot.getValue());
                                for (DataSnapshot ds :
                                        dataSnapshot.getChildren()) {

                                    String token = ds.getValue(String.class);
                                    sendNotification(token);
                                }

                                Toast.makeText(view.getContext(), "Notification Sent Successfully ", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }


                }



            }
     });
        return view;
    }
    private void sendNotification(String token) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dbtry-c5fbe.web.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.sendNotification(token, mtitle.getEditText().getText().toString(), mbody.getEditText().getText().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mtitle.getEditText().setText("");
                mbody.getEditText().setText("");
                inappmsg.check(R.id.msgno);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

}
