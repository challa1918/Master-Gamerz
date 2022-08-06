package com.example.mastersrgamerz.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mastersrgamerz.Model.RegisterData;
import com.example.mastersrgamerz.Notification.Api;
import com.example.mastersrgamerz.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountRequestAdapter extends RecyclerView.Adapter<AccountRequestAdapter.ViewHolder> {
   public  ArrayList<RegisterData> requests;
    Context context;
  public  AccountRequestAdapter(ArrayList<RegisterData> requests, Context context){
        this.requests=requests;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.final_account_request,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                final RegisterData registerData=requests.get(position);
                holder.name.setText(registerData.NAME);
        Glide.with(context).load(registerData.profilepic).into(holder.profilepic);
        holder.verify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DatabaseReference verifying= FirebaseDatabase.getInstance().getReference("Users_Admin").child(registerData.UID);
                    verifying.child("verification").setValue("Success").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,"Verification Successful",Toast.LENGTH_LONG).show();
                            int flag=1;
                            sendNotification(registerData.tokenid,registerData.NAME,flag);

                        }
                    });
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder= new MaterialAlertDialogBuilder(context);
                materialAlertDialogBuilder.setIcon(R.drawable.delt);
                materialAlertDialogBuilder.setMessage(Html.fromHtml("<font color='#db0101'><b>Are you sure you want to delete account request ?</b></font>"));

                materialAlertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference verifying= FirebaseDatabase.getInstance().getReference("Users_Admin").child(registerData.UID);
                        verifying.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context,"Request deleted successfully",Toast.LENGTH_LONG).show();
                                int flag=0;
                                sendNotification(registerData.tokenid,registerData.NAME,flag);
                            }
                        });
                    }
                });
                materialAlertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                materialAlertDialogBuilder.create().show();

            }
        });

    }

    private void sendNotification(String token,String name,int flag) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dbtry-c5fbe.web.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        String body;
        if(flag==1)
            body="Hello "+name+" your account is successfully registered";
        else
            body="Hello "+name+" your account is rejected by the app admin";
        Call<ResponseBody> call = api.sendNotification(token, "Account  Verification ",body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
TextView name;
Switch verify;
ImageView profilepic,delete;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.requestadminname);
            verify=itemView.findViewById(R.id.requestadminverify);
            profilepic=itemView.findViewById(R.id.requestadminprofile);

                delete=itemView.findViewById(R.id.deleteaccountrequest);
        }
    }

}
