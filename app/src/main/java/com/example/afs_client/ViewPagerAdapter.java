package com.example.afs_client;

import com.example.afs_client.fragments.LogFragment;
import com.example.afs_client.fragments.MonitorFragment;
import com.example.afs_client.fragments.StatsFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

class ViewPagerAdapter extends FragmentStateAdapter {

    private final int TABS_COUNT = 3;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            default: return MonitorFragment.newInstance();
            case 1: return StatsFragment.newInstance();
            case 2: return LogFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return TABS_COUNT;
    }
}
