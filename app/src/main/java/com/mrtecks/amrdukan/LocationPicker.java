package com.mrtecks.amrdukan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.button.MaterialButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.shivtechs.maplocationpicker.MapUtility;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS;

public class LocationPicker extends AppCompatActivity {

    private Context context;
    private LocationPicker activity;
    public static String[] location_permision = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    String lat = "", lng = "";
    ImageView iv_locaion_icon;
    TextView tv_address;

    MaterialButton change, confirm;
    private int AUTOCOMPLETE_REQUEST_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        context = activity = this;
        iv_locaion_icon = findViewById(R.id.iv_locaion_icon);
        tv_address = findViewById(R.id.tv_address);
        change = findViewById(R.id.change);
        confirm = findViewById(R.id.confirm);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationPicker.this, LocationPickerActivity2.class);
                //intent.putExtra(MapUtility.LATITUDE, sourceLAT);
                //intent.putExtra(MapUtility.LONGITUDE, sourceLNG);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });


        askPermis();

    }

    private void askPermis() {
        TedPermission.with(context)
                .setPermissionListener(locationlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(location_permision)
                .check();
    }

    PermissionListener locationlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            getMyLocation();

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {


        }
    };

    private void getMyLocation() {
        Glide.with(getApplicationContext())
                .asGif()
                .load(R.drawable.location_high)
                .into(iv_locaion_icon);
        if (lat.length() > 0) {
            if (SharePreferenceUtils.getInstance().getString("address").length() > 0) {
                tv_address.setText(SharePreferenceUtils.getInstance().getString("address"));
                //goHome();

            } else {
                getMyAddress();
            }

        } else {

            getMyAddress();

        }
    }

    private void getMyAddress() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableGpsDialog();

        } else {
            boolean network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (network_enabled) {


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
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location != null) {


                    getAddress(location);

                }
            }
        }
    }

    private void getAddress(Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);

            if (!addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0);
                String area = addresses.get(0).getSubAdminArea();
                String pin = addresses.get(0).getPostalCode();
                String city = addresses.get(0).getSubLocality();

                lat = String.valueOf(location.getLatitude());
                lng = String.valueOf(location.getLongitude());

                SharePreferenceUtils.getInstance().saveString("lat", lat);
                SharePreferenceUtils.getInstance().saveString("lng", lng);
                SharePreferenceUtils.getInstance().saveString("address", address);
                SharePreferenceUtils.getInstance().saveString("pin", pin);
                SharePreferenceUtils.getInstance().saveString("city", city);

                tv_address.setText(address);
                //goHome();
                Log.d("all_address" , String.valueOf(addresses.get(0)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goHome() {

        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finishAffinity();

        /*new Handler(Looper.getMainLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }, 1200);*/

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                try {
                    if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {

                        double sourceLAT = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                        double sourceLNG = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);

                        String srcAddress = data.getStringExtra("addressasdasd");
                        tv_address.setText(srcAddress);

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
                Toast.makeText(LocationPicker.this, status.toString(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.i("TAG", "User agreed to make required location settings changes.");
                    //getLocation2();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(LocationPicker.this, "Location is required for this app", Toast.LENGTH_LONG).show();
                    finishAffinity();
                    break;
            }
        }

    }

}