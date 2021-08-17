package com.mrtecks.amrdukan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.SmartLocation;

public class AddAddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Context context;
    private AddAddressActivity activity;
    private LatLng current_lat_lng;
    private double latitude, longitude;
    //map
    private GoogleMap mMap;
    //location variables
    public static String[] location_permision = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private String mLandmark, mHouse_No, mArea, mAddressType, country, pincode, state, city;
    private boolean isFilled = false;
    private boolean isEditAddress = false;

    ProgressBar progress_horizontal;
    Button bt_add_addressToProceed;
    TextView tv_area, tv_full_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address2);

        this.context = this;

        progress_horizontal = findViewById(R.id.progress_horizontal);
        bt_add_addressToProceed = findViewById(R.id.bt_add_addressToProceed);
        tv_area = findViewById(R.id.tv_area);
        tv_full_address = findViewById(R.id.tv_full_address);

        askPermission();

        bt_add_addressToProceed.setOnClickListener(view -> {

            if (SharePreferenceUtils.getInstance().getString("address").length() > 0) {
                finish();
            }

        });

    }

    private void askPermission() {
        TedPermission.with(context)
                .setPermissionListener(locationlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(location_permision)
                .check();
    }

    PermissionListener locationlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            iniMap();
            setUpLocation();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            bt_add_addressToProceed.setEnabled(false);
        }
    };

    private void setUpLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableGpsDialog();

        } else {

            SmartLocation.with(context).location()
                    .start(location -> {
                        if (location != null) {
                            hideLocationProgress();

                            current_lat_lng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            latitude = current_lat_lng.latitude;
                            longitude = current_lat_lng.longitude;
                            Log.d("current_lat_lng:", new Gson().toJson(current_lat_lng));
                            //Commn.commonLog("current_lat_lng:" + new Gson().toJson(current_lat_lng));
                            focusMap();
                            setCurrentLocationMarker();
                            getAddress();
                        }
                    });


        }
    }

    private void getAddress() {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 5);

            if (!addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0);
                String area = addresses.get(0).getSubAdminArea();
                state = addresses.get(0).getLocality();
                city = addresses.get(0).getSubLocality();
                pincode = addresses.get(0).getPostalCode();
                country = addresses.get(0).getCountryName();

                tv_full_address.setText(address);
                tv_area.setText(area);

                String lat = String.valueOf(latitude);
                String lng = String.valueOf(longitude);

                SharePreferenceUtils.getInstance().saveString("lat", lat);
                SharePreferenceUtils.getInstance().saveString("lng", lng);
                SharePreferenceUtils.getInstance().saveString("address", address);
                SharePreferenceUtils.getInstance().saveString("pin", pincode);
                SharePreferenceUtils.getInstance().saveString("city", city);

                Log.d("all_address:", new Gson().toJson(addresses.get(0)));
                //Commn.commonLog("all_address:" + new Gson().toJson(addresses.get(0)) + "");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentLocationMarker() {

        if (mMap != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(current_lat_lng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
        }


    }

    private void hideLocationProgress() {
        progress_horizontal.setVisibility(View.GONE);
        bt_add_addressToProceed.setEnabled(true);
    }


    private void enableGpsDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void focusMap() {

        if (mMap != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(current_lat_lng)
                    .zoom(15)
                    .build();
            CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.animateCamera(camUpd3);

        }

    }

    private void iniMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        MapsInitializer.initialize(context);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        //create Hazards
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        mMap.setOnCameraChangeListener(cameraPosition -> {
            latitude = cameraPosition.target.latitude;
            longitude = cameraPosition.target.longitude;


            if (latitude != 0.0 && longitude != 0.0) {
                current_lat_lng = new LatLng(latitude, longitude);
                setCurrentLocationMarker();
                getAddress();
            }


            Log.d("onCameraChange:", new Gson().toJson(cameraPosition));
            //Commn.commonLog("onCameraChange:" + new Gson().toJson(cameraPosition));
        });

    }

}