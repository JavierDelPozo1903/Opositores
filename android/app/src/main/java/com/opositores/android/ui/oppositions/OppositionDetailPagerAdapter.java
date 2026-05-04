package com.opositores.android.ui.oppositions;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.opositores.android.ui.topics.TopicListFragment;
import com.opositores.android.ui.planning.PlanFragment;
import com.opositores.android.ui.stats.StatsFragment;

public class OppositionDetailPagerAdapter extends FragmentStateAdapter {

    private final long oppositionId;

    public OppositionDetailPagerAdapter(FragmentActivity activity, long oppositionId) {
        super(activity);
        this.oppositionId = oppositionId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle args = new Bundle();
        args.putLong("oppositionId", oppositionId);

        Fragment fragment;
        switch (position) {
            case 0: fragment = new TopicListFragment(); break;
            case 1: fragment = new PlanFragment(); break;
            default: fragment = new StatsFragment(); break;
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() { return 3; }
}
