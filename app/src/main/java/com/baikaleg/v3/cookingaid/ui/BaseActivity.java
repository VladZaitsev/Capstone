package com.baikaleg.v3.cookingaid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.baikaleg.v3.cookingaid.R;
import com.baikaleg.v3.cookingaid.ui.basket.BasketActivity;
import com.baikaleg.v3.cookingaid.ui.recipes.RecipesActivity;
import com.baikaleg.v3.cookingaid.ui.storage.StorageActivity;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public NavigationView navigationView;
    public FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        frameLayout = findViewById(R.id.content_frame);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (item.isChecked()) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }

        if (id == R.id.nav_recipes) {
            startActivity(new Intent(getApplicationContext(), RecipesActivity.class));
        } else if (id == R.id.nav_basket) {
            startActivity(new Intent(getApplicationContext(), BasketActivity.class));
        } else if (id == R.id.nav_storage) {
            startActivity(new Intent(getApplicationContext(), StorageActivity.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
