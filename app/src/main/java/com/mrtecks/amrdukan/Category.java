package com.mrtecks.amrdukan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mrtecks.amrdukan.foodCatPOJO.Datum;
import com.mrtecks.amrdukan.foodCatPOJO.foodCatBean;
import com.mrtecks.amrdukan.homePOJO.Banners;
import com.mrtecks.amrdukan.homePOJO.homeBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Category extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager banners;
    CircleIndicator indicator;
    RecyclerView grid;
    CategoryAdapter adapter;
    GridLayoutManager manager;
    List<Datum> list;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        list = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar2);
        grid = findViewById(R.id.grid);
        progress = findViewById(R.id.progressBar2);
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

        adapter = new CategoryAdapter(this, list);
        manager = new GridLayoutManager(this, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.HEADERS);
        logging.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<foodCatBean> call = cr.getFoodCat();

        call.enqueue(new Callback<foodCatBean>() {
            @Override
            public void onResponse(Call<foodCatBean> call, Response<foodCatBean> response) {

                adapter.setData(response.body().getData());

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<foodCatBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        progress.setVisibility(View.VISIBLE);

        Call<foodCatBean> call2 = cr.getFoodBanner();
        call2.enqueue(new Callback<foodCatBean>() {
            @Override
            public void onResponse(Call<foodCatBean> call, Response<foodCatBean> response) {


                if (response.body().getStatus().equals("1")) {


                    final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, response.body().getData());
                    banners.setAdapter(adapter);
                    indicator.setViewPager(banners);

                }

                progress.setVisibility(View.GONE);

                Log.d("asdasd", response.body().getMessage());

            }

            @Override
            public void onFailure(Call<foodCatBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Log.d("asdasd", t.toString());
            }
        });

    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        List<Datum> blist = new ArrayList<>();

        public PagerAdapter(@NonNull FragmentManager fm, int behavior, List<Datum> blist) {
            super(fm, behavior);
            this.blist = blist;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            page frag = new page();
            frag.setData(blist.get(position).getImage());
            return frag;
        }

        @Override
        public int getCount() {
            return blist.size();
        }
    }

    public static class page extends Fragment {

        String url;
        ImageView image;

        void setData(String url) {
            this.url = url;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.page, container, false);

            image = view.findViewById(R.id.image);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(url, image, options);

            return view;
        }
    }


    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
        Context context;
        List<Datum> list = new ArrayList<>();


        public CategoryAdapter(Context context, List<Datum> list) {
            this.context = context;
            this.list = list;
        }

        void setData(List<Datum> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.category_list_model2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            final Datum item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);

            holder.title.setText(item.getName());
            holder.desc.setText(item.getDescription());

            holder.rating.setRating(Float.parseFloat(item.getRating()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, Products.class);
                    intent.putExtra("title", item.getName());
                    intent.putExtra("cid", item.getId());
                    context.startActivity(intent);

                }
            });


        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            RoundedImageView image;
            TextView title, desc;
            RatingBar rating;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.imageView21);
                title = itemView.findViewById(R.id.textView67);
                desc = itemView.findViewById(R.id.textView68);
                rating = itemView.findViewById(R.id.ratingBar);
            }
        }
    }

}