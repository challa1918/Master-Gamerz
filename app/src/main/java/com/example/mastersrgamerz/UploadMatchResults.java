package com.example.mastersrgamerz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class UploadMatchResults extends AppCompatActivity {
TextInputLayout match;
Button upload;
ImageView resultdisplay;
LottieAnimationView  uploading,done;
Uri image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_match_results);
        match=findViewById(R.id.postresultmatchid);
        upload=findViewById(R.id.postresultupload);
        resultdisplay=findViewById(R.id.image);
        uploading= findViewById(R.id.uploading);
        done=findViewById(R.id.done);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(match.getEditText().getText().toString().equals("")){
                    match.getEditText().setError("Enter Match Id ");
                    match.getEditText().requestFocus();
                }
                else{
                    uploading.setVisibility(View.VISIBLE);
                    StorageReference storageReference= FirebaseStorage.getInstance().getReference(match.getEditText().getText().toString());
                    storageReference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(UploadMatchResults.this,"Uploaded",Toast.LENGTH_LONG).show();
                            uploading.setVisibility(View.INVISIBLE);
                            done.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
       }



    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
         if (imageUri != null) {
            // Update UI to reflect image being shared
            Toast.makeText(UploadMatchResults.this,"Recieved ",Toast.LENGTH_LONG).show();
            image=imageUri;
             Glide.with(UploadMatchResults.this).load(imageUri).into(resultdisplay);


        }
    }

}
