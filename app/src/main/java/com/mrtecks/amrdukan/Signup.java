package com.mrtecks.amrdukan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mrtecks.amrdukan.loginPOJO.Data;
import com.mrtecks.amrdukan.loginPOJO.loginBean;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Signup extends AppCompatActivity {

    ImageButton back;
    TextInputEditText name, email, phone, password;
    Button signup;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        back = findViewById(R.id.imageButton);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        signup = findViewById(R.id.button);
        password = findViewById(R.id.password);
        progress = findViewById(R.id.progressBar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String f = name.getText().toString();
                String e = email.getText().toString();
                String ph = phone.getText().toString();
                String pa = password.getText().toString();

                if (f.length() > 0) {
                    if (e.length() > 0) {
                        if (ph.length() == 10) {
                            if (pa.length() > 0) {
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

                                Call<loginBean> call = cr.register(
                                        e,
                                        pa,
                                        f,
                                        SharePreferenceUtils.getInstance().getString("token"),
                                        ph
                                );

                                call.enqueue(new Callback<loginBean>() {
                                    @Override
                                    public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                        if (response.body().getStatus().equals("1")) {

                                            Data item = response.body().getData();

                                            SharePreferenceUtils.getInstance().saveString("userId" , item.getId());
                                            SharePreferenceUtils.getInstance().saveString("phone", item.getPhone());
                                            SharePreferenceUtils.getInstance().saveString("email", item.getEmail());
                                            SharePreferenceUtils.getInstance().saveString("name", item.getName());
                                            Toast.makeText(Signup.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(Signup.this, MainActivity.class);
                                            startActivity(intent);
                                            finishAffinity();

                                        } else {
                                            Toast.makeText(Signup.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                        progress.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(Call<loginBean> call, Throwable t) {
                                        progress.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                Toast.makeText(Signup.this, "Invalid password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Signup.this, "Invalid phone", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Signup.this, "Invalid email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Signup.this, "Invalid name", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}