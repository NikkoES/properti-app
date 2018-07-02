package com.analisaproperti.analisaproperti.activity.nilaipasar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.activity.MainActivity;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.nilaipasar.PropertiNilaiPasar;
import com.analisaproperti.analisaproperti.model.response.ResponsePost;
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

public class HasilNilaiPasarActivity extends AppCompatActivity {

    @BindView(R.id.txt_harga_pasaran_per_meter)
    TextView txtHargaPasaranPerMeter;
    @BindView(R.id.txt_perbandingan_properti)
    TextView txtPerbandinganProperti;
    @BindView(R.id.img_perbandingan_properti)
    ImageView imgPerbandinganProperti;

    @BindView(R.id.ad_bottom)
    AdView bottomAds;

    BaseApiService apiService;

    private SharedPreferencesUtils userDataSharedPreferences;

    JSONObject userProfile;

    String idUser, idNilaiPasar, keterangan;

    long hargaPasaranPerMeter;
    double perbandinganPropertiIncaran;

    int countProperty;

    double dHargaPasaranPerMeter = 0;

    List<PropertiNilaiPasar> listAngkaNilaiPasar = new ArrayList<>();

    String catatanKondisi, catatanSurvey;

    int hasil;

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

        setContentView(R.layout.activity_hasil_nilai_pasar);

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.final_result));

        idNilaiPasar = getIntent().getStringExtra("idNilaiPasar");
        listAngkaNilaiPasar = (ArrayList<PropertiNilaiPasar>)getIntent().getSerializableExtra("NilaiPasar");
        catatanKondisi = getIntent().getStringExtra("catatanKondisi");
        catatanSurvey = getIntent().getStringExtra("catatanSurvey");
        keterangan = getIntent().getStringExtra("keterangan");
        countProperty = getIntent().getIntExtra("Count", 0);

        //final result
        for(int j=1; j<=countProperty; j++){
            PropertiNilaiPasar pasaranPerMeter = listAngkaNilaiPasar.get(j);
            dHargaPasaranPerMeter = dHargaPasaranPerMeter + Long.parseLong(pasaranPerMeter.getHargaTanahPerMeter());
        }
        hargaPasaranPerMeter = (long) dHargaPasaranPerMeter / countProperty;
        perbandinganPropertiIncaran = 100 * Long.parseLong(listAngkaNilaiPasar.get(0).getHargaTanahPerMeter()) / hargaPasaranPerMeter;

        DecimalFormat dFormat = new DecimalFormat("#.##");
        if(hargaPasaranPerMeter<0){
            hargaPasaranPerMeter = hargaPasaranPerMeter * -1;
        }
        else{
            hargaPasaranPerMeter = hargaPasaranPerMeter * 1;
        }
        txtHargaPasaranPerMeter.setText(concat(currencyFormatter(hargaPasaranPerMeter)));
        txtPerbandinganProperti.setText(""+Double.valueOf(dFormat.format(perbandinganPropertiIncaran))+"%");
        if(perbandinganPropertiIncaran < 99){
            imgPerbandinganProperti.setImageResource(R.drawable.ic_down_green);
        }
        else{
            imgPerbandinganProperti.setImageResource(R.drawable.ic_up_red);
        }

    }

    @OnClick(R.id.btn_revisi)
    public void revisiProperty(){
        finish();
    }

    @OnClick(R.id.btn_reset)
    public void resetProperty(){
        finish();
        Intent i = new Intent(this, NilaiPasarActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public String currencyFormatter(long number){
        DecimalFormat kursIndo = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndo.setDecimalFormatSymbols(formatRp);
        return kursIndo.format(number);
    }

    private void actionSave(){
        apiService.saveNilaiPasar(idNilaiPasar, keterangan, ""+hargaPasaranPerMeter, ""+perbandinganPropertiIncaran, catatanKondisi, catatanSurvey, idUser).enqueue(new Callback<ResponsePost>() {
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
                    Toast.makeText(getApplicationContext(), getString(R.string.gagal_koneksi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.koneksi_internet_bermasalah), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProperti(String idNilaiPasar, String idProperti, String hargaJual, String luasTanah, String luasBangunan, String usiaBangunan, String hargaRata, String hargaBangunanBaru, String hargaBangunanSaatIni, String hargaTanah){
        apiService.savePropertiNilaiPasar(idNilaiPasar, idProperti, hargaJual, luasTanah, luasBangunan, usiaBangunan, hargaRata, hargaBangunanBaru, hargaBangunanSaatIni, hargaTanah).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){

                    }
                    else {

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.gagal_koneksi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.koneksi_internet_bermasalah), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePropertiDefault(String idNilaiPasar, String idProperti){
        apiService.savePropertiDefault(idNilaiPasar, idProperti).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){

                    }
                    else {

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.gagal_koneksi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.koneksi_internet_bermasalah), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actionUpdate(){
        apiService.updateNilaiPasar(idNilaiPasar, keterangan, ""+hargaPasaranPerMeter, ""+perbandinganPropertiIncaran, catatanKondisi, catatanSurvey, idUser).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){
                        Toast.makeText(getApplicationContext(), getString(R.string.berhasil_diubah), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getString(R.string.gagal_diubah), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.gagal_koneksi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.koneksi_internet_bermasalah), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProperti(String idNilaiPasar, String idProperti, String hargaJual, String luasTanah, String luasBangunan, String usiaBangunan, String hargaRata, String hargaBangunanBaru, String hargaBangunanSaatIni, String hargaTanah){
        apiService.updatePropertiNilaiPasar(idNilaiPasar, idProperti, hargaJual, luasTanah, luasBangunan, usiaBangunan, hargaRata, hargaBangunanBaru, hargaBangunanSaatIni, hargaTanah).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){

                    }
                    else {

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.gagal_koneksi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.koneksi_internet_bermasalah), Toast.LENGTH_SHORT).show();
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
                if(getIntent().getIntExtra("position", 0)==999) {
                    actionSave();

                    final String id = idNilaiPasar;

                    PropertiNilaiPasar dataProperti;
                    for(int i=0;i<listAngkaNilaiPasar.size();i++){
                        dataProperti = listAngkaNilaiPasar.get(i);
                        saveProperti(id, dataProperti.getIdProperti(), ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());
                    }
//                    for(int i=listAngkaNilaiPasar.size();i<6;i++){
//                        saveProperti(id, ""+(i+1),"0","0","0","0","0","0","0","0");
//                    }

//                    dataProperti = listAngkaNilaiPasar.get(0);
//                    saveProperti(id, ""+1, ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());
//
//                    dataProperti = listAngkaNilaiPasar.get(1);
//                    saveProperti(id, ""+2, ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());

//                    dataProperti = listAngkaNilaiPasar.get(2);
//                    saveProperti(id, ""+3, ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());
//
//                    dataProperti = listAngkaNilaiPasar.get(3);
//                    saveProperti(id, ""+4, ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());
//
//                    dataProperti = listAngkaNilaiPasar.get(4);
//                    saveProperti(id, ""+5, ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());
//
//                    dataProperti = listAngkaNilaiPasar.get(5);
//                    saveProperti(id, ""+6, ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());

                }
                else{
                    actionUpdate();

                    final String id = idNilaiPasar;

                    PropertiNilaiPasar dataProperti;
                    for(int i=0;i<listAngkaNilaiPasar.size();i++){
                        dataProperti = listAngkaNilaiPasar.get(i);
                        updateProperti(idNilaiPasar, dataProperti.getIdProperti(), ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());
                    }

//                    PropertiNilaiPasar dataProperti;
//
//
//                    dataProperti = listAngkaNilaiPasar.get(0);
//                    updateProperti(idNilaiPasar, ""+1, ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());
//
//                    dataProperti = listAngkaNilaiPasar.get(1);
//                    updateProperti(idNilaiPasar, ""+2, ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());
//
//                    dataProperti = listAngkaNilaiPasar.get(2);
//                    updateProperti(idNilaiPasar, ""+3, ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());
//
//                    dataProperti = listAngkaNilaiPasar.get(3);
//                    updateProperti(idNilaiPasar, ""+4, ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());
//
//                    dataProperti = listAngkaNilaiPasar.get(4);
//                    updateProperti(idNilaiPasar, ""+5, ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());
//
//                    dataProperti = listAngkaNilaiPasar.get(5);
//                    updateProperti(idNilaiPasar, ""+6, ""+dataProperti.getHargaJualProperti(), ""+dataProperti.getLuasTanah(),""+dataProperti.getLuasBangunan(),""+dataProperti.getUsiaBangunan(),""+dataProperti.getHargaRataPerMeter(),""+dataProperti.getHargaBangunanBaru(),""+dataProperti.getHargaBangunanSaatIni(),""+dataProperti.getHargaTanahPerMeter());

                }
                finish();
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
