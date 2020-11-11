package com.mrtecks.amrdukan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.mrtecks.amrdukan.cartPOJO.cartBean;
import com.mrtecks.amrdukan.foodProductsPOJO.Data;
import com.mrtecks.amrdukan.foodProductsPOJO.foodProductBean;
import com.mrtecks.amrdukan.productsPOJO.Datum;
import com.mrtecks.amrdukan.seingleProductPOJO.singleProductBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;
import nl.dionsegijn.steppertouch.StepperTouch;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SingleProduct extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabs;
    CircleIndicator indicator;
    LayoutInflater inflater;
    String cid, pid;

    ImageView image;
    TextView title, selling, mrp, size, type, description, addontitle;
    Button add;
    ProgressBar progress;

    LinearLayout addon;

    List<String> addids;

    EditText request;

    String nv1;

    TextView btotal, bcount, bview;
    View cart_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        addids = new ArrayList<>();

        cid = getIntent().getStringExtra("cid");
        pid = getIntent().getStringExtra("pid");

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        toolbar = findViewById(R.id.toolbar2);
        tabs = findViewById(R.id.tabLayout);
        indicator = findViewById(R.id.indicator);
        image = findViewById(R.id.viewPager2);
        title = findViewById(R.id.textView28);
        selling = findViewById(R.id.textView32);
        mrp = findViewById(R.id.textView33);
        size = findViewById(R.id.size);
        type = findViewById(R.id.type);
        description = findViewById(R.id.description);
        progress = findViewById(R.id.progressBar7);
        add = findViewById(R.id.button2);
        addon = findViewById(R.id.checkBox);
        addontitle = findViewById(R.id.textView13);
        request = findViewById(R.id.editTextTextPersonName);
        cart_bottom = findViewById(R.id.cart_bottom);
        btotal = findViewById(R.id.textView9);
        bcount = findViewById(R.id.textView3);
        bview = findViewById(R.id.textView10);


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
        toolbar.setTitle(getIntent().getStringExtra("title"));

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

        Call<foodProductBean> call = cr.getFoodProductById(pid);
        call.enqueue(new Callback<foodProductBean>() {
            @Override
            public void onResponse(Call<foodProductBean> call, Response<foodProductBean> response) {

                if (response.body().getStatus().equals("1")) {
                    final Data item = response.body().getData();

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(item.getImage(), image, options);

                    title.setText(item.getName());
                    size.setText(item.getSize());
                    type.setText(item.getType());

                    if (item.getType().equals("VEG")) {
                        type.setTextColor(Color.parseColor("#45A822"));
                    } else {
                        type.setTextColor(Color.RED);
                    }

                    if (item.getStock().equals("In stock")) {
                        add.setVisibility(View.VISIBLE);
                    } else {
                        add.setVisibility(View.GONE);
                    }

                    description.setText(Html.fromHtml(item.getDescription()));

                    float mrp1 = Float.parseFloat(item.getPrice());
                    float sel = Float.parseFloat(item.getDiscount());
                    float dis = mrp1 - sel;

                    if (dis > 0) {
                        nv1 = String.valueOf(item.getDiscount());
                        selling.setText(Html.fromHtml("\u20B9 " + item.getDiscount()));
                        mrp.setText(Html.fromHtml("<strike>\u20B9 " + item.getPrice() + "</strike>"));
                        mrp.setVisibility(View.VISIBLE);
                    } else {
                        nv1 = String.valueOf(item.getDiscount());
                        selling.setText("\u20B9 " + item.getDiscount());
                        mrp.setVisibility(View.GONE);
                    }

                    if (item.getAddons().size() > 0) {
                        addon.removeAllViews();

                        for (int i = 0; i < item.getAddons().size(); i++) {
                            View addonmodel = inflater.inflate(R.layout.addon_model, null);
                            CheckBox check = addonmodel.findViewById(R.id.check);
                            check.setText(item.getAddons().get(i).getTitle() + " (+ ₹" + item.getAddons().get(i).getPrice() + ")");

                            final int finalI = i;
                            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                    if (isChecked) {
                                        addids.add(item.getAddons().get(finalI).getId());
                                    } else {
                                        addids.remove(item.getAddons().get(finalI).getId());
                                    }

                                }
                            });

                            addon.addView(addonmodel);
                        }

                        addon.setVisibility(View.VISIBLE);
                        addontitle.setVisibility(View.VISIBLE);
                    } else {
                        addon.setVisibility(View.GONE);
                        addontitle.setVisibility(View.GONE);
                    }

                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<foodProductBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String aons = TextUtils.join(",", addids);
                final String req = request.getText().toString();


                final Dialog dialog = new Dialog(SingleProduct.this, R.style.MyDialogTheme);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.add_cart_dialog);
                dialog.show();

                final StepperTouch stepperTouch = dialog.findViewById(R.id.stepperTouch);
                Button add = dialog.findViewById(R.id.button8);
                final ProgressBar progressBar = dialog.findViewById(R.id.progressBar2);

                stepperTouch.setMinValue(1);
                stepperTouch.setMaxValue(99);
                stepperTouch.setSideTapEnabled(true);
                stepperTouch.setCount(1);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        progressBar.setVisibility(View.VISIBLE);

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

                        Log.d("userid", SharePreferenceUtils.getInstance().getString("userid"));
                        Log.d("pid", pid);
                        Log.d("quantity", String.valueOf(stepperTouch.getCount()));


                        Call<singleProductBean> call = cr.addFoodCart(
                                SharePreferenceUtils.getInstance().getString("userId"),
                                pid,
                                String.valueOf(stepperTouch.getCount()),
                                nv1,
                                cid,
                                aons,
                                req
                        );

                        call.enqueue(new Callback<singleProductBean>() {
                            @Override
                            public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                if (response.body().getStatus().equals("1")) {
                                    dialog.dismiss();
                                    onResume();
                                }

                                Toast.makeText(SingleProduct.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<singleProductBean> call, Throwable t) {
                                progressBar.setVisibility(View.GONE);
                            }
                        });


                    }
                });

            }
        });

        bview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SingleProduct.this, Cart2.class);
                intent.putExtra("cid", cid);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

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

        Call<cartBean> call2 = cr.getFoodCart(SharePreferenceUtils.getInstance().getString("userId"), cid);
        call2.enqueue(new Callback<cartBean>() {
            @Override
            public void onResponse(Call<cartBean> call, Response<cartBean> response) {

                if (response.body().getData().size() > 0) {

                    btotal.setText("Total: \u20B9 " + response.body().getTotal());
                    bcount.setText(response.body().getItems());

                    cart_bottom.setVisibility(View.VISIBLE);
                } else {
                    cart_bottom.setVisibility(View.GONE);
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<cartBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }


}