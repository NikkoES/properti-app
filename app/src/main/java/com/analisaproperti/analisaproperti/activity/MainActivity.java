package com.analisaproperti.analisaproperti.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.fragment.CashFlowFragment;
import com.analisaproperti.analisaproperti.fragment.CicilanFragment;
import com.analisaproperti.analisaproperti.fragment.HomeFragment;
import com.analisaproperti.analisaproperti.fragment.NilaiPasarFragment;
import com.analisaproperti.analisaproperti.utils.SharedPreferencesUtils;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferencesUtils userDataSharedPreferences;

    GoogleApiClient googleApiClient;

    JSONObject userProfile;

    String userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        setLocale(pref.getString("language", ""));

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userDataSharedPreferences = new SharedPreferencesUtils(this, "UserData");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v =  navigationView.getHeaderView(0);
        ImageView imageProfile = v.findViewById(R.id.image_profile);
        TextView txtNama = v.findViewById(R.id.txt_nama);
        TextView txtEmail = v.findViewById(R.id.txt_email);

        try {
            userData = userDataSharedPreferences.getPreferenceData("userProfile");
            userProfile = new JSONObject(userData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glide.with(this).load(userProfile.get("profile_photo").toString()).placeholder(R.drawable.ic_profile).into(imageProfile);
            txtNama.setText(userProfile.get("name").toString());
            txtEmail.setText(userProfile.get("email").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_home);
    }

    public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        displaySelectedScreen(item.getItemId());

        return true;
    }

    public void displaySelectedScreen(int itemId){
        //creating fragment object
        Fragment fragment = null;

        Intent i;

        Context context;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_cicilan:
                fragment = new CicilanFragment();
                break;
            case R.id.nav_nilai_pasar:
                fragment = new NilaiPasarFragment();
                break;
            case R.id.nav_cash_flow:
                fragment = new CashFlowFragment();
                break;
            case R.id.nav_setting:
                i = new Intent(getApplicationContext(), PengaturanBahasaActivity.class);
                startActivity(i);
                break;
            case R.id.nav_keluar:
                logout();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void logout(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.yakin_ingin_keluar))
                .setPositiveButton(getString(R.string.ya), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userDataSharedPreferences.removeData("userProfile");
                        //logout akun google
                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                Toast.makeText(MainActivity.this, "Logout Success !", Toast.LENGTH_SHORT).show();
                            }
                        });
                        LoginManager.getInstance().logOut();
                        finish();
                        Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intentLogin);
                    }
                })
                .setNegativeButton(getString(R.string.tidak), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .show();
    }
}
