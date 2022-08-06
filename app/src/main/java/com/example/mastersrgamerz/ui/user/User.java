package com.example.mastersrgamerz.ui.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mastersrgamerz.Adapter.UserViewPagerAdapter;
import com.example.mastersrgamerz.Adapter.ViewPagerAdapter;
import com.example.mastersrgamerz.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


/**

 */
public class User extends Fragment {



    public User() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewPager2 viewPager;
        TabLayout tabLayout;
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_user, container, false);
        viewPager=root.findViewById(R.id.viewpageruser);
        viewPager.setAdapter(new UserViewPagerAdapter((FragmentActivity) getContext()));
        tabLayout=root.findViewById(R.id.userstab);
        TabLayoutMediator tabLayoutMediator= new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){
                    case 0:
                        tab.setText("Admin");
                        tab.setIcon(R.drawable.adminfinal);
                        break;
                    case 1:
                        tab.setText("Client");
                        tab.setIcon(R.drawable.clientfinal);
                        break;

                }
            }
        });

        tabLayoutMediator.attach();
        return root;
    }

}
