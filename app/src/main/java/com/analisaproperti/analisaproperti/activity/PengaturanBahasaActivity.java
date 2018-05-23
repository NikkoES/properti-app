package com.analisaproperti.analisaproperti.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.analisaproperti.analisaproperti.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PengaturanBahasaActivity extends AppCompatActivity {

    @BindView(R.id.check_inggris)
    ImageView checkInggris;
    @BindView(R.id.check_indo)
    ImageView checkIndo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        setLocale(pref.getString("language", ""));

        setContentView(R.layout.activity_pengaturan_bahasa);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.nav_pengaturan));

        ButterKnife.bind(this);

        if(pref.getString("language", "").equalsIgnoreCase("en")) {
            checkInggris.setVisibility(View.VISIBLE);
            checkIndo.setVisibility(View.INVISIBLE);
        }
        else if(pref.getString("language", "").equalsIgnoreCase("in")) {
            checkInggris.setVisibility(View.INVISIBLE);
            checkIndo.setVisibility(View.VISIBLE);
        }
        else{
            checkInggris.setVisibility(View.VISIBLE);
            checkIndo.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.layout_inggris)
    public void setInggris(){
        setLocale("en");
        checkInggris.setVisibility(View.VISIBLE);
        checkIndo.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.layout_indo)
    public void setIndo(){
        setLocale("in");
        checkInggris.setVisibility(View.INVISIBLE);
        checkIndo.setVisibility(View.VISIBLE);
    }

    public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_PRIVATE).edit();
        editor.putString("language", lang);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        //restart app
//        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home : {
                finish();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
