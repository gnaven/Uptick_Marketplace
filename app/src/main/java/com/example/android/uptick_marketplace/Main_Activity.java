package com.example.android.uptick_marketplace;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class Main_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setTitle("");


        setContentView(R.layout.main_layout_caller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setting all defaults to home
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbar_title);
        textView.setText("Home");
        Fragment fragment = new Home();
        loadFragment(fragment);

        // Setting up bottom navigation view
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        //activates the listener with on create
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnBottomNavigationListener);

        //Setting up the Navigation view (Hamburger Menu)
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hamburger_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnBottomNavigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            TextView toolbartext = (TextView)toolbar.findViewById(R.id.toolbar_title);

            // used for activating colors of the bottom navigation view items
            BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);
            bottomNavigationView.getMenu().setGroupCheckable(0, true, true);

            // used for deactivating colors in navigation view drawer

            NavigationView navigationViewDrawer = findViewById(R.id.nav_view);
            unCheckNavView(navigationViewDrawer,false);
            //navigationViewDrawer.getMenu().setGroupCheckable(2,false,false);

            Fragment fragment = null;
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                toolbartext.setText("Profile");
                fragment = new Profile();
            } else if (id == R.id.navigation_home) {
                toolbartext.setText("Home");
                fragment = new Home();
            } else if (id == R.id.navigation_messages) {
                toolbartext.setText("Messages");
                fragment = new Message();
            }
            loadFragment(fragment);
        return true;
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle only navigation drawer view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbartext = (TextView)toolbar.findViewById(R.id.toolbar_title);

        //activates the use of navigation view drawer
        NavigationView navigationViewDrawer = findViewById(R.id.nav_view);
        unCheckNavView(navigationViewDrawer, true);
        //navigationViewDrawer.getMenu().setGroupCheckable(2,true,true);


        // deactivates the bottom navigation view for selecting navigation view drawer items
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);

        if (id == R.id.nav_settings) {
            fragment = new Settings();
            toolbartext.setText("Settings");
        } else if (id == R.id.nav_terms){
            fragment = new TermsNConditions();
            toolbartext.setText("Terms & Conditions");

        } else if (id == R.id.nav_contact){
            fragment = new Contact_Us();
            toolbartext.setText("Contact Us");
        }else if(id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, ActivityLogin.class));
        }
        loadFragment(fragment);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onStart() {
        //checks if user is logged in, if not logged in then sends them back to
        //login screen
        super.onStart();

        if (mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, ActivityLogin.class));
        }
    }

    private void loadFragment (Fragment fragment){
        /*
        method for initializing the fragments
         */
        if(fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.replace(R.id.screen_area,fragment);
            ft.commit();
        }

    }

    private void unCheckNavView(NavigationView mNavigationView, boolean check){
        int size = mNavigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            mNavigationView.getMenu().getItem(i).setCheckable(check);
        }
    }
}
