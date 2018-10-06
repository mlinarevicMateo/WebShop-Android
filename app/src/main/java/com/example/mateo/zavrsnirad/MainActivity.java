package com.example.mateo.zavrsnirad;


import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        setTitle("WebShop");

        String USER_LOGGED_IN_SP = getResources().getString(R.string.USER_LOGGED_IN_SP);

        SharedPreferences sharedPrefs = getSharedPreferences(USER_LOGGED_IN_SP, MODE_PRIVATE);
        SharedPreferences.Editor ed;

        if (sharedPrefs.getBoolean(USER_LOGGED_IN_SP, false) == false) {
            ed = sharedPrefs.edit();

            ed.putBoolean(USER_LOGGED_IN_SP, false);

            ed.commit();

            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        } else {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            if (isNetworkAvailable()) {
                fm.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
            } else {
                fm.beginTransaction().replace(R.id.content_frame, new NoInternet()).commit();
            }

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(i);
                }
            });

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            if (isNetworkAvailable()) {

                String USER_EMAIL = getResources().getString(R.string.USER_EMAIL);
                String USER_NAME = getResources().getString(R.string.USER_NAME);
                String USER_IMAGE = getResources().getString(R.string.USER_IMAGE);

                String email = sharedPrefs.getString(USER_EMAIL, getString(R.string.DEFAULT_VALUE_SP));
                String name = sharedPrefs.getString(USER_NAME, getString(R.string.DEFAULT_VALUE_SP));
                String image = sharedPrefs.getString(USER_IMAGE, getString(R.string.DEFAULT_VALUE_SP));


                NavigationView navigationView = findViewById(R.id.nav_view);
                View hView = navigationView.getHeaderView(0);
                navigationView.setNavigationItemSelectedListener(this);
                navigationView.getMenu().getItem(0).setChecked(true);

                ImageView user_image = hView.findViewById(R.id.navHeaderUserImage);
                TextView user_email = hView.findViewById(R.id.navHeaderUserEmail);
                TextView user_name = hView.findViewById(R.id.navHeaderUserName);

                Picasso.with(MainActivity.this).load(MainActivity.this.getString(R.string.IMAGES_URL) + image).into(user_image);
                user_email.setText(email);
                user_name.setText(name);

                Cart cart = Cart.getInstance();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_all_articles) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_laptops) {
            fragment = setFragment(getResources().getString(R.string.laptops_id));
        } else if (id == R.id.nav_cases) {
            fragment = setFragment(getResources().getString(R.string.cases_id));
        } else if (id == R.id.nav_monitors) {
            fragment = setFragment(getResources().getString(R.string.monitors_id));
        } else if (id == R.id.nav_mouses) {
            fragment = setFragment(getResources().getString(R.string.mouses_id));
        } else if (id == R.id.nav_headphones) {
            fragment = setFragment(getResources().getString(R.string.speakers_id));
        } else if (id == R.id.nav_keyboard) {
            fragment = setFragment(getResources().getString(R.string.keyboards_id));
        } else if (id == R.id.nav_logout) {
            logout();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Fragment setFragment(String id) {
        Bundle bundle = new Bundle();
        Fragment fragment = new ByCategory();
        bundle.putString("id", id);
        fragment.setArguments(bundle);

        return fragment;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void logout() {
        String USER_LOGGED_IN_SP = getResources().getString(R.string.USER_LOGGED_IN_SP);
        SharedPreferences preferences = getSharedPreferences(USER_LOGGED_IN_SP, MODE_PRIVATE);
        preferences.edit().clear().apply();

        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }


}
