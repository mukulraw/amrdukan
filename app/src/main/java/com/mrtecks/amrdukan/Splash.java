package com.mrtecks.amrdukan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private static final String TAG = "Key hash";
    Timer t;

    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }

        if (hasPermissions(this, PERMISSIONS)) {

            startApp();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
        }


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {

            Log.d("permmm", "1");

            if (hasPermissions(this, PERMISSIONS)) {

                Log.d("permmm", "2");

                startApp();

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                ) {

                    Log.d("permmm", "3");

                    Toast.makeText(getApplicationContext(), "Permissions are required for this app", Toast.LENGTH_SHORT).show();
                    finish();

                } else {

                    Log.d("permmm", "4");
                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                            .show();
                    finish();
                    //                            //proceed with logic by disabling the related features or quit the app.
                }
            }
        }

    }


    void startApp()
    {

        t = new Timer();

        t.schedule(new TimerTask() {
            @Override
            public void run() {

                final String uid = SharePreferenceUtils.getInstance().getString("userId");

                if (uid.length() > 0)
                {
                    Intent intent = new Intent(Splash.this , LocationPicker.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(Splash.this , Login.class);
                    startActivity(intent);
                    finish();
                }



            }
        } , 1200);




    }

}