package com.mrtecks.amrdukan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class NewOrder extends Fragment {


    TabLayout tabs;
    ViewPager pager;
    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_layout, container, false);
        mainActivity = (MainActivity)getActivity();
        tabs = view.findViewById(R.id.tabLayout4);
        pager = view.findViewById(R.id.pager);


        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);


        return view;
    }

    class PagerAdapter extends FragmentStatePagerAdapter{

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
            {
                return "Foodkart Orders";
            }
            else
            {
                return "Shop Orders";
            }
        }

        public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0)
            {
                return new Orders2();
            }
            else
            {
                return new Orders();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
