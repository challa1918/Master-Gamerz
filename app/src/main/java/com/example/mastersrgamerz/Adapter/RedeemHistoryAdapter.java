package com.example.mastersrgamerz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mastersrgamerz.Model.Match_details;
import com.example.mastersrgamerz.Model.redeemtransaction;
import com.example.mastersrgamerz.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RedeemHistoryAdapter extends RecyclerView.Adapter <RedeemHistoryAdapter.ViewHolder>{
ArrayList<redeemtransaction> transactionlist;
Context context;

public RedeemHistoryAdapter(ArrayList<redeemtransaction> transactionlist,Context context)
{
    this.transactionlist=transactionlist;
    this.context=context;
}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.redemption_history_listitem,null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        redeemtransaction transaction=transactionlist.get(position);
        Glide.with(context)
                .load(transaction.profilepic)
                .into(holder.profilepic);
        holder.sender.setText(transaction.from);
        holder.receiver.setText(transaction.to);
        holder.amount.setText("-"+transaction.money);
        holder.date.setText(transaction.date);

    }


    @Override
    public int getItemCount() {
        return transactionlist.size();
    }


  public  class ViewHolder extends RecyclerView.ViewHolder{
CircleImageView profilepic;
TextView sender,receiver,date,amount;

      public ViewHolder(@NonNull View itemView) {
          super(itemView);
          profilepic=itemView.findViewById(R.id.profilepic);
          sender=itemView.findViewById(R.id.sender);
          receiver=itemView.findViewById(R.id.receiver);
          date=itemView.findViewById(R.id.date);
          amount=itemView.findViewById(R.id.amount);
      }
  }
}
