package com.example.mastersrgamerz.ui.slideshow;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mastersrgamerz.Adapter.ViewPagerAdapter;
import com.example.mastersrgamerz.Admin_Interface;
import com.example.mastersrgamerz.Model.User;
import com.example.mastersrgamerz.Notification.Api;
import com.example.mastersrgamerz.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    TextInputLayout mtitle,mbody;
    MaterialButton msend;
    ViewPager2 viewPager;
    View root;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;
    DatabaseReference notidemo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
         root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        GoogleSignInAccount acount= GoogleSignIn.getLastSignedInAccount(root.getContext());
        notidemo=FirebaseDatabase.getInstance().getReference("Users_Admin").child(acount.getId()).child("notidemo");
        viewPager=root.findViewById(R.id.viewpager);
        viewPager.setAdapter(new ViewPagerAdapter((FragmentActivity) getContext()));
        tabLayout=root.findViewById(R.id.tabLayout);

        TabLayoutMediator tabLayoutMediator= new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch(position){
                            case 1:
                                tab.setText("Room Id");
                                tab.setIcon(R.drawable.roo);
                                break;
                            case 0:

                                tab.setText("Notification");
                                tab.setIcon(R.drawable.chatmessage);
                                break;
                        }
            }
        });

        tabLayoutMediator.attach();




        return root;
    }




}
