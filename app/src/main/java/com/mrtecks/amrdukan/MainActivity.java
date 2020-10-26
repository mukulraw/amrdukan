package com.mrtecks.amrdukan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawer;
    BottomNavigationView navigation;

    ImageView cart;
    TextView count;

    TextView address, orders, cart1, contact, logout, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        cart = findViewById(R.id.imageView2);
        navigation = findViewById(R.id.bottomNavigationView);
        count = findViewById(R.id.textView3);
        address = findViewById(R.id.textView19);
        orders = findViewById(R.id.textView20);
        cart1 = findViewById(R.id.textView21);
        contact = findViewById(R.id.textView22);
        logout = findViewById(R.id.textView26);
        name = findViewById(R.id.textView17);

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


        drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navigation.setSelectedItemId(R.id.action_cart);

            }
        });

        name.setText(SharePreferenceUtils.getInstance().getString("phone"));

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:

                        FragmentManager fm = getSupportFragmentManager();

                        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }

                        FragmentTransaction ft = fm.beginTransaction();
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        Home frag1 = new Home();
                        ft.replace(R.id.replace, frag1);
                        //ft.addToBackStack(null);
                        ft.commit();
                        //drawer.closeDrawer(GravityCompat.START);

                        break;
                    case R.id.action_search:


                        FragmentManager fm1 = getSupportFragmentManager();

                        for (int i = 0; i < fm1.getBackStackEntryCount(); ++i) {
                            fm1.popBackStack();
                        }

                        FragmentTransaction ft1 = fm1.beginTransaction();
                        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        Contact frag11 = new Contact();
                        ft1.replace(R.id.replace, frag11);
                        //ft.addToBackStack(null);
                        ft1.commit();
                        drawer.closeDrawer(GravityCompat.START);


                        // put your all data using put extra

                        //LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);


                        break;
                    case R.id.action_blog:
                        FragmentManager fm2 = getSupportFragmentManager();

                        for (int i = 0; i < fm2.getBackStackEntryCount(); ++i) {
                            fm2.popBackStack();
                        }

                        FragmentTransaction ft2 = fm2.beginTransaction();
                        ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        Search frag12 = new Search();
                        ft2.replace(R.id.replace, frag12);
                        //ft.addToBackStack(null);
                        ft2.commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.action_cart:
                        FragmentManager fm3 = getSupportFragmentManager();

                        for (int i = 0; i < fm3.getBackStackEntryCount(); ++i) {
                            fm3.popBackStack();
                        }

                        FragmentTransaction ft3 = fm3.beginTransaction();
                        ft3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        Cart frag13 = new Cart();
                        ft3.replace(R.id.replace, frag13);
                        //ft.addToBackStack(null);
                        ft3.commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.action_orders:
                        FragmentManager fm31 = getSupportFragmentManager();

                        for (int i = 0; i < fm31.getBackStackEntryCount(); ++i) {
                            fm31.popBackStack();
                        }

                        FragmentTransaction ft31 = fm31.beginTransaction();
                        ft31.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        Orders frag131 = new Orders();
                        ft31.replace(R.id.replace, frag131);
                        //ft.addToBackStack(null);
                        ft31.commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Address.class);
                startActivity(intent);

                drawer.closeDrawer(GravityCompat.START);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharePreferenceUtils.getInstance().deletePref();

                Intent intent = new Intent(MainActivity.this, Splash.class);
                startActivity(intent);
                finishAffinity();

            }
        });

        cart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navigation.setSelectedItemId(R.id.action_cart);

                drawer.closeDrawer(GravityCompat.START);

            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navigation.setSelectedItemId(R.id.action_search);

                drawer.closeDrawer(GravityCompat.START);

            }
        });


        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navigation.setSelectedItemId(R.id.action_orders);

                drawer.closeDrawer(GravityCompat.START);

            }
        });


        navigation.setSelectedItemId(R.id.action_home);

    }
}