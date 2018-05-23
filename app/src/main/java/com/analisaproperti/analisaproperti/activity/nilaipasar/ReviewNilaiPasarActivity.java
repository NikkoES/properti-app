package com.analisaproperti.analisaproperti.activity.nilaipasar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.model.nilaipasar.PropertiNilaiPasar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.concat;

public class ReviewNilaiPasarActivity extends AppCompatActivity {

    @BindView(R.id.row_harga_jual_properti)
    View rowHargaJualProperti;
    @BindView(R.id.row_luas_tanah)
    View rowLuasTanah;
    @BindView(R.id.row_luas_bangunan)
    View rowLuasBangunan;
    @BindView(R.id.row_usia_bangunan)
    View rowUsiaBangunan;
    @BindView(R.id.row_harga_rata_rata)
    View rowHargaRata;

    List<PropertiNilaiPasar> listAngkaNilaiPasar = new ArrayList<>();

    int countProperty;

    long hargaPasaranPerMeter;

    double perbandinganPropertiIncaran;

    double dHargaPasaranPerMeter = 0;

    @BindView(R.id.ad_top)
    AdView topAds;
    @BindView(R.id.ad_bottom)
    AdView bottomAds;

    public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        setLocale(pref.getString("language", ""));

        setContentView(R.layout.activity_review_nilai_pasar);

        ButterKnife.bind(this);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        topAds.loadAd(adRequest);
        bottomAds.loadAd(adRequest);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.judul_review_nilai_pasar));

        listAngkaNilaiPasar = (ArrayList<PropertiNilaiPasar>)getIntent().getSerializableExtra("NilaiPasar");
        countProperty = getIntent().getIntExtra("Count", 0);

        if(countProperty==2){
            countProperty = 1;
        }
        else if(countProperty==4){
            countProperty = 2;
        }
        else if(countProperty==6){
            countProperty = 3;
        }
        else if(countProperty==8){
            countProperty = 4;
        }
        else if(countProperty==10){
            countProperty = 5;
        }

        //final result
        for(int j=1; j<=countProperty; j++){
            PropertiNilaiPasar pasaranPerMeter = listAngkaNilaiPasar.get(j);
            dHargaPasaranPerMeter = dHargaPasaranPerMeter + Long.parseLong(pasaranPerMeter.getHargaTanahPerMeter());
        }
        hargaPasaranPerMeter = (long) dHargaPasaranPerMeter / countProperty;
        perbandinganPropertiIncaran = 100 * Long.parseLong(listAngkaNilaiPasar.get(0).getHargaTanahPerMeter()) / hargaPasaranPerMeter;

        int size = 8;
        int sizeInDp = (int) (size * getResources().getDisplayMetrics().density + 0.5f);

        //row table
        for(int a=0; a<=5; a++){
            TextView txtHargaJualProperti = new TextView(this);
            TextView txtLuasTanah = new TextView(this);
            TextView txtLuasBangunan = new TextView(this);
            TextView txtUsiaBangunan = new TextView(this);
            TextView txtHargaRata = new TextView(this);

            txtHargaJualProperti.setText(""+concat(currencyFormatter(Long.parseLong(listAngkaNilaiPasar.get(a).getHargaJualProperti()))));
            txtHargaJualProperti.setPadding(sizeInDp, sizeInDp, sizeInDp, sizeInDp);
            txtHargaJualProperti.setGravity(Gravity.CENTER);
            txtHargaJualProperti.setBackgroundResource(R.drawable.table_cell_bg);
            txtHargaJualProperti.setTextColor(getResources().getColor(R.color.colorAccent));

            txtLuasTanah.setText(""+listAngkaNilaiPasar.get(a).getLuasTanah());
            txtLuasTanah.setPadding(sizeInDp, sizeInDp, sizeInDp, sizeInDp);
            txtLuasTanah.setGravity(Gravity.CENTER);
            txtLuasTanah.setBackgroundResource(R.drawable.table_cell_bg);
            txtLuasTanah.setTextColor(getResources().getColor(R.color.colorAccent));

            txtLuasBangunan.setText(""+listAngkaNilaiPasar.get(a).getLuasBangunan());
            txtLuasBangunan.setPadding(sizeInDp, sizeInDp, sizeInDp, sizeInDp);
            txtLuasBangunan.setGravity(Gravity.CENTER);
            txtLuasBangunan.setBackgroundResource(R.drawable.table_cell_bg);
            txtLuasBangunan.setTextColor(getResources().getColor(R.color.colorAccent));

            txtUsiaBangunan.setText(""+listAngkaNilaiPasar.get(a).getUsiaBangunan());
            txtUsiaBangunan.setPadding(sizeInDp, sizeInDp, sizeInDp, sizeInDp);
            txtUsiaBangunan.setGravity(Gravity.CENTER);
            txtUsiaBangunan.setBackgroundResource(R.drawable.table_cell_bg);
            txtUsiaBangunan.setTextColor(getResources().getColor(R.color.colorAccent));

            txtHargaRata.setText(""+concat(currencyFormatter(Long.parseLong(listAngkaNilaiPasar.get(a).getHargaRataPerMeter()))));
            txtHargaRata.setPadding(sizeInDp, sizeInDp, sizeInDp, sizeInDp);
            txtHargaRata.setGravity(Gravity.CENTER);
            txtHargaRata.setBackgroundResource(R.drawable.table_cell_bg);
            txtHargaRata.setTextColor(getResources().getColor(R.color.colorAccent));

            ((TableRow) rowHargaJualProperti).addView(txtHargaJualProperti);
            ((TableRow) rowLuasTanah).addView(txtLuasTanah);
            ((TableRow) rowLuasBangunan).addView(txtLuasBangunan);
            ((TableRow) rowUsiaBangunan).addView(txtUsiaBangunan);
            ((TableRow) rowHargaRata).addView(txtHargaRata);
        }
    }

    public String currencyFormatter(long number){
        DecimalFormat kursIndo = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setGroupingSeparator('.');

        kursIndo.setDecimalFormatSymbols(formatRp);
        return kursIndo.format(number);
    }

    @OnClick(R.id.btn_back)
    public void backReview(){
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        for (int j=5; j>countProperty; j--) {
            listAngkaNilaiPasar.remove(j);
        }
    }

    @OnClick(R.id.btn_finish)
    public void finishReview(){
        finish();
        Intent i = new Intent(ReviewNilaiPasarActivity.this, HasilNilaiPasarActivity.class);
        i.putExtra("NilaiPasar", (ArrayList<PropertiNilaiPasar>)listAngkaNilaiPasar);
        i.putExtra("HargaPasaran", hargaPasaranPerMeter);
        i.putExtra("Perbandingan", perbandinganPropertiIncaran);
        i.putExtra("catatanKondisi", getIntent().getStringExtra("catatanKondisi"));
        i.putExtra("catatanSurvey", getIntent().getStringExtra("catatanSurvey"));
        i.putExtra("idNilaiPasar", getIntent().getStringExtra("idNilaiPasar"));
        i.putExtra("keterangan", getIntent().getStringExtra("keterangan"));
        i.putExtra("position", getIntent().getIntExtra("position", 0));
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        for (int j=0; j<=5; j++) {
            listAngkaNilaiPasar.remove(j);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home : {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
