package com.analisaproperti.analisaproperti.activity.cicilan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.activity.MainActivity;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.cicilan.Cicilan;
import com.analisaproperti.analisaproperti.model.response.ResponsePost;
import com.analisaproperti.analisaproperti.utils.CurrencyEditText;
import com.analisaproperti.analisaproperti.utils.SharedPreferencesUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.concat;

public class CicilanActivity extends AppCompatActivity {

    private SharedPreferencesUtils userDataSharedPreferences;

    @BindView(R.id.ad_bottom)
    AdView bottomAds;

    @BindView(R.id.et_pinjaman_kpr)
    EditText etPinjamanKpr;
    @BindView(R.id.et_bunga_per_tahun)
    EditText etBungaPerTahun;
    @BindView(R.id.et_lama_pinjaman)
    EditText etTenorLamaPinjaman;
    @BindView(R.id.et_tenor_bunga_fix)
    EditText etTenorBungaFix;
    @BindView(R.id.et_bunga_floating)
    EditText etBungaFloating;

    @BindView(R.id.txt_sisa_pokok_pinjaman)
    TextView txtSisaPokokPinjaman;
    @BindView(R.id.txt_cicilan)
    TextView txtCicilan;
    @BindView(R.id.txt_cicilan_floating)
    TextView txtCicilanFloating;

    List<String> listBungaPerTahun = new ArrayList<>();
    List<String> listTenorPinjaman = new ArrayList<>();
    List<String> listTenorBungaFix = new ArrayList<>();
    List<String> listBungaFloating = new ArrayList<>();

    List<Cicilan> listCicilan = new ArrayList<>();
    int position;

    int bungaPerTahun, tenorLamaPinjaman, tenorBungaFix, bungaFloatingPerTahun, cicilanFloating, cicilan;

    long loPinjamanKpr, loSisaPokokPinjaman;

    double dCicilan, dCicilanFloating;

    String idCicilan, keterangan, tanggal, idUser;

    ProgressDialog loading;

    BaseApiService apiService;

    JSONObject userProfile;

    int i;

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

        setContentView(R.layout.activity_cicilan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.nav_cicilan));

        ButterKnife.bind(this);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        bottomAds.loadAd(adRequest);

        userDataSharedPreferences = new SharedPreferencesUtils(this, "UserData");

        try {
            userProfile = new JSONObject(userDataSharedPreferences.getPreferenceData("userProfile"));
            idUser = userProfile.get("id_user").toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        apiService = UtilsApi.getAPIService();

        new CurrencyEditText(etPinjamanKpr);

        Intent intent = getIntent();
        if(intent.hasExtra("position")){
            listCicilan = (ArrayList<Cicilan>)getIntent().getSerializableExtra("Cicilan");
            position = getIntent().getIntExtra("position", 0);

            final Cicilan dataCicilan = listCicilan.get(position);

            idCicilan = dataCicilan.getIdCicilan();
            keterangan = dataCicilan.getKeterangan();
            tanggal = dataCicilan.getTanggal();
            loPinjamanKpr = Long.parseLong(dataCicilan.getPinjamanKpr());
            bungaPerTahun = Integer.parseInt(dataCicilan.getBungaPerTahun());
            tenorLamaPinjaman = Integer.parseInt(dataCicilan.getTenorLamaPinjaman());
            tenorBungaFix = Integer.parseInt(dataCicilan.getTenorBungaFix());
            loSisaPokokPinjaman = Long.parseLong(dataCicilan.getSisaPokokPinjaman());
            bungaFloatingPerTahun = Integer.parseInt(dataCicilan.getBungaFloatingPerTahun());
            cicilan = Integer.parseInt(dataCicilan.getCicilan());
            cicilanFloating = Integer.parseInt(dataCicilan.getCicilanSetelahFloating());

            etPinjamanKpr.setText(""+loPinjamanKpr);
            etBungaPerTahun.setText(""+bungaPerTahun);
            etTenorLamaPinjaman.setText(""+tenorLamaPinjaman);
            etTenorBungaFix.setText(""+tenorBungaFix);
            etBungaFloating.setText(""+bungaFloatingPerTahun);
            txtSisaPokokPinjaman.setText(concat(currencyFormatterLong(loSisaPokokPinjaman)));
            txtCicilan.setText(concat(currencyFormatter(cicilan)));
            txtCicilanFloating.setText(concat(currencyFormatter(cicilanFloating)));
        }
        else{
            idCicilan = getIntent().getStringExtra("id_cicilan");
            keterangan = getIntent().getStringExtra("keterangan");
        }
    }

    @OnClick(R.id.et_bunga_per_tahun)
    public void setBungaPerTahun(){
        AlertDialog.Builder builderBungaPerTahun = new AlertDialog.Builder(this);
        builderBungaPerTahun.setTitle(getString(R.string.pilih_salah_satu_persen));

        final ArrayAdapter<String> arrayBungaPerTahun = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        for(i=1; i<=20; i++){
            arrayBungaPerTahun.add(""+i);
        }

        builderBungaPerTahun.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderBungaPerTahun.setAdapter(arrayBungaPerTahun, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayBungaPerTahun.getItem(which);
                etBungaPerTahun.setText(strName);
                bungaPerTahun = Integer.parseInt(strName);
            }
        });
        builderBungaPerTahun.show();
    }

    @OnClick(R.id.et_lama_pinjaman)
    public void setLamaPinjaman(){
        AlertDialog.Builder buiderTenorPinjaman = new AlertDialog.Builder(this);
        buiderTenorPinjaman.setTitle(getString(R.string.pilih_salah_satu_bulan));

        final ArrayAdapter<String> arrayTenorPinjaman = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        for(i=12; i<=240; i+=12){
            arrayTenorPinjaman.add(""+i);
        }

        buiderTenorPinjaman.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        buiderTenorPinjaman.setAdapter(arrayTenorPinjaman, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayTenorPinjaman.getItem(which);
                etTenorLamaPinjaman.setText(strName);
                tenorLamaPinjaman = Integer.parseInt(strName);
            }
        });
        buiderTenorPinjaman.show();
    }

    @OnClick(R.id.et_tenor_bunga_fix)
    public void setTenorBungaFix(){
        AlertDialog.Builder builderBungaFix = new AlertDialog.Builder(this);
        builderBungaFix.setTitle(getString(R.string.pilih_salah_satu_bulan));

        final ArrayAdapter<String> arrayBungaFix = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        for(i=12; i<=240; i+=12){
            arrayBungaFix.add(""+i);
        }

        builderBungaFix.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderBungaFix.setAdapter(arrayBungaFix, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayBungaFix.getItem(which);
                etTenorBungaFix.setText(strName);
                tenorBungaFix = Integer.parseInt(strName);
            }
        });
        builderBungaFix.show();
    }

    @OnClick(R.id.et_bunga_floating)
    public void setBungaFloating(){
        AlertDialog.Builder builderBungaFloating = new AlertDialog.Builder(this);
        builderBungaFloating.setTitle(getString(R.string.pilih_salah_satu_persen));

        final ArrayAdapter<String> arrayBungaFloating = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        for(i=1; i<=5; i++){
            arrayBungaFloating.add(""+i);
        }

        builderBungaFloating.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderBungaFloating.setAdapter(arrayBungaFloating, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayBungaFloating.getItem(which);
                etBungaFloating.setText(strName);
                bungaFloatingPerTahun = Integer.parseInt(strName);
            }
        });
        builderBungaFloating.show();
    }

    @OnClick(R.id.btn_hitung)
    public void hitungCicilan(){
        if(bungaPerTahun==0 || tenorLamaPinjaman==0 || tenorBungaFix==0 || bungaFloatingPerTahun==0){
            Toast.makeText(this, getString(R.string.data_belum_lengkap), Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(etPinjamanKpr.getText().toString())){
            Toast.makeText(this, getString(R.string.data_belum_lengkap), Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            String sPinjamanKpr = (etPinjamanKpr.getText().toString()).replace(".", "");
            loPinjamanKpr =  Long.parseLong(sPinjamanKpr);
        }

        //formulasi
        loSisaPokokPinjaman = loPinjamanKpr / tenorLamaPinjaman * (tenorLamaPinjaman - tenorBungaFix);

        dCicilan = pmtJava(bungaPerTahun, tenorLamaPinjaman, loPinjamanKpr);
        cicilan = (int) dCicilan;

        dCicilanFloating = pmtJava(bungaFloatingPerTahun, tenorLamaPinjaman-tenorBungaFix, loSisaPokokPinjaman);
        cicilanFloating = (int) dCicilanFloating;

        txtSisaPokokPinjaman.setText(concat(currencyFormatterLong(loSisaPokokPinjaman)));
        txtCicilan.setText(concat(currencyFormatter(cicilan)));
        txtCicilanFloating.setText(concat(currencyFormatter(cicilanFloating)));

    }

    public double pmtJava(double rate, double term, double amount) {
        double r = (rate * 0.01) / 12;
        double t = (term / 12)*12;
        return (amount * r) / (1 - Math.pow(1 + r, -t));
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

    public String currencyFormatterLong(long number){
        DecimalFormat kursIndo = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setGroupingSeparator('.');

        kursIndo.setDecimalFormatSymbols(formatRp);
        return kursIndo.format(number);
    }

    private void actionSave(){

        apiService.saveCicilan(idCicilan, ""+keterangan, ""+loPinjamanKpr, ""+bungaPerTahun, ""+tenorLamaPinjaman, ""+tenorBungaFix, ""+loSisaPokokPinjaman, ""+bungaFloatingPerTahun, ""+cicilan, ""+cicilanFloating, idUser).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){
                        Toast.makeText(getApplicationContext(), getString(R.string.berhasil_disimpan), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getString(R.string.gagal_disimpan), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal koneksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actionUpdate(){

        apiService.updateCicilan(idCicilan, ""+keterangan, ""+loPinjamanKpr, ""+bungaPerTahun, ""+tenorLamaPinjaman, ""+tenorBungaFix, ""+loSisaPokokPinjaman, ""+bungaFloatingPerTahun, ""+cicilan, ""+cicilanFloating).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){
                        Toast.makeText(getApplicationContext(), getString(R.string.berhasil_disimpan), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getString(R.string.gagal_disimpan), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal koneksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_save, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.save : {
                Intent intent = getIntent();
                if(intent.hasExtra("position")) {
                    actionUpdate();
                    finish();
                    Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else{
                    actionSave();
                    finish();
                    Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
