package com.mrtecks.amrdukan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.mrtecks.amrdukan.loginPOJO.Data;
import com.mrtecks.amrdukan.loginPOJO.loginBean;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends AppCompatActivity {

    TextView signup;
    Button login;
    TextInputEditText email, password;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.textView88);
        login = findViewById(R.id.button);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progress = findViewById(R.id.progressBar);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String em = email.getText().toString();
                String pa = password.getText().toString();

                if (em.length() > 0) {

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

                        Call<loginBean> call = cr.login(em, pa, SharePreferenceUtils.getInstance().getString("token"));

                        call.enqueue(new Callback<loginBean>() {
                            @Override
                            public void onResponse(@NotNull Call<loginBean> call, @NotNull Response<loginBean> response) {

                                if (response.body().getStatus().equals("1")) {

                                    Data item = response.body().getData();

                                    SharePreferenceUtils.getInstance().saveString("userId" , item.getId());
                                    SharePreferenceUtils.getInstance().saveString("phone", item.getPhone());
                                    SharePreferenceUtils.getInstance().saveString("email", item.getEmail());
                                    SharePreferenceUtils.getInstance().saveString("name", item.getName());
                                    Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();

                                } else {
                                    Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(@NotNull Call<loginBean> call, @NotNull Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Toast.makeText(Login.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Login.this, "Invalid phone", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}