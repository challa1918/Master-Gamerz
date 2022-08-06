package com.example.mastersrgamerz.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.mastersrgamerz.Model.User;
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

public class ClientUsersAdapter extends RecyclerView.Adapter<ClientUsersAdapter.ViewHolder> implements Filterable {
    public ArrayList<User> requests;
    public ArrayList<User> allrequests;
    Context context;

    public ClientUsersAdapter(ArrayList<User> requests, Context context) {
        this.requests = requests;
        this.context = context;
        this.allrequests = new ArrayList<>(requests);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, null);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final User user = requests.get(position);

        holder.player.setImageResource(R.drawable.pubgid);

        System.out.println("Client Adapter: "+user.Name);
        holder.kills.setText(user.Kills);
        holder.wins.setText(user.Wins);
        holder.matches.setText(String.valueOf(user.no_of_matches));
        holder.name.setText(user.Name);
        Glide.with(context).load(user.photo_url).into(holder.profilepic);
        holder.mail.setText(user.email);
        holder.uid.setText(user.PUBG_USERNAME);
        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);

            holder.block.setVisibility(View.VISIBLE);


        if (user.block.equals("yes")){
            holder.block.setChecked(true);
            holder.block.setText(holder.block.getTextOn());
            holder.card.setStrokeColor(ColorStateList.valueOf(Color.RED));
           // holder.card.setCardBackgroundColor();
        }
        else {
            holder.block.setChecked(false);
            holder.block.setText(holder.block.getTextOff());

        }

        holder.block.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users_Client").child("Users").child(Objects.requireNonNull(user.PUBG_ID));
                    db.child("block").setValue("yes");
                    holder.block.setText(holder.block.getTextOn());
                    holder.card.setStrokeColor(ColorStateList.valueOf(Color.RED));


                }else{
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users_Client").child("Users").child(Objects.requireNonNull(user.PUBG_ID));
                    db.child("block").setValue("no");
                    holder.block.setText(holder.block.getTextOff());
                  holder.card.setStrokeColor(ColorStateList.valueOf(Color.TRANSPARENT));

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
            ArrayList<User> userArrayList= new ArrayList<>();
            if(constraint.toString().isEmpty()){
                userArrayList.addAll(allrequests);

            }else {
                for (User rd:
                        allrequests) {
                    if(rd.Name.toLowerCase().contains(constraint.toString().toLowerCase())){
                        userArrayList.add(rd);
                    }

                }

            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=userArrayList;
            return filterResults;


        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            requests.clear();
            requests.addAll((Collection<? extends User>) results.values);

            notifyDataSetChanged();
        }

    };






    public  class ViewHolder extends RecyclerView.ViewHolder{
TextView name,mail,uid,kills,wins,matches;
Switch block;
LottieAnimationView verified,phncall;

ImageView profilepic,player;
MaterialCardView card;
Button call;

        final GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(context);


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            profilepic=itemView.findViewById(R.id.userprofile);
            name=itemView.findViewById(R.id.userdisname);
               block=itemView.findViewById(R.id.userblock);
               uid=itemView.findViewById(R.id.userid);

               mail=itemView.findViewById(R.id.usermail);
               card=itemView.findViewById(R.id.card);
               kills=itemView.findViewById(R.id.kills);
               wins=itemView.findViewById(R.id.wins);
               matches=itemView.findViewById(R.id.matchesplayed);
               player=itemView.findViewById(R.id.adminid);







        }
    }

}
