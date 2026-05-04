package com.opositores.android.ui.oppositions;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.opositores.android.databinding.ActivityOppositionDetailBinding;
import com.opositores.android.viewmodel.OppositionViewModel;

public class OppositionDetailActivity extends AppCompatActivity {

    public static final String EXTRA_OPPOSITION_ID = "opposition_id";
    public static final String EXTRA_OPPOSITION_NAME = "opposition_name";

    private ActivityOppositionDetailBinding binding;
    private long oppositionId;

    private static final String[] TAB_TITLES = {"Temario", "Plan", "Estadísticas"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOppositionDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        oppositionId = getIntent().getLongExtra(EXTRA_OPPOSITION_ID, -1);
        String name = getIntent().getStringExtra(EXTRA_OPPOSITION_NAME);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupTabs();
    }

    private void setupTabs() {
        OppositionDetailPagerAdapter pagerAdapter =
                new OppositionDetailPagerAdapter(this, oppositionId);
        binding.viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, pos) -> tab.setText(TAB_TITLES[pos])).attach();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
