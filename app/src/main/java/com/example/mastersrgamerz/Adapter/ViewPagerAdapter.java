package com.example.mastersrgamerz.Adapter;

import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mastersrgamerz.ui.slideshow.notificationfragment;
import com.example.mastersrgamerz.ui.slideshow.roomidfragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
List<Fragment> fragmentList;
List<String>fragmenttitlelist;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:

            return  new roomidfragment();

            case 0:
                return  new notificationfragment();

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
