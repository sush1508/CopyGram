package com.example.xeroxapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class Dashboard_page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //First page to display after launching app(when navigation drawer is not used)
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Upload_Page()).commit();
            navigationView.setCheckedItem(R.id.drawer_upload);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.drawer_upload:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Upload_Page()).commit();
                break;
            case R.id.drawer_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile_Page()).commit();
                break;
            case R.id.drawer_payment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Payment_Page()).commit();
                break;
            case R.id.drawer_contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Contact_Page()).commit();
                break;
            case R.id.drawer_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new About_Page()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
