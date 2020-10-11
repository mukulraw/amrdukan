package com.mrtecks.amrdukan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class Home extends Fragment {

    RecyclerView categories;
    RecyclerView best;
    GridLayoutManager manager;
    LinearLayoutManager manager2;
    ViewPager banners;
    CircleIndicator indicator;

    ImageView left, right;
    boolean programaticallyScrolled;
    int currentVisibleItem = 0;

    final int duration = 10;
    final int pixelsToMove = 30;
    private final Handler mHandler = new Handler(Looper.getMainLooper());


    static MainActivity mainActivity;

    EditText search;

    CategoryAdapter categoryAdapter;
    BestAdapter bestAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        mainActivity = (MainActivity) getActivity();

        banners = view.findViewById(R.id.viewPager);
        indicator = view.findViewById(R.id.indicator);
        categories = view.findViewById(R.id.recyclerView);
        best = view.findViewById(R.id.recyclerView2);


        final PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        banners.setAdapter(adapter);
        indicator.setViewPager(banners);
        categoryAdapter = new CategoryAdapter(mainActivity);
        bestAdapter = new BestAdapter(mainActivity);
        manager = new GridLayoutManager(mainActivity, 3);
        manager2 = new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return categoryAdapter.getSpans(position);
            }
        });

        categories.setAdapter(categoryAdapter);
        categories.setLayoutManager(manager);

        best.setAdapter(bestAdapter);
        best.setLayoutManager(manager2);

        /*search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                FragmentTransaction ft4 = fm4.beginTransaction();
                Search frag14 = new Search();
                ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    *//*Bundle b = new Bundle();
                    b.putString("id", item.getId());
                    b.putString("title", item.getName());
                    frag14.setArguments(b);*//*
                ft4.replace(R.id.replace, frag14);
                //ft4.addToBackStack(null);
                ft4.commit();

            }
        });*/


        return view;
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
            page frag = new page();
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


    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
        Context context;

        String[] title = new String[]{
                "Grocery",
                "Foodkart",
                "Electronics",
                "Medicines",
                "Exclusive Fashion Store"
        };

        int[] images = new int[]
                {
                        R.drawable.grocery,
                        R.drawable.foodkart,
                        R.drawable.electronics,
                        R.drawable.medicine,
                        R.drawable.fashion
                };

        int[] spans = new int[]
                {
                        1,
                        1,
                        1,
                        1,
                        2
                };


        public CategoryAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.category_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            holder.image.setImageDrawable(context.getResources().getDrawable(images[position]));
            holder.title.setText(title[position]);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (title[position].equals("Foodkart")) {
                        Intent intent = new Intent(context, Category.class);
                        intent.putExtra("title", title[position]);
                        context.startActivity(intent);
                    }
                    else if (title[position].equals("Medicines"))
                    {
                        Intent intent = new Intent(context, Category3.class);
                        intent.putExtra("title", title[position]);
                        context.startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(context, Category2.class);
                        intent.putExtra("title", title[position]);
                        context.startActivity(intent);
                    }



                    /*FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                    FragmentTransaction ft4 = fm4.beginTransaction();
                    SubCat frag14 = new SubCat();
                    ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    *//*Bundle b = new Bundle();
                    b.putString("id", item.getId());
                    b.putString("title", item.getName());
                    frag14.setArguments(b);*//*
                    ft4.replace(R.id.replace, frag14);
                    ft4.addToBackStack(null);
                    ft4.commit();*/

                }
            });


        }

        public int getSpans(int pos) {
            return spans[pos];
        }

        @Override
        public int getItemCount() {
            return title.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                title = itemView.findViewById(R.id.textView7);
            }
        }
    }

    class BestAdapter extends RecyclerView.Adapter<BestAdapter.ViewHolder> {
        Context context;

        public BestAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.best_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                    FragmentTransaction ft4 = fm4.beginTransaction();
                    SubCat frag14 = new SubCat();
                    ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    *//*Bundle b = new Bundle();
                    b.putString("id", item.getId());
                    b.putString("title", item.getName());
                    frag14.setArguments(b);*//*
                    ft4.replace(R.id.replace, frag14);
                    ft4.addToBackStack(null);
                    ft4.commit();*/

                }
            });


        }


        @Override
        public int getItemCount() {
            return 8;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

}
