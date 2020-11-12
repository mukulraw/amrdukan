package com.mrtecks.amrdukan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mrtecks.amrdukan.cartPOJO.Datum;
import com.mrtecks.amrdukan.cartPOJO.cartBean;
import com.mrtecks.amrdukan.seingleProductPOJO.singleProductBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Cart2 extends AppCompatActivity {

    ProgressBar bar;
    String base;
    TextView btotal, bproceed, clear;

    float amm = 0;

    View bottom;

    CartAdapter adapter;

    GridLayoutManager manager;

    RecyclerView grid;

    List<Datum> list;

    String client, txn;

    Toolbar toolbar;
    TextView amount, gst, delivery, grand;
    float del = 25;
    float gs = 0;
    float mem = 0;
    float gt = 0;

    String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart2);

        list = new ArrayList<>();

        cid = getIntent().getStringExtra("cid");

        toolbar = findViewById(R.id.toolbar2);
        bar = findViewById(R.id.progressBar3);
        bottom = findViewById(R.id.cart_bottom);
        btotal = findViewById(R.id.textView9);
        bproceed = findViewById(R.id.textView10);
        grid = findViewById(R.id.grid);
        clear = findViewById(R.id.textView12);
        grand = findViewById(R.id.textView74);
        amount = findViewById(R.id.textView70);
        gst = findViewById(R.id.textView76);
        delivery = findViewById(R.id.textView71);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Cart");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        adapter = new CartAdapter(list, this);

        manager = new GridLayoutManager(this, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bar.setVisibility(View.VISIBLE);

                Bean b = (Bean) getApplicationContext();

                base = b.baseurl;

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseurl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                Call<singleProductBean> call = cr.clearFoodCart(SharePreferenceUtils.getInstance().getString("userId"), cid);

                call.enqueue(new Callback<singleProductBean>() {
                    @Override
                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                        if (response.body().getStatus().equals("1")) {
                            finish();
                        }

                        Toast.makeText(Cart2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        bar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<singleProductBean> call, Throwable t) {
                        bar.setVisibility(View.GONE);
                    }
                });


            }
        });

        bproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (amm > 0) {
                    Intent intent = new Intent(Cart2.this, Checkout2.class);
                    intent.putExtra("amount", String.valueOf(gt));
                    intent.putExtra("actual_amount", String.valueOf(amm));
                    intent.putExtra("gst", String.valueOf(gs));
                    intent.putExtra("membership_discount", String.valueOf(mem));
                    intent.putExtra("delivery", String.valueOf(del));
                    intent.putExtra("cid", cid);
                    startActivity(intent);
                } else {
                    Toast.makeText(Cart2.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        loadCart();

    }

    void loadCart() {
        bar.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        base = b.baseurl;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Log.d("userid", SharePreferenceUtils.getInstance().getString("userId"));


        Call<cartBean> call = cr.getFoodCart(
                SharePreferenceUtils.getInstance().getString("userId"),
                cid,
                SharePreferenceUtils.getInstance().getString("lat"),
                SharePreferenceUtils.getInstance().getString("lng")
        );
        call.enqueue(new Callback<cartBean>() {
            @Override
            public void onResponse(Call<cartBean> call, Response<cartBean> response) {

                if (response.body().getData().size() > 0) {


                    adapter.setgrid(response.body().getData());

                    amm = Float.parseFloat(response.body().getTotal());

                    del = Float.parseFloat(response.body().getDelcharges());

                    gs = 0;

                    delivery.setText("₹ " + del);
                    gst.setText("₹ " + gs);

                    gt = amm + gs - mem + del;
                    grand.setText("₹ " + gt);

                    btotal.setText("Total: \u20B9 " + gt);
                    amount.setText("\u20B9 " + response.body().getTotal());

                    bottom.setVisibility(View.VISIBLE);
                } else {
                    finish();
                    adapter.setgrid(response.body().getData());
                    bottom.setVisibility(View.GONE);
                    Toast.makeText(Cart2.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }

                bar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<cartBean> call, Throwable t) {
                bar.setVisibility(View.GONE);
            }
        });

    }

    class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

        List<Datum> list = new ArrayList<>();
        Context context;
        LayoutInflater inflater;

        CartAdapter(List<Datum> list, Context context) {
            this.context = context;
            this.list = list;
        }

        void setgrid(List<Datum> list) {

            this.list = list;
            notifyDataSetChanged();

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.prod_list_model5, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i1) {

            final Datum item = list.get(i1);

            //viewHolder.setIsRecyclable(false);


            viewHolder.title.setText(item.getName());
            viewHolder.brand.setText("Addons: " + item.getAddon());


            viewHolder.quantity.setText(item.getQuantity());

            if (item.getRequest().length() > 0) {
                viewHolder.special.setText("(Special Request: " + item.getRequest() + " )");
            }


            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int q = Integer.parseInt(item.getQuantity());

                    if (q < 99) {

                        q++;

                        viewHolder.quantity.setText(String.valueOf(q));

                        bar.setVisibility(View.VISIBLE);

                        Bean b = (Bean) context.getApplicationContext();

                        base = b.baseurl;

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                        Call<singleProductBean> call = cr.updateFoodCart(item.getPid(), String.valueOf(q), item.getPrice());

                        call.enqueue(new Callback<singleProductBean>() {
                            @Override
                            public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                if (response.body().getStatus().equals("1")) {
                                    loadCart();
                                }

                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                bar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<singleProductBean> call, Throwable t) {
                                bar.setVisibility(View.GONE);
                            }
                        });

                    }


                }
            });


            viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int q = Integer.parseInt(item.getQuantity());

                    if (q > 1) {

                        q--;

                        viewHolder.quantity.setText(String.valueOf(q));

                        bar.setVisibility(View.VISIBLE);

                        Bean b = (Bean) context.getApplicationContext();

                        base = b.baseurl;

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                        Call<singleProductBean> call = cr.updateFoodCart(item.getPid(), String.valueOf(q), item.getPrice());

                        call.enqueue(new Callback<singleProductBean>() {
                            @Override
                            public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                if (response.body().getStatus().equals("1")) {
                                    loadCart();
                                }

                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                bar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<singleProductBean> call, Throwable t) {
                                bar.setVisibility(View.GONE);
                            }
                        });

                    }


                }
            });


            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    bar.setVisibility(View.VISIBLE);

                    Bean b = (Bean) context.getApplicationContext();

                    base = b.baseurl;

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<singleProductBean> call = cr.deleteFoodCart(item.getPid());

                    call.enqueue(new Callback<singleProductBean>() {
                        @Override
                        public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                            if (response.body().getStatus().equals("1")) {
                                loadCart();
                            }

                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            bar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<singleProductBean> call, Throwable t) {
                            bar.setVisibility(View.GONE);
                            Log.d("error", t.toString());
                        }
                    });

                }
            });


            viewHolder.price.setText("\u20B9 " + item.getPrice());


            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), viewHolder.imageView, options);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            ImageButton delete;

            Button add, remove;
            TextView quantity, title, brand, price, special;


            ViewHolder(@NonNull View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.image);
                //buy = itemView.findViewById(R.id.play);


                delete = itemView.findViewById(R.id.delete);

                add = itemView.findViewById(R.id.increment);
                remove = itemView.findViewById(R.id.decrement);
                quantity = itemView.findViewById(R.id.display);
                title = itemView.findViewById(R.id.textView20);
                brand = itemView.findViewById(R.id.textView21);
                price = itemView.findViewById(R.id.textView22);
                special = itemView.findViewById(R.id.textView69);

                //buy.setSideTapEnabled(true);

                //name = itemView.findViewById(R.id.name);


            }
        }
    }

}