package com.mrtecks.amrdukan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.hsalf.smileyrating.SmileyRating;
import com.mrtecks.amrdukan.addressPOJO.Datum;
import com.mrtecks.amrdukan.addressPOJO.addressBean;
import com.mrtecks.amrdukan.checkPromoPOJO.checkPromoBean;
import com.mrtecks.amrdukan.checkoutPOJO.checkoutBean;
import com.shivtechs.maplocationpicker.MapUtility;

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
    EditText name, address, area, city, pin, promo, phone;
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
    String packing;
    String membership_discount;
    String delivery1;
    String cid;
    String auto_cancel;

    TextView promotext, getlocation;

    private FusedLocationProviderClient fusedLocationClient;

    String lat = "", lng = "";

    LocationSettingsRequest.Builder builder;
    LocationRequest locationRequest;
    CountDownTimer timer;
    Dialog dialog;
    LocalBroadcastManager bm;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private int AUTOCOMPLETE_REQUEST_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout2);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        list = new ArrayList<>();
        ts = new ArrayList<>();
        adlist = new ArrayList<>();

        amm = getIntent().getStringExtra("amount");
        actual_amount = getIntent().getStringExtra("actual_amount");
        gst = getIntent().getStringExtra("gst");
        packing = getIntent().getStringExtra("packing");
        membership_discount = getIntent().getStringExtra("membership_discount");
        delivery1 = getIntent().getStringExtra("delivery");
        cid = getIntent().getStringExtra("cid");
        auto_cancel = getIntent().getStringExtra("auto_cancel");

        toolbar = findViewById(R.id.toolbar4);
        phone = findViewById(R.id.editTextPhone);
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
        getlocation = findViewById(R.id.textView84);

        bm = LocalBroadcastManager.getInstance(this);
        IntentFilter actionReceiver = new IntentFilter();
        actionReceiver.addAction("count");
        bm.registerReceiver(onJsonReceived, actionReceiver);

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


        phone.setText(SharePreferenceUtils.getInstance().getString("phone"));
        address.setText(SharePreferenceUtils.getInstance().getString("address"));
        area.setText(SharePreferenceUtils.getInstance().getString("getlocality"));
        city.setText(SharePreferenceUtils.getInstance().getString("getcity"));
        pin.setText(SharePreferenceUtils.getInstance().getString("getpincode"));

        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Checkout2.this, LocationPickerActivity2.class);
                //intent.putExtra(MapUtility.LATITUDE, sourceLAT);
                //intent.putExtra(MapUtility.LONGITUDE, sourceLNG);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                //address.setText(SharePreferenceUtils.getInstance().getString("deliveryLocation"));

            }
        });

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
                String ph = phone.getText().toString();

                if (ph.length() == 10) {
                    if (n.length() > 0) {

                        if (a.length() > 0) {

                            if (p.length() == 6) {
                                int iidd = group.getCheckedRadioButtonId();

                                if (iidd > -1) {


                                    Bean b = (Bean) getApplicationContext();
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(b.baseurl)
                                            .addConverterFactory(ScalarsConverterFactory.create())
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                    Call<checkoutBean> call2 = cr.checkShipping(p);
                                    call2.enqueue(new Callback<checkoutBean>() {
                                        @Override
                                        public void onResponse(Call<checkoutBean> call, Response<checkoutBean> response) {
                                            if (response.body().getStatus().equals("1")) {
                                                RadioButton cb = group.findViewById(iidd);

                                                paymode = cb.getText().toString();


                                                oid = String.valueOf(System.currentTimeMillis());

                                                if (paymode.equals("Cash on Delivery")) {
                                                    progress.setVisibility(View.VISIBLE);
                                                    String adr = a;

                                                    Call<checkoutBean> call3 = cr.buyVouchers2(
                                                            SharePreferenceUtils.getInstance().getString("userId"),
                                                            SharePreferenceUtils.getInstance().getString("lat"),
                                                            SharePreferenceUtils.getInstance().getString("lng"),
                                                            gtotal,
                                                            gst,
                                                            packing,
                                                            oid,
                                                            ph,
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
                                                    call3.enqueue(new Callback<checkoutBean>() {
                                                        @Override
                                                        public void onResponse(Call<checkoutBean> call, Response<checkoutBean> response) {

                                                            if (auto_cancel.equals("yes")) {
                                                                Toast.makeText(Checkout2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                                progress.setVisibility(View.GONE);

                                                                dialog = new Dialog(Checkout2.this, R.style.MyDialogTheme);
                                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                dialog.setCancelable(false);
                                                                dialog.setContentView(R.layout.wait_popup);
                                                                dialog.show();

                                                                TextView oi = dialog.findViewById(R.id.textView89);
                                                                ProgressBar bar = dialog.findViewById(R.id.progressBar6);

                                                                timer = new CountDownTimer(120000, 1000) {
                                                                    @Override
                                                                    public void onTick(long millisUntilFinished) {
                                                                        oi.setText(convertSecondsToHMmSs(millisUntilFinished / 1000));
                                                                    }

                                                                    @Override
                                                                    public void onFinish() {
                                                                        bar.setVisibility(View.VISIBLE);

                                                                        Bean b = (Bean) getApplicationContext();

                                                                        //String adr = a + ", " + ar + ", " + c + ", " + p;
                                                                        String adr = a;

                                                                        Log.d("addd", adr);

                                                                        Retrofit retrofit = new Retrofit.Builder()
                                                                                .baseUrl(b.baseurl)
                                                                                .addConverterFactory(ScalarsConverterFactory.create())
                                                                                .addConverterFactory(GsonConverterFactory.create())
                                                                                .build();

                                                                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                                                        Call<checkoutBean> call1 = cr.cancelOrder(
                                                                                response.body().getData().getId(),
                                                                                cid
                                                                        );

                                                                        call1.enqueue(new Callback<checkoutBean>() {
                                                                            @Override
                                                                            public void onResponse(Call<checkoutBean> call, Response<checkoutBean> response) {
                                                                                Toast.makeText(Checkout2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                dialog.dismiss();
                                                                                bar.setVisibility(View.GONE);
                                                                                finish();
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<checkoutBean> call, Throwable t) {
                                                                                bar.setVisibility(View.GONE);
                                                                            }
                                                                        });
                                                                    }
                                                                };

                                                                timer.start();
                                                            } else {
                                                                Toast.makeText(Checkout2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                                progress.setVisibility(View.GONE);

                                                                dialog = new Dialog(Checkout2.this, R.style.MyDialogTheme);
                                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                dialog.setCancelable(false);
                                                                dialog.setContentView(R.layout.wait_popup2);
                                                                dialog.show();

                                                                Button dismiss = dialog.findViewById(R.id.textView89);

                                                                dismiss.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        dialog.dismiss();
                                                                        finish();
                                                                    }
                                                                });

                                                            }


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
                                                Toast.makeText(Checkout2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<checkoutBean> call, Throwable t) {

                                        }
                                    });

                                } else {
                                    Toast.makeText(Checkout2.this, "Please select a Payment Mode", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Checkout2.this, "Please enter a valid Pin Code", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Checkout2.this, "Please enter a valid Address", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(Checkout2.this, "Please enter a valid Name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Checkout2.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%02d:%02d", m, s);
    }

    protected void createLocationRequest() {

        progress.setVisibility(View.VISIBLE);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(Checkout2.this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(Checkout2.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getLocation();
            }
        });

        task.addOnFailureListener(Checkout2.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but mainActivity can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(Checkout2.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }


    void getLocation() {
        if (ActivityCompat.checkSelfPermission(Checkout2.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Checkout2.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                        lat = String.valueOf(location.getLatitude());
                        lng = String.valueOf(location.getLongitude());

                        SharePreferenceUtils.getInstance().saveString("lat", lat);
                        SharePreferenceUtils.getInstance().saveString("lng", lng);

                        Log.d("lat123", lat);
                        Log.d("lat123", lng);

                        LocationServices.getFusedLocationProviderClient(Checkout2.this).removeLocationUpdates(this);

                        Geocoder geocoder = new Geocoder(Checkout2.this, Locale.getDefault());
                        List<android.location.Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(Double.parseDouble(SharePreferenceUtils.getInstance().getString("lat")), Double.parseDouble(SharePreferenceUtils.getInstance().getString("lng")), 1);
                            //Toast.makeText(Checkout2.this, "Your location: - " + addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                            SharePreferenceUtils.getInstance().saveString("deliveryLocation", addresses.get(0).getAddressLine(0));
                            //SharePreferenceUtils.getInstance().saveString("getcity", addresses.get(0).getLocality());
                            //SharePreferenceUtils.getInstance().saveString("getlocality", addresses.get(0).getSubLocality());
                            //SharePreferenceUtils.getInstance().saveString("getpincode", addresses.get(0).getPostalCode());

                            address.setText(addresses.get(0).getAddressLine(0));

                            progress.setVisibility(View.GONE);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        };

        LocationServices.getFusedLocationProviderClient(Checkout2.this).requestLocationUpdates(locationRequest, mLocationCallback, null);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                try {
                    if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {

                        double sourceLAT = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                        double sourceLNG = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);

                        String srcAddress = data.getStringExtra("addressasdasd");
                        address.setText(srcAddress);

                        SharePreferenceUtils.getInstance().saveString("lat", String.valueOf(sourceLAT));
                        SharePreferenceUtils.getInstance().saveString("lng", String.valueOf(sourceLNG));
                        SharePreferenceUtils.getInstance().saveString("address", srcAddress);

                        Log.d("loc1", String.valueOf(sourceLAT));
                        Log.d("loc1", String.valueOf(sourceLNG));

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(Checkout2.this, status.toString(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.i("TAG", "User agreed to make required location settings changes.");
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(Checkout2.this, "Location is required for mainActivity app", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        break;
                }
                break;
        }


    }

    private final BroadcastReceiver onJsonReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Log.d("cancel", "called");
                try {
                    timer.cancel();
                    dialog.dismiss();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bm.unregisterReceiver(onJsonReceived);
    }

}