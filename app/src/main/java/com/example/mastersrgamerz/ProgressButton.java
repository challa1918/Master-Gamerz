package com.example.mastersrgamerz;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.card.MaterialCardView;

public class ProgressButton {
    ConstraintLayout layout;
    TextView textView;
    MaterialCardView materialCardView;
    ProgressBar progressBar;


    ProgressButton(Context context, View view){
        materialCardView=view.findViewById(R.id.buttoncardview);
        layout=view.findViewById(R.id.buttonlayout);
        textView=view.findViewById(R.id.buttontextview);
        progressBar=view.findViewById(R.id.buttonprogress);

    }
    public void buttonActivated(){
      progressBar.setVisibility(View.VISIBLE);
      textView.setText("Please wait....");
    }
    public void buttonFinished(){
        progressBar.setVisibility(View.GONE);
        textView.setText("Participants");
    }
}
