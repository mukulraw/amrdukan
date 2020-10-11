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
import android.widget.ImageView;

import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;

public class Category3 extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager banners;
    CircleIndicator indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category3);

        toolbar = findViewById(R.id.toolbar2);
        banners = findViewById(R.id.viewPager);
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

        String title = getIntent().getStringExtra("title");

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(title);

        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        banners.setAdapter(adapter);
        indicator.setViewPager(banners);



    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        int[] banners = new int[]{
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3,
                R.drawable.banner4
        };

        public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Home.page frag = new Home.page();
            Bundle b = new Bundle();
            b.putInt("banner", banners[position]);
            frag.setArguments(b);
            return frag;
        }

        @Override
        public int getCount() {
            return banners.length;
        }
    }

    public static class page extends Fragment {

        int banner;
        ImageView image;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.page, container, false);

            banner = getArguments().getInt("banner");

            image = view.findViewById(R.id.image);

            image.setImageDrawable(getActivity().getResources().getDrawable(banner));

            return view;
        }
    }

}