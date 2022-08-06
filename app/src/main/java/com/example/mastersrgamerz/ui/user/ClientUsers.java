package com.example.mastersrgamerz.ui.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mastersrgamerz.Adapter.ClientUsersAdapter;
import com.example.mastersrgamerz.Model.User;
import com.example.mastersrgamerz.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnPausedListener;

import java.util.ArrayList;


public class ClientUsers extends Fragment {
    private final String KEY_RECYCLER_STATE = "recycler_state";

    private static Bundle mBundleRecyclerViewState;
    ArrayList<User> clientuserslist;
    DatabaseReference db;
    ClientUsersAdapter clientUsersAdapter;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseRecyclerOptions options;
    RecyclerView recyclerView;
    SearchView searchView;
    TextView count;
    private Parcelable recyclerViewState;
    public ClientUsers() {
        // Required empty public constructor
        db= FirebaseDatabase.getInstance().getReference("Users_Client").child("Users");
        clientuserslist=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root=inflater.inflate(R.layout.fragment_client_users, container, false);
        recyclerView=root.findViewById(R.id.clientuserslist);
        searchView=root.findViewById(R.id.clietusersearchView);
        count=root.findViewById(R.id.clientuserscount);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Bundle mBundleRecyclerViewState = new Bundle();
                Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
                clientuserslist.clear();
                for (DataSnapshot ds:
                        dataSnapshot.getChildren()) {
                    User user=ds.getValue(User.class);
                    System.out.println("ClientUser: "+user.Name);
                    clientuserslist.add(user);
                }
                System.out.println("List :   \n"+clientuserslist);
                // Save state


                count.setText(String.valueOf(clientuserslist.size()));
                clientUsersAdapter= new ClientUsersAdapter(clientuserslist,root.getContext());
                recyclerView.setHasFixedSize(true);

                if (mBundleRecyclerViewState != null) {
                    Parcelable listStat = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    recyclerView.getLayoutManager().onRestoreInstanceState(listState);
                }
                recyclerView.setAdapter(clientUsersAdapter);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        if(clientuserslist.contains(query)){

                        }else{
                            Toast.makeText(getContext(), "No Match found",Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        clientUsersAdapter.getFilter().filter(newText);
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
