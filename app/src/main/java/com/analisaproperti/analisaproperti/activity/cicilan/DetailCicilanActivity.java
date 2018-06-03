package com.analisaproperti.analisaproperti.activity.cicilan;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.model.cicilan.Cicilan;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailCicilanActivity extends AppCompatActivity {

    @BindView(R.id.txt_pinjaman_kpr)
    TextView txtPinjamanKpr;
    @BindView(R.id.txt_tenor_bunga_Fix)
    TextView txtTenorBungaFix;
    @BindView(R.id.txt_bunga_per_tahun)
    TextView txtBungaPerTahun;
    @BindView(R.id.txt_sisa_pokok_pinjaman)
    TextView txtSisaPokokPinjaman;
    @BindView(R.id.txt_tenor_lama_pinjaman)
    TextView txtTenorLamaPinjaman;
    @BindView(R.id.txt_bunga_floating)
    TextView txtBungaFloating;

    @BindView(R.id.txt_cicilan)
    TextView txtCicilan;
    @BindView(R.id.txt_cicilan_floating)
    TextView txtCicilanFloating;

    @BindView(R.id.ad_top)
    AdView topAds;
    @BindView(R.id.ad_bottom)
    AdView bottomAds;

    List<Cicilan> listCicilan = new ArrayList<>();
    int position;

    int bungaPerTahun, tenorLamaPinjaman, tenorBungaFix, bungaFloatingPerTahun, cicilanFloating, cicilan;

    long loPinjamanKpr, loSisaPokokPinjaman;

    public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        setLocale(pref.getString("language", ""));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cicilan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.detail_cicilan));

        ButterKnife.bind(this);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        topAds.loadAd(adRequest);
        bottomAds.loadAd(adRequest);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        listCicilan = (ArrayList<Cicilan>)getIntent().getSerializableExtra("Cicilan");
        position = getIntent().getIntExtra("position", 0);

        final Cicilan dataCicilan = listCicilan.get(position);

        loPinjamanKpr = Long.parseLong(dataCicilan.getPinjamanKpr());
        bungaPerTahun = Integer.parseInt(dataCicilan.getBungaPerTahun());
        tenorLamaPinjaman = Integer.parseInt(dataCicilan.getTenorLamaPinjaman());
        tenorBungaFix = Integer.parseInt(dataCicilan.getTenorBungaFix());
        loSisaPokokPinjaman = Long.parseLong(dataCicilan.getSisaPokokPinjaman());
        bungaFloatingPerTahun = Integer.parseInt(dataCicilan.getBungaFloatingPerTahun());
        cicilan = Integer.parseInt(dataCicilan.getCicilan());
        cicilanFloating = Integer.parseInt(dataCicilan.getCicilanSetelahFloating());

        txtPinjamanKpr.setText(currencyFormatterLong(loPinjamanKpr));
        txtBungaPerTahun.setText(bungaPerTahun+"%");
        txtTenorLamaPinjaman.setText(""+tenorLamaPinjaman);
        txtTenorBungaFix.setText(""+tenorBungaFix);
        txtSisaPokokPinjaman.setText(currencyFormatterLong(loSisaPokokPinjaman));
        txtBungaFloating.setText(bungaFloatingPerTahun+"%");
        txtCicilan.setText(currencyFormatter(cicilan));
        txtCicilanFloating.setText(currencyFormatter(cicilanFloating));

    }

    public String currencyFormatterLong(long number){
        DecimalFormat kursIndo = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setGroupingSeparator('.');

        kursIndo.setDecimalFormatSymbols(formatRp);
        return kursIndo.format(number);
    }

    public String currencyFormatter(int number){
        DecimalFormat kursIndo = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setGroupingSeparator('.');

        kursIndo.setDecimalFormatSymbols(formatRp);
        return kursIndo.format(number);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
