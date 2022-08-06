package com.example.mastersrgamerz.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.mastersrgamerz.Model.RegisterData;
import com.example.mastersrgamerz.Model.User;
import com.example.mastersrgamerz.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class AdminUsersAdapter extends RecyclerView.Adapter<AdminUsersAdapter.ViewHolder> implements Filterable {
    public ArrayList<RegisterData> requests;
    public ArrayList<RegisterData> allrequests;
    Context context;

    public AdminUsersAdapter(ArrayList<RegisterData> requests, Context context) {
        this.requests = requests;
        this.context = context;
        this.allrequests = new ArrayList<>(requests);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_admin_users, null);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final RegisterData registerData = requests.get(position);
     // holder.scoreviewer.setVisibility(View.INVISIBLE);
  //    holder.kills.setVisibility(View.INVISIBLE);
    //  holder.wins.setVisibility(View.INVISIBLE);
      //holder.matches.setVisibility(View.INVISIBLE);
    holder.name.setText(registerData.NAME);
        Glide.with(context).load(registerData.profilepic).into(holder.profilepic);


      final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account.getEmail().equals("rameshyadavchalla@gmail.com")||account.getEmail().equals("radhakrishna2888@gmail.com")) {
            holder.adminblock.setVisibility(View.VISIBLE);
        }
        if (registerData.verification.equals("Success"))
            holder.verified.setVisibility(View.VISIBLE);

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permission=1;
                if(registerData.phnno.length()==10) {
                    Toast.makeText(context, "calling "+registerData.phnno, Toast.LENGTH_LONG).show();

                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:" + registerData.phnno));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, permission);
                        return;
                    }
                    context.startActivity(i);
                }
                else {
                    Toast.makeText(context, "Invalid phone number", Toast.LENGTH_LONG).show();
                }

            }
        });
        if (registerData.block.equals("true")){
            holder.adminblock.setChecked(true);
            holder.adminblock.setText(holder.adminblock.getTextOn());

        }
        else {
            holder.adminblock.setChecked(false);
            holder.adminblock.setText(holder.adminblock.getTextOff());

        }

        holder.adminblock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users_Admin").child(Objects.requireNonNull(registerData.UID));
                    db.child("block").setValue("true");
                    holder.adminblock.setText(holder.adminblock.getTextOn());

                }else{
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users_Admin").child(Objects.requireNonNull(registerData.UID));
                    db.child("block").setValue("false");

                    holder.adminblock.setText(holder.adminblock.getTextOff());
                }
            }
        });


            }



    @Override
    public int getItemCount() {
        return requests.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<RegisterData> registerDataArrayList= new ArrayList<>();
            if(constraint.toString().isEmpty()){
                registerDataArrayList.addAll(allrequests);

            }else {
                for (RegisterData rd:
                        allrequests) {
                    if(rd.NAME.toLowerCase().contains(constraint.toString().toLowerCase())){
                        registerDataArrayList.add(rd);
                    }

                }

            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=registerDataArrayList;
            return filterResults;


        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            requests.clear();
            requests.addAll((Collection<? extends RegisterData>) results.values);

            notifyDataSetChanged();
        }

    };






    public  class ViewHolder extends RecyclerView.ViewHolder{
TextView name,mail,uid,kills,wins,matches;
Switch adminblock;
ImageView verified;
ImageView profilepic;
LinearLayout call;
MaterialCardView card;


        final GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(context);


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            profilepic=itemView.findViewById(R.id.adminprofile);
            name=itemView.findViewById(R.id.adminname);
adminblock=itemView.findViewById(R.id.adminblock);
verified=itemView.findViewById(R.id.verifiedfinal);

            call=itemView.findViewById(R.id.callinglayout);
      //      kills=itemView.findViewById(R.id.kills);
        //    wins=itemView.findViewById(R.id.wins);
          //  matches=itemView.findViewById(R.id.matchesplayed);







        }
    }

}
