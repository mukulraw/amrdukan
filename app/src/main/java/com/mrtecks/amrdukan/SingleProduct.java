package com.mrtecks.amrdukan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;

public class SingleProduct extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabs;
    ViewPager pager;
    CircleIndicator indicator;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        toolbar = findViewById(R.id.toolbar2);
        tabs = findViewById(R.id.tabLayout);
        pager = findViewById(R.id.viewPager2);
        indicator = findViewById(R.id.indicator);

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Product Title");

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        tabs.addTab(tabs.newTab().setCustomView(getCustomView(inflater)));
        tabs.addTab(tabs.newTab().setCustomView(getCustomView(inflater)));
        tabs.addTab(tabs.newTab().setCustomView(getCustomView(inflater)));
        tabs.addTab(tabs.newTab().setCustomView(getCustomView(inflater)));

        tabs.setupWithViewPager(pager);


        tabs.getTabAt(0).setCustomView(getCustomView(inflater));
        tabs.getTabAt(1).setCustomView(getCustomView(inflater));
        tabs.getTabAt(2).setCustomView(getCustomView(inflater));
        tabs.getTabAt(3).setCustomView(getCustomView(inflater));

    }

    private View getCustomView(LayoutInflater inflater) {
        View view = getLayoutInflater().inflate(R.layout.tabs_layout, null);

        return view;
    }


    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new page();
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public static class page extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.page3, container, false);

            return view;
        }
    }

}