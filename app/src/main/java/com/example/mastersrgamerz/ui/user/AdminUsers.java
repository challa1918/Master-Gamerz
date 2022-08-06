package com.example.mastersrgamerz.ui.user;

import android.content.DialogInterface;
import android.graphics.ColorSpace;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Parcelable;
import android.text.InputType;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mastersrgamerz.Adapter.AdminUsersAdapter;
import com.example.mastersrgamerz.Model.RegisterData;
import com.example.mastersrgamerz.R;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class AdminUsers extends Fragment {
ArrayList<RegisterData> adminuserslist;
DatabaseReference datab;
AdminUsersAdapter adminUsersAdapter;
FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private final String KEY_RECYCLER_STATE = "recycler_state";

    private static Bundle mBundleRecyclerViewState;
FirebaseRecyclerOptions options;
RecyclerView recyclerView;
    private Parcelable recyclerViewState;
    SearchView searchView;
    TextView count;
    public AdminUsers() {
        // Required empty public constructor
        datab= FirebaseDatabase.getInstance().getReference("Users_Admin");
        adminuserslist=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root=inflater.inflate(R.layout.fragment_admin_users, container, false);
        recyclerView=root.findViewById(R.id.adminuserslist);
        searchView=root.findViewById(R.id.searchView);
        count=root.findViewById(R.id.adminuserscount);
        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(root.getContext());
        final DatabaseReference db=FirebaseDatabase.getInstance().getReference("Users_Admin").child(account.getId()).child("phnno");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().equals("0")){

                    AlertDialog dialog;
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(root.getContext());
                    alertDialog.setTitle("Phone Number");
                    final EditText input = new EditText(root.getContext());
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setSelectAllOnFocus(true);
                    alertDialog.setView(input);
                    alertDialog.setIcon(R.drawable.phn);
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.setValue(input.getText().toString());
                            dialog.cancel();
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.setValue("Phone Number");
                            Snackbar.make(root,"Phone number isn't saved make sure to save one by clicking Update Details",Snackbar.LENGTH_LONG)
                                    .setActionTextColor(ContextCompat.getColor(root.getContext(), R.color.halfFilled)).setBackgroundTint(ContextCompat.getColor(root.getContext(), R.color.colorPrimaryDark)).show();;


                        }
                    });
                    dialog = alertDialog.create();
                    dialog.setCancelable( false );
                    dialog.show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL);


        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        datab.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Bundle mBundleRecyclerViewState = new Bundle();
                Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
                adminuserslist.clear();
                for (DataSnapshot ds:
                     dataSnapshot.getChildren()) {
                    RegisterData registerData=ds.getValue(RegisterData.class);
                    System.out.println("AdminUser: "+registerData.NAME);
                    adminuserslist.add(registerData);
                }
                System.out.println("List :   \n"+adminuserslist);
                // Save state

                count.setText(String.valueOf(adminuserslist.size()));
                adminUsersAdapter= new AdminUsersAdapter(adminuserslist,root.getContext());
                recyclerView.setHasFixedSize(true);
                if (mBundleRecyclerViewState != null) {
                    Parcelable listStat = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    recyclerView.getLayoutManager().onRestoreInstanceState(listStat);
                }
                             recyclerView.setAdapter(adminUsersAdapter);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        if(adminuserslist.contains(query)){

                        }else{
                            Toast.makeText(getContext(), "No Match found",Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                       adminUsersAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        return  root;
    }


}
