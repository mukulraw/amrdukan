package com.mrtecks.amrdukan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mrtecks.amrdukan.addressPOJO.Datum;
import com.mrtecks.amrdukan.addressPOJO.addressBean;
import com.mrtecks.amrdukan.checkPromoPOJO.checkPromoBean;
import com.mrtecks.amrdukan.checkoutPOJO.checkoutBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Checkout2 extends AppCompatActivity {

    private static final String TAG = "Checkout";
    Toolbar toolbar;
    EditText name, address, area, city, pin, promo;
    Button proceed, apply;
    ProgressBar progress;
    String amm, gtotal;
    Spinner addr;
    String tslot = "";
    String paymode;
    RadioGroup group;
    String oid;
    TextView amount, grand;
    String dd = "";
    List<String> ts;

    String pid;

    List<String> list;
    List<Datum> adlist;

    String isnew = "1";

    int del = 25;

    String actual_amount;
    String gst;
    String membership_discount;
    String delivery1;
    String cid;

    TextView promotext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout2);

        list = new ArrayList<>();
        ts = new ArrayList<>();
        adlist = new ArrayList<>();

        amm = getIntent().getStringExtra("amount");
        actual_amount = getIntent().getStringExtra("actual_amount");
        gst = getIntent().getStringExtra("gst");
        membership_discount = getIntent().getStringExtra("membership_discount");
        delivery1 = getIntent().getStringExtra("delivery");
        cid = getIntent().getStringExtra("cid");

        toolbar = findViewById(R.id.toolbar4);
        promotext = findViewById(R.id.textView79);
        name = findViewById(R.id.editText2);
        address = findViewById(R.id.editText3);
        proceed = findViewById(R.id.button6);
        progress = findViewById(R.id.progressBar4);
        addr = findViewById(R.id.spinner2);
        group = findViewById(R.id.radioButton);
        area = findViewById(R.id.editText5);
        city = findViewById(R.id.editText6);
        pin = findViewById(R.id.editText7);
        amount = findViewById(R.id.textView49);
        grand = findViewById(R.id.textView51);
        promo = findViewById(R.id.editText8);
        apply = findViewById(R.id.button9);


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
        toolbar.setTitle("Checkout");


        amount.setText("₹ " + amm);

        float am = Float.parseFloat(amm);

        float gt = 0;

        if (am >= 1000) {
            gt = Float.parseFloat(amm) + 0;
            //delivery.setText("₹ " + 0);
        } else {
            gt = Float.parseFloat(amm) + 0;
            //delivery.setText("₹ " + del);
        }


        grand.setText("₹ " + gt);

        gtotal = String.valueOf(gt);


        address.setText(SharePreferenceUtils.getInstance().getString("deliveryLocation"));

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<addressBean> call = cr.getAddress(SharePreferenceUtils.getInstance().getString("userId"));
        call.enqueue(new Callback<addressBean>() {
            @Override
            public void onResponse(Call<addressBean> call, Response<addressBean> response) {

                list.clear();
                adlist.clear();

                list.add("New Address");

                if (response.body().getData().size() > 0) {

                    adlist.addAll(response.body().getData());

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        list.add(response.body().getData().get(i).getName());
                    }


                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Checkout2.this,
                        android.R.layout.simple_list_item_1, list);

                addr.setAdapter(adapter);


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<addressBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        addr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {


                    isnew = "0";
                    Datum item = adlist.get(position - 1);
                    name.setText(item.getName());
                    address.setText(item.getHouse());
                    area.setText(item.getArea());
                    city.setText(item.getCity());
                    pin.setText(item.getPin());


                } else {
                    isnew = "1";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pc = promo.getText().toString();

                if (pc.length() > 0) {

                    apply.setEnabled(false);
                    apply.setClickable(false);

                    promo.setEnabled(false);
                    promo.setClickable(false);

                    progress.setVisibility(View.VISIBLE);

                    Bean b = (Bean) getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<checkPromoBean> call = cr.checkPromo(pc, SharePreferenceUtils.getInstance().getString("userId"));

                    call.enqueue(new Callback<checkPromoBean>() {
                        @Override
                        public void onResponse(Call<checkPromoBean> call, Response<checkPromoBean> response) {

                            if (response.body().getStatus().equals("1")) {

                                float amt = Float.parseFloat(amm);
                                float dis = Float.parseFloat(response.body().getData().getDiscount());

                                float da = (dis / 100) * amt;

                                float na = amt - da;

                                amm = String.valueOf(na);

                                amount.setText("₹ " + amm);

                                float gt = Float.parseFloat(amm) + 0;

                                grand.setText("₹ " + gt);

                                gtotal = String.valueOf(gt);

                                pid = response.body().getData().getPid();

                                promotext.setText("PROMO Code applied for ₹ " + da);
                                promotext.setVisibility(View.VISIBLE);

                                Toast.makeText(Checkout2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Checkout2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                apply.setEnabled(true);
                                apply.setClickable(true);
                                promotext.setVisibility(View.GONE);
                                promo.setEnabled(true);
                                promo.setClickable(true);
                            }

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<checkPromoBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                            apply.setEnabled(true);
                            apply.setClickable(true);
                            promotext.setVisibility(View.GONE);
                            promo.setEnabled(true);
                            promo.setClickable(true);
                        }
                    });

                } else {
                    Toast.makeText(Checkout2.this, "Invalid PROMO code", Toast.LENGTH_SHORT).show();
                }

            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String n = name.getText().toString();
                String a = address.getText().toString();
                String ar = area.getText().toString();
                String c = city.getText().toString();
                String p = pin.getText().toString();

                if (n.length() > 0) {

                    if (a.length() > 0) {


                        if (ar.length() > 0) {

                            if (c.length() > 0) {

                                if (p.length() > 0) {

                                    int iidd = group.getCheckedRadioButtonId();

                                    if (iidd > -1) {

                                        RadioButton cb = group.findViewById(iidd);

                                        paymode = cb.getText().toString();


                                        oid = String.valueOf(System.currentTimeMillis());

                                        if (paymode.equals("Cash on Delivery")) {
                                            progress.setVisibility(View.VISIBLE);

                                            Bean b = (Bean) getApplicationContext();

                                            String adr = a + ", " + ar + ", " + c + ", " + p;

                                            Log.d("addd", adr);

                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(b.baseurl)
                                                    .addConverterFactory(ScalarsConverterFactory.create())
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();

                                            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                            Call<checkoutBean> call = cr.buyVouchers2(
                                                    SharePreferenceUtils.getInstance().getString("userId"),
                                                    //SharePreferenceUtils.getInstance().getString("lat"),
                                                    //SharePreferenceUtils.getInstance().getString("lng"),
                                                    gtotal,
                                                    oid,
                                                    n,
                                                    adr,
                                                    paymode,
                                                    tslot,
                                                    dd,
                                                    pid,
                                                    a,
                                                    ar,
                                                    c,
                                                    p,
                                                    isnew,
                                                    cid
                                            );
                                            call.enqueue(new Callback<checkoutBean>() {
                                                @Override
                                                public void onResponse(Call<checkoutBean> call, Response<checkoutBean> response) {

                                                    Toast.makeText(Checkout2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                    progress.setVisibility(View.GONE);

                                                    Dialog dialog = new Dialog(Checkout2.this, R.style.DialogCustomTheme);
                                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                    dialog.setCancelable(true);
                                                    dialog.setContentView(R.layout.success_popup);
                                                    dialog.show();

                                                    TextView oi = dialog.findViewById(R.id.textView57);
                                                    TextView au = dialog.findViewById(R.id.textView58);

                                                    oi.setText(oid);
                                                    au.setText("₹ " + gtotal);

                                                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                        @Override
                                                        public void onCancel(DialogInterface dialog) {

                                                            dialog.dismiss();
                                                            finish();

                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onFailure(Call<checkoutBean> call, Throwable t) {
                                                    progress.setVisibility(View.GONE);
                                                }
                                            });
                                        } else {


                                                    /*progress.setVisibility(View.VISIBLE);

                                                    Bean b = (Bean) getApplicationContext();

                                                    String adr = a + ", " + ar + ", " + c + ", " + p;

                                                    Log.d("addd", adr);

                                                    Retrofit retrofit = new Retrofit.Builder()
                                                            .baseUrl(b.baseurl)
                                                            .addConverterFactory(ScalarsConverterFactory.create())
                                                            .addConverterFactory(GsonConverterFactory.create())
                                                            .build();

                                                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                                    Call<payBean> call1 = cr.getOrderId(String.valueOf(Float.parseFloat(gtotal) * 100), oid);

                                                    call1.enqueue(new Callback<payBean>() {
                                                        @Override
                                                        public void onResponse(Call<payBean> call, Response<payBean> response) {

                                                            com.razorpay.Checkout checkout = new com.razorpay.Checkout();
                                                            checkout.setKeyID("rzp_live_R9U1AGFiyxMvSS");
                                                            checkout.setImage(R.drawable.back);

                                                            try {
                                                                JSONObject options = new JSONObject();

                                                                options.put("name", "S A Enterprises");
                                                                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                                                                options.put("order_id", response.body().getId());//from response of step 3.
                                                                options.put("theme.color", "#3399cc");
                                                                options.put("currency", "INR");
                                                                options.put("amount", String.valueOf(response.body().getAmount() * 100));//pass amount in currency subunits
                                                                //options.put("prefill.email", "gaurav.kumar@example.com");
                                                                options.put("prefill.contact", SharePreferenceUtils.getInstance().getString("phone"));
                                                                checkout.open(Checkout.this, options);
                                                            } catch (Exception e) {
                                                                Log.e(TAG, "Error in starting Razorpay Checkout", e);
                                                            }

                                                            progress.setVisibility(View.GONE);

                                                        }

                                                        @Override
                                                        public void onFailure(Call<payBean> call, Throwable t) {
                                                            progress.setVisibility(View.GONE);
                                                        }
                                                    });
*/




                                                    /*Intent intent = new Intent(Checkout.this, WebViewActivity.class);
                                                    intent.putExtra(AvenuesParams.ACCESS_CODE, "AVOL70EE77BF91LOFB");
                                                    intent.putExtra(AvenuesParams.MERCHANT_ID, "133862");
                                                    intent.putExtra(AvenuesParams.ORDER_ID, oid);
                                                    intent.putExtra(AvenuesParams.CURRENCY, "INR");
                                                    intent.putExtra(AvenuesParams.AMOUNT, String.valueOf(gtotal));
                                                    //intent.putExtra(AvenuesParams.AMOUNT, "1");
                                                    intent.putExtra("pid", SharePreferenceUtils.getInstance().getString("userid"));

                                                    intent.putExtra(AvenuesParams.REDIRECT_URL, "https://mrtecks.com/grocery/api/pay/ccavResponseHandler.php");
                                                    intent.putExtra(AvenuesParams.CANCEL_URL, "https://mrtecks.com/grocery/api/pay/ccavResponseHandler.php");
                                                    intent.putExtra(AvenuesParams.RSA_KEY_URL, "https://mrtecks.com/grocery/api/pay/GetRSA.php");

                                                    startActivityForResult(intent, 12);*/


                                        }


                                    } else {
                                        Toast.makeText(Checkout2.this, "Please select a Payment Mode", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(Checkout2.this, "Please select a valid PIN Code", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(Checkout2.this, "Please select a valid City", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(Checkout2.this, "Please select a valid Locality/ Area/ District", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(Checkout2.this, "Please enter a valid House/ Apartment No.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Checkout2.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}