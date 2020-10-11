package com.mrtecks.amrdukan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;

public class Category2 extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager banners;
    CircleIndicator indicator;
    RecyclerView grid;
    CategoryAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category2);

        toolbar = findViewById(R.id.toolbar2);
        banners = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);
        grid = findViewById(R.id.grid);
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

        adapter2 = new CategoryAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(this, 3);

        grid.setAdapter(adapter2);
        grid.setLayoutManager(manager);

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

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        Context context;

        public CategoryAdapter(Context context) {
            this.context = context;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.category_list_model3, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            //holder.tag.setText(item.getTag());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context , Products2.class);
                    startActivity(intent);

                    /*FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                    for (int i = 0; i < fm4.getBackStackEntryCount(); ++i) {
                        fm4.popBackStack();
                    }

                    FragmentTransaction ft4 = fm4.beginTransaction();
                    SubCat frag14 = new SubCat();
                    Bundle b = new Bundle();
                    b.putString("id", item.getId());
                    b.putString("title", item.getName());
                    b.putString("image", item.getImage());
                    frag14.setArguments(b);
                    ft4.replace(R.id.replace, frag14);
                    ft4.addToBackStack(null);
                    ft4.commit();
*/
                }
            });


        }

        @Override
        public int getItemCount() {
            return 9;
        }

        class ViewHolder extends RecyclerView.ViewHolder {


            public ViewHolder(@NonNull View itemView) {
                super(itemView);



            }
        }
    }

}