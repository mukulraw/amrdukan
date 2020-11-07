package com.mrtecks.amrdukan;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.api.ResolvableApiException;
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
import com.mrtecks.amrdukan.cartPOJO.cartBean;
import com.mrtecks.amrdukan.homePOJO.Banners;
import com.mrtecks.amrdukan.homePOJO.Best;
import com.mrtecks.amrdukan.homePOJO.Cat;
import com.mrtecks.amrdukan.homePOJO.homeBean;
import com.mrtecks.amrdukan.seingleProductPOJO.singleProductBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Home extends Fragment {

    RecyclerView categories;
    RecyclerView best;
    GridLayoutManager manager;
    LinearLayoutManager manager2;
    ViewPager banners;
    CircleIndicator indicator;

    ImageView left, right;
    boolean programaticallyScrolled;
    int currentVisibleItem = 0;

    final int duration = 10;
    final int pixelsToMove = 30;
    private final Handler mHandler = new Handler(Looper.getMainLooper());


    static MainActivity mainActivity;

    EditText search;

    CategoryAdapter categoryAdapter;
    BestAdapter bestAdapter;

    ProgressBar progress;
    List<Cat> list;
    List<Best> blist;
    ImageView banner1;
    RecyclerView obanners;
    List<Banners> bannersList;
    OfferAdapter offerAdapter;

    private FusedLocationProviderClient fusedLocationClient;

    String lat = "", lng = "";

    LocationSettingsRequest.Builder builder;
    LocationRequest locationRequest;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        mainActivity = (MainActivity) getActivity();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity);

        list = new ArrayList<>();
        blist = new ArrayList<>();
        bannersList = new ArrayList<>();

        banners = view.findViewById(R.id.viewPager);
        indicator = view.findViewById(R.id.indicator);
        categories = view.findViewById(R.id.recyclerView);
        best = view.findViewById(R.id.recyclerView2);
        progress = view.findViewById(R.id.progressBar2);
        banner1 = view.findViewById(R.id.imageView3);
        obanners = view.findViewById(R.id.imageView4);


        categoryAdapter = new CategoryAdapter(mainActivity, list);
        bestAdapter = new BestAdapter(mainActivity, blist);
        manager = new GridLayoutManager(mainActivity, 3);
        manager2 = new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false);

        offerAdapter = new OfferAdapter(mainActivity, bannersList);
        GridLayoutManager manager22 = new GridLayoutManager(mainActivity, 1);
        obanners.setAdapter(offerAdapter);
        obanners.setLayoutManager(manager22);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return categoryAdapter.getSpans(position);
            }
        });

        categories.setAdapter(categoryAdapter);
        categories.setLayoutManager(manager);

        best.setAdapter(bestAdapter);
        best.setLayoutManager(manager2);

        /*search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                FragmentTransaction ft4 = fm4.beginTransaction();
                Search frag14 = new Search();
                ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    *//*Bundle b = new Bundle();
                    b.putString("id", item.getId());
                    b.putString("title", item.getName());
                    frag14.setArguments(b);*//*
                ft4.replace(R.id.replace, frag14);
                //ft4.addToBackStack(null);
                ft4.commit();

            }
        });*/

        createLocationRequest();

        return view;
    }


    class PagerAdapter extends FragmentStatePagerAdapter {

        List<Banners> blist = new ArrayList<>();

        public PagerAdapter(@NonNull FragmentManager fm, int behavior, List<Banners> blist) {
            super(fm, behavior);
            this.blist = blist;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            page frag = new page();
            frag.setData(blist.get(position).getImage(), blist.get(position).getCname(), blist.get(position).getCid());
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

        void setData(String url, String tit, String cid) {
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
        List<Cat> list = new ArrayList<>();

        public CategoryAdapter(Context context, List<Cat> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Cat> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.category_list_model, parent, false);
            return new ViewHolder(view);
        }

        public int getSpans(int pos) {
            return Integer.parseInt(list.get(pos).getWidth());
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            final Cat item = list.get(position);


            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);


            holder.title.setText(item.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (item.getType().equals("foodkart")) {
                        Intent intent = new Intent(context, Category.class);
                        intent.putExtra("title", item.getName());
                        intent.putExtra("cid", item.getId());
                        context.startActivity(intent);
                    } else if (item.getType().equals("medicine")) {
                        Intent intent = new Intent(context, Category3.class);
                        intent.putExtra("title", item.getName());
                        intent.putExtra("cid", item.getId());
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, Category2.class);
                        intent.putExtra("title", item.getName());
                        intent.putExtra("cid", item.getId());
                        context.startActivity(intent);
                    }


                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                title = itemView.findViewById(R.id.textView7);
            }
        }
    }

    class BestAdapter extends RecyclerView.Adapter<BestAdapter.ViewHolder> {
        Context context;
        List<Best> list = new ArrayList<>();

        BestAdapter(Context context, List<Best> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Best> list) {
            this.list = list;
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.best_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Best item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);


            holder.selling.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(item.getPrice()) + " </b></font>"));

            holder.mrp.setText(Html.fromHtml("<strike>\u20B9 " + item.getDiscount() + "</strike>"));


            holder.title.setText(item.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, SingleProduct2.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("title", item.getName());
                    startActivity(intent);

                }
            });

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String uid = SharePreferenceUtils.getInstance().getString("userId");

                    if (uid.length() > 0) {

                        final Dialog dialog = new Dialog(context, R.style.MyDialogTheme);
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

                                Bean b = (Bean) context.getApplicationContext();


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
                                Log.d("pid", item.getId());
                                Log.d("quantity", String.valueOf(stepperTouch.getCount()));

                                int versionCode = BuildConfig.VERSION_CODE;
                                String versionName = BuildConfig.VERSION_NAME;

                                Call<singleProductBean> call = cr.addCart(SharePreferenceUtils.getInstance().getString("userId"), item.getId(), String.valueOf(stepperTouch.getCount()), item.getDiscount(), versionName);

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1")) {
                                            //loadCart();
                                            dialog.dismiss();
                                            loadCart();
                                        }

                                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(context, "Please login to continue", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Login.class);
                        context.startActivity(intent);

                    }

                }
            });

        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView title, selling, mrp;
            Button add;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.imageView5);
                title = itemView.findViewById(R.id.textView2);
                selling = itemView.findViewById(R.id.textView4);
                mrp = itemView.findViewById(R.id.textView6);
                add = itemView.findViewById(R.id.button);
            }
        }
    }

    class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

        Context context;
        List<Banners> list = new ArrayList<>();

        public OfferAdapter(Context context, List<Banners> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Banners> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.best_list_model1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Banners item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);

/*
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (item.getCid() != null) {
                        FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                        for (int i = 0; i < fm4.getBackStackEntryCount(); ++i) {
                            fm4.popBackStack();
                        }

                        FragmentTransaction ft4 = fm4.beginTransaction();
                        SubCat frag14 = new SubCat();
                        Bundle b = new Bundle();
                        b.putString("id", item.getCid());
                        b.putString("title", item.getCname());
                        b.putString("image", item.getCatimage());
                        frag14.setArguments(b);
                        ft4.replace(R.id.replace, frag14);
                        ft4.addToBackStack(null);
                        //ft.addToBackStack(null);
                        ft4.commit();
                    }


                }
            });
*/


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView4);


            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();


        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) mainActivity.getApplicationContext();

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

        Call<homeBean> call = cr.getHome(SharePreferenceUtils.getInstance().getString("userId"));
        call.enqueue(new Callback<homeBean>() {
            @Override
            public void onResponse(Call<homeBean> call, Response<homeBean> response) {


                if (response.body().getStatus().equals("1")) {


                    final PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, response.body().getPbanner());

                    banners.setAdapter(adapter);
                    indicator.setViewPager(banners);

                    bestAdapter.setData(response.body().getBest());

                    categoryAdapter.setData(response.body().getCat());

                    try {
                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                        ImageLoader loader = ImageLoader.getInstance();
                        String url = response.body().getObanner().get(0).getImage();
                        loader.displayImage(url, banner1, options);

                        String cid = response.body().getObanner().get(0).getCid();
                        String tit = response.body().getObanner().get(0).getCname();
                        //String image = response.body().getObanner().get(0).getCatimage();

/*
                        banner1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cid != null) {

                                    FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                                    for (int i = 0; i < fm4.getBackStackEntryCount(); ++i) {
                                        fm4.popBackStack();
                                    }

                                    FragmentTransaction ft4 = fm4.beginTransaction();
                                    SubCat frag14 = new SubCat();
                                    Bundle b = new Bundle();
                                    b.putString("id", cid);
                                    b.putString("title", tit);
                                    b.putString("image", image);
                                    frag14.setArguments(b);
                                    ft4.replace(R.id.replace, frag14);
                                    ft4.addToBackStack(null);
                                    //ft.addToBackStack(null);
                                    ft4.commit();

                                }
                            }
                        });
*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (response.body().getObanner().size() > 1) {
                            List<Banners> ll = response.body().getObanner();
                            ll.remove(0);
                            offerAdapter.setData(ll);
                        }

                        /*SharePreferenceUtils.getInstance().saveString("location", response.body().getLocation());

                        Geocoder geocoder = new Geocoder(mainActivity, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(SharePreferenceUtils.getInstance().getString("lat")), Double.parseDouble(SharePreferenceUtils.getInstance().getString("lng")), 1);

                        Log.d("address", addresses.toString());

                        SharePreferenceUtils.getInstance().saveString("deliveryLocation", addresses.get(0).getAddressLine(0));
                        mainActivity.location.setText(addresses.get(0).getAddressLine(0));*/
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("address", e.toString());
                    }

                }

                progress.setVisibility(View.GONE);

                Log.d("asdasd", response.body().getMessage());

            }

            @Override
            public void onFailure(Call<homeBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Log.d("asdasd", t.toString());
            }
        });
        loadCart();


    }

    void loadCart() {
        String uid = SharePreferenceUtils.getInstance().getString("userId");

        if (uid.length() > 0) {
            Bean b = (Bean) mainActivity.getApplicationContext();

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

            Call<cartBean> call2 = cr.getCart(SharePreferenceUtils.getInstance().getString("userId"));
            call2.enqueue(new Callback<cartBean>() {
                @Override
                public void onResponse(Call<cartBean> call, Response<cartBean> response) {

                    if (response.body().getData().size() > 0) {


                        mainActivity.count.setText(String.valueOf(response.body().getData().size()));


                    } else {

                        mainActivity.count.setText("0");

                    }

                    progress.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<cartBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });


        } else {
            mainActivity.count.setText("0");
        }
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(mainActivity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(mainActivity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getLocation();
            }
        });

        task.addOnFailureListener(mainActivity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but mainActivity can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(mainActivity,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }


    void getLocation() {
        if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                        LocationServices.getFusedLocationProviderClient(mainActivity).removeLocationUpdates(this);

                        Geocoder geocoder = new Geocoder(mainActivity, Locale.getDefault());
                        List<android.location.Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(Double.parseDouble(SharePreferenceUtils.getInstance().getString("lat")), Double.parseDouble(SharePreferenceUtils.getInstance().getString("lng")), 1);
                            //mainActivity.toolbar.setSubtitle(addresses.get(0).getPostalCode());
                            Toast.makeText(mainActivity, "Your location: - " + addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                            SharePreferenceUtils.getInstance().saveString("deliveryLocation", addresses.get(0).getAddressLine(0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        };

        LocationServices.getFusedLocationProviderClient(mainActivity).requestLocationUpdates(locationRequest, mLocationCallback, null);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.i("TAG", "User agreed to make required location settings changes.");
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(mainActivity, "Location is required for mainActivity app", Toast.LENGTH_LONG).show();
                        mainActivity.finishAffinity();
                        break;
                }
                break;
        }


    }

}
