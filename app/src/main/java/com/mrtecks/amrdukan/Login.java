package com.mrtecks.amrdukan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

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

    private static final int RC_SIGN_IN = 123;
    EditText phone;
    Button login;
    ProgressBar progress;
    ImageButton back;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        phone = findViewById(R.id.editText);
        login = findViewById(R.id.button);
        progress = findViewById(R.id.progressBar);
        back = findViewById(R.id.imageButton);
        SignInButton signInButton = findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String p = phone.getText().toString();

                if (p.length() == 10) {


                    /*progress.setVisibility(View.VISIBLE);

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

                    Call<loginBean> call = cr.login(p, SharePreferenceUtils.getInstance().getString("token"));

                    call.enqueue(new Callback<loginBean>() {
                        @Override
                        public void onResponse(@NotNull Call<loginBean> call, @NotNull Response<loginBean> response) {

                            assert response.body() != null;
                            if (response.body().getStatus().equals("1")) {
                                //SharePreferenceUtils.getInstance().saveString("userId" , response.body().getUserId());
                                SharePreferenceUtils.getInstance().saveString("phone", response.body().getPhone());
                                Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Login.this, OTP.class);
                                startActivity(intent);
                                finishAffinity();

                            }

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(@NotNull Call<loginBean> call, @NotNull Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });*/


                } else {
                    Toast.makeText(Login.this, "Please enter a valid Phone Number", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("email", account.getEmail());
            Log.d("email", account.getDisplayName());
            Log.d("email", account.getId());

            String email = account.getEmail();
            String password = account.getId();
            String name = account.getDisplayName();

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

            Call<loginBean> call = cr.login(email, password, name, SharePreferenceUtils.getInstance().getString("token"));

            call.enqueue(new Callback<loginBean>() {
                @Override
                public void onResponse(@NotNull Call<loginBean> call, @NotNull Response<loginBean> response) {

                    assert response.body() != null;
                    if (response.body().getStatus().equals("1")) {
                        SharePreferenceUtils.getInstance().saveString("userId" , response.body().getUserId());
                        SharePreferenceUtils.getInstance().saveString("email", response.body().getEmail());
                        SharePreferenceUtils.getInstance().saveString("name", response.body().getName());
                        Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();

                    }

                    progress.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<loginBean> call, @NotNull Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

}