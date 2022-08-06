package com.example.mastersrgamerz.ui.slideshow;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.AndroidException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mastersrgamerz.Model.Players;
import com.example.mastersrgamerz.Model.User;
import com.example.mastersrgamerz.Notification.Api;
import com.example.mastersrgamerz.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class roomidfragment extends Fragment {
    View view;
    TextInputLayout rid,rpass;
    Spinner rmid;
    MaterialButton rupdate;
    String MatchID;
    ArrayList<String> matchid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.roomid_fragment,null);
    //    getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        rmid=view.findViewById(R.id.rmid);
        rid=view.findViewById(R.id.roomid);
        rpass=view.findViewById(R.id.password);
        rupdate=view.findViewById(R.id.rupdate);
        matchid= new ArrayList<>();



        DatabaseReference db=FirebaseDatabase.getInstance().getReference("Matches");
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                matchid.clear();
                matchid.add("Match Id");
                for (DataSnapshot ds:
                     dataSnapshot.getChildren()) {
                    System.out.println("Matches Id:   "+ds.getKey());
                    matchid.add(ds.getKey());

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        view.getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        matchid
                );
adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);

                rmid.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        rupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog=new ProgressDialog(getContext());
                progressDialog.setMessage("Sending Message");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);


                if(rmid.getSelectedItem().toString().equals("Match Id")||rid.getEditText().getText().toString().equals("")||rpass.getEditText().getText().toString().equals("")){
                    Toast.makeText(view.getContext(),"Fill all the Credentials",Toast.LENGTH_LONG).show();
                }
                else{
                    MatchID= (String) rmid.getSelectedItem();
                    rupdate.setClickable(false);
                 progressDialog.show();
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Matches").child(rmid.getSelectedItem().toString());
                    databaseReference.child("roomId").setValue(rid.getEditText().getText().toString());
                    databaseReference.child("password").setValue(rpass.getEditText().getText().toString());
               //     Toast.makeText(view.getContext(),"Details Uploaded Successfully",Toast.LENGTH_LONG).show();
                     databaseReference = FirebaseDatabase.getInstance().getReference("Matches_Going").child(MatchID);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds :
                                    dataSnapshot.getChildren()) {
                                Players player=ds.getValue(Players.class);
                                System.out.println("Notification Sendingon roomid update to:  " +player.PUBG_ID);
                                sendNotification(player.Token_Id,rid.getEditText().getText().toString(),rpass.getEditText().getText().toString());
                            }
                            progressDialog.dismiss();
                            Toast.makeText(view.getContext(), "Room Details Sent Successfully !!", Toast.LENGTH_SHORT).show();

                        }

                        private void sendNotification(String token,String roomid,String rooompass) {


                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://dbtry-c5fbe.web.app/api/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            Api api = retrofit.create(Api.class);
                            Call<ResponseBody> call = api.sendNotification(token, "Match: "+MatchID,"RoomId: "+roomid+" Password: "+rooompass);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    rid.getEditText().setText("");
                                    rpass.getEditText().setText("");

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(view.getContext(), "Notification Failed to send!! Retry Again...", Toast.LENGTH_LONG).show();
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });





        return view;
    }

}
