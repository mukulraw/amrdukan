package com.mrtecks.amrdukan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mrtecks.amrdukan.seingleProductPOJO.Data;
import com.mrtecks.amrdukan.seingleProductPOJO.singleProductBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.steppertouch.StepperTouch;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SingleProduct2 extends AppCompatActivity {

    Toolbar toolbar;
    ImageView image;
    TextView discount, title, price;
    Button add;
    TextView brand, unit, seller;
    TextView description, key_features, packaging, life, disclaimer, stock;
    ProgressBar progress;

    String id, name;

    String pid, nv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product2);
        toolbar = findViewById(R.id.toolbar2);

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("title");

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(name);



        image = findViewById(R.id.image);
        discount = findViewById(R.id.discount);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        add = findViewById(R.id.add);
        brand = findViewById(R.id.brand);
        unit = findViewById(R.id.unit);
        seller = findViewById(R.id.seller);
        description = findViewById(R.id.description);
        key_features = findViewById(R.id.key_features);
        packaging = findViewById(R.id.packaging);
        life = findViewById(R.id.life);
        disclaimer = findViewById(R.id.disclaimer);
        progress = findViewById(R.id.progress);
        stock = findViewById(R.id.stock);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (pid.length() > 0) {
                    String uid = SharePreferenceUtils.getInstance().getString("userId");

                    if (uid.length() > 0) {

                        final Dialog dialog = new Dialog(SingleProduct2.this, R.style.MyDialogTheme);
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
                                Log.d("price", nv1);

                                int versionCode = BuildConfig.VERSION_CODE;
                                String versionName = BuildConfig.VERSION_NAME;

                                Call<singleProductBean> call = cr.addCart(SharePreferenceUtils.getInstance().getString("userId"), pid, String.valueOf(stepperTouch.getCount()), nv1, versionName);

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1")) {
                                            dialog.dismiss();
                                        }

                                        Toast.makeText(SingleProduct2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        progressBar.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onFailure(Call<singleProductBean> call, Throwable t) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });


                            }
                        });

                    } else {
                        Toast.makeText(SingleProduct2.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SingleProduct2.this, Login.class);
                        startActivity(intent);

                    }
                }


            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

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

        Call<singleProductBean> call = cr.getProductById(id);
        call.enqueue(new Callback<singleProductBean>() {
            @Override
            public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {


                if (response.body().getStatus().equals("1")) {
                    Data item = response.body().getData();

                    pid = item.getId();

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(item.getImage(), image, options);

                    float mrp = Float.parseFloat(item.getPrice());
                    float sel = Float.parseFloat(item.getDiscount());
                    float dis = mrp - sel;


                    if (dis > 0) {

                        float dv = (dis / mrp) * 100;

                        nv1 = String.valueOf(item.getDiscount());

                        discount.setVisibility(View.VISIBLE);
                        discount.setText(Math.round(dv) + "% OFF");
                        price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + nv1 + " </b></font><strike>\u20B9 " + item.getPrice() + "</strike>"));
                    } else {

                        nv1 = item.getPrice();
                        discount.setVisibility(View.GONE);
                        price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(item.getPrice()) + " </b></font>"));
                    }


                    title.setText(item.getName());

                    brand.setText(item.getBrand());
                    unit.setText(item.getSize());
                    seller.setText(item.getSeller());
                    description.setText(item.getDescription());
                    key_features.setText(item.getKeyFeatures());
                    packaging.setText(item.getPackagingType());
                    life.setText(item.getShelfLife());
                    disclaimer.setText(item.getDisclaimer());

                    if (item.getStock().equals("In stock")) {
                        add.setVisibility(View.VISIBLE);
                    } else {
                        add.setVisibility(View.GONE);
                    }


                    stock.setText(item.getStock());


                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<singleProductBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

}