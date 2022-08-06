package com.example.mastersrgamerz.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mastersrgamerz.ui.user.AdminUsers;
import com.example.mastersrgamerz.ui.user.ClientUsers;

public class UserViewPagerAdapter extends FragmentStateAdapter {

    public UserViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return  new AdminUsers();

            case 1:
                return  new ClientUsers();
            default:
                return null;

        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
