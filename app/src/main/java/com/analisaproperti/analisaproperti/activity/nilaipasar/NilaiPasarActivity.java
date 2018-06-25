package com.analisaproperti.analisaproperti.activity.nilaipasar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.nilaipasar.PropertiNilaiPasar;
import com.analisaproperti.analisaproperti.model.response.ResponsePropertiNilaiPasar;
import com.analisaproperti.analisaproperti.utils.CurrencyEditText;
import com.analisaproperti.analisaproperti.utils.SharedPreferencesUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NilaiPasarActivity extends AppCompatActivity {

    @BindView(R.id.rg_tabs)
    RadioGroup tabs;
    @BindView(R.id.txt_nama_property)
    TextView txtNamaProperty;

    @BindView(R.id.layout_property_incaran)
    LinearLayout layoutPropertyIncaran;
    @BindView(R.id.layout_property_satu)
    LinearLayout layoutPropertySatu;
    @BindView(R.id.layout_property_dua)
    LinearLayout layoutPropertyDua;
    @BindView(R.id.layout_property_tiga)
    LinearLayout layoutPropertyTiga;
    @BindView(R.id.layout_property_empat)
    LinearLayout layoutPropertyEmpat;
    @BindView(R.id.layout_property_lima)
    LinearLayout layoutPropertyLima;

    //properti incaran
    @BindView(R.id.et_harga_jual_property)
    EditText etHargaJualProperty;
    @BindView(R.id.et_luas_tanah)
    EditText etLuasTanah;
    @BindView(R.id.et_luas_bangunan)
    EditText etLuasBangunan;
    @BindView(R.id.et_usia_bangunan)
    EditText etUsiaBangunan;
    @BindView(R.id.et_harga_rata_bangunan_per_meter)
    EditText etHargaRataBangunanPerMeter;

    //properti satu
    @BindView(R.id.et_harga_jual_property_satu)
    EditText etHargaJualPropertySatu;
    @BindView(R.id.et_luas_tanah_satu)
    EditText etLuasTanahSatu;
    @BindView(R.id.et_luas_bangunan_satu)
    EditText etLuasBangunanSatu;
    @BindView(R.id.et_usia_bangunan_satu)
    EditText etUsiaBangunanSatu;
    @BindView(R.id.et_harga_rata_bangunan_per_meter_satu)
    EditText etHargaRataBangunanPerMeterSatu;

    //properti dua
    @BindView(R.id.et_harga_jual_property_dua)
    EditText etHargaJualPropertyDua;
    @BindView(R.id.et_luas_tanah_dua)
    EditText etLuasTanahDua;
    @BindView(R.id.et_luas_bangunan_dua)
    EditText etLuasBangunanDua;
    @BindView(R.id.et_usia_bangunan_dua)
    EditText etUsiaBangunanDua;
    @BindView(R.id.et_harga_rata_bangunan_per_meter_dua)
    EditText etHargaRataBangunanPerMeterDua;

    //properti tiga
    @BindView(R.id.et_harga_jual_property_tiga)
    EditText etHargaJualPropertyTiga;
    @BindView(R.id.et_luas_tanah_tiga)
    EditText etLuasTanahTiga;
    @BindView(R.id.et_luas_bangunan_tiga)
    EditText etLuasBangunanTiga;
    @BindView(R.id.et_usia_bangunan_tiga)
    EditText etUsiaBangunanTiga;
    @BindView(R.id.et_harga_rata_bangunan_per_meter_tiga)
    EditText etHargaRataBangunanPerMeterTiga;

    //properti empat
    @BindView(R.id.et_harga_jual_property_empat)
    EditText etHargaJualPropertyEmpat;
    @BindView(R.id.et_luas_tanah_empat)
    EditText etLuasTanahEmpat;
    @BindView(R.id.et_luas_bangunan_empat)
    EditText etLuasBangunanEmpat;
    @BindView(R.id.et_usia_bangunan_empat)
    EditText etUsiaBangunanEmpat;
    @BindView(R.id.et_harga_rata_bangunan_per_meter_empat)
    EditText etHargaRataBangunanPerMeterEmpat;

    //properti lima
    @BindView(R.id.et_harga_jual_property_lima)
    EditText etHargaJualPropertyLima;
    @BindView(R.id.et_luas_tanah_lima)
    EditText etLuasTanahLima;
    @BindView(R.id.et_luas_bangunan_lima)
    EditText etLuasBangunanLima;
    @BindView(R.id.et_usia_bangunan_lima)
    EditText etUsiaBangunanLima;
    @BindView(R.id.et_harga_rata_bangunan_per_meter_lima)
    EditText etHargaRataBangunanPerMeterLima;

    @BindView(R.id.et_catatan_kondisi_bangunan)
    EditText etCatatanKondisiBangunan;
    @BindView(R.id.et_catatan_survey_lokasi)
    EditText etCatatanSurveyLokasi;

    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_review)
    Button btnReview;

    @BindView(R.id.layout_catatan)
    CardView layoutCatatan;

    private SharedPreferencesUtils dataSharedPreferences;

    BaseApiService apiService;

    JSONObject userProfile, propertiNilaiPasar;

    String idNilaiPasar, keterangan, tanggal, idUser;

    String catatanKondisi, catatanSurvey;

    int properti, next, back, valueProperty;

    long hargaJualProperty, luasTanah, luasBangunan, usiaBangunan, hargaRataBangunanPerMeter;
    long hargaJualProperty1, luasTanah1, luasBangunan1, usiaBangunan1, hargaRataBangunanPerMeter1;
    long hargaJualProperty2, luasTanah2, luasBangunan2, usiaBangunan2, hargaRataBangunanPerMeter2;
    long hargaJualProperty3, luasTanah3, luasBangunan3, usiaBangunan3, hargaRataBangunanPerMeter3;
    long hargaJualProperty4, luasTanah4, luasBangunan4, usiaBangunan4, hargaRataBangunanPerMeter4;
    long hargaJualProperty5, luasTanah5, luasBangunan5, usiaBangunan5, hargaRataBangunanPerMeter5;

    long hargaBangunanBaru, hargaBangunanSaatIni, hargaTanahPerMeter;
    long hargaBangunanBaru1, hargaBangunanSaatIni1, hargaTanahPerMeter1;
    long hargaBangunanBaru2, hargaBangunanSaatIni2, hargaTanahPerMeter2;
    long hargaBangunanBaru3, hargaBangunanSaatIni3, hargaTanahPerMeter3;
    long hargaBangunanBaru4, hargaBangunanSaatIni4, hargaTanahPerMeter4;
    long hargaBangunanBaru5, hargaBangunanSaatIni5, hargaTanahPerMeter5;

    double dHargaBangunanSaatIni, dHargaBangunanSaatIni1, dHargaBangunanSaatIni2, dHargaBangunanSaatIni3, dHargaBangunanSaatIni4, dHargaBangunanSaatIni5;

    List<PropertiNilaiPasar> listAngkaNilaiPasar = new ArrayList<>();

    int count;

    int position;

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

        setContentView(R.layout.activity_nilai_pasar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.nav_nilai_pasar));

        ButterKnife.bind(this);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        bottomAds.loadAd(adRequest);

        dataSharedPreferences = new SharedPreferencesUtils(this, "UserData");

        apiService = UtilsApi.getAPIService();

        setCurrencyEditText();

        try {
            userProfile = new JSONObject(dataSharedPreferences.getPreferenceData("userProfile"));
            idUser = userProfile.get("id_user").toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        if(intent.hasExtra("position")){
            try {
                propertiNilaiPasar = new JSONObject(getIntent().getStringExtra("propertiNilaiPasar"));
                idNilaiPasar = propertiNilaiPasar.get("id_nilai_pasar").toString();
                keterangan = propertiNilaiPasar.get("keterangan").toString();

                catatanKondisi = propertiNilaiPasar.get("catatan_kondisi_bangunan").toString();
                catatanSurvey = propertiNilaiPasar.get("catatan_survey_lokasi").toString();

                position = getIntent().getIntExtra("position", 0);
//                listAngkaNilaiPasar = (ArrayList<PropertiNilaiPasar>)getIntent().getSerializableExtra("properti");
                idNilaiPasar = getIntent().getStringExtra("id_nilai_pasar");

                etCatatanKondisiBangunan.setText(catatanKondisi);
                etCatatanSurveyLokasi.setText(catatanSurvey);

                getDataProperti(idNilaiPasar);

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            idNilaiPasar = getIntent().getStringExtra("id_nilai_pasar");
            keterangan = getIntent().getStringExtra("keterangan");
            position = 999;
        }

        for(int i=0;i<=5;i++){
            listAngkaNilaiPasar.add(new PropertiNilaiPasar(idNilaiPasar,""+(i+1),"0","0","0","0","0","0", "", ""));
        }

        btnBack.setVisibility(View.INVISIBLE);
        btnReview.setVisibility(View.INVISIBLE);

        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup,  @IdRes int i) {
                tabsHandler(i);
            }
        });
    }

    public void getDataProperti(String idNilaiPasar){
        apiService.getDataNilaiPasar(idNilaiPasar).enqueue(new Callback<ResponsePropertiNilaiPasar>() {
            @Override
            public void onResponse(Call<ResponsePropertiNilaiPasar> call, Response<ResponsePropertiNilaiPasar> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equalsIgnoreCase("success")){
                        listAngkaNilaiPasar = response.body().getData();

                        Toast.makeText(NilaiPasarActivity.this, ""+listAngkaNilaiPasar.size(), Toast.LENGTH_SHORT).show();

                        PropertiNilaiPasar properti;

                        properti = listAngkaNilaiPasar.get(0);
                        hargaJualProperty = Long.parseLong(properti.getHargaJualProperti());
                        luasTanah = Long.parseLong(properti.getLuasTanah());
                        luasBangunan = Long.parseLong(properti.getLuasBangunan());
                        usiaBangunan = Long.parseLong(properti.getUsiaBangunan());
                        hargaRataBangunanPerMeter = Long.parseLong(properti.getHargaRataPerMeter());

                        properti = listAngkaNilaiPasar.get(1);
                        hargaJualProperty1 = Long.parseLong(properti.getHargaJualProperti());
                        luasTanah1 = Long.parseLong(properti.getLuasTanah());
                        luasBangunan1 = Long.parseLong(properti.getLuasBangunan());
                        usiaBangunan1 = Long.parseLong(properti.getUsiaBangunan());
                        hargaRataBangunanPerMeter1 = Long.parseLong(properti.getHargaRataPerMeter());

                        if(listAngkaNilaiPasar.size()==3){
                            properti = listAngkaNilaiPasar.get(2);
                            hargaJualProperty2 = Long.parseLong(properti.getHargaJualProperti());
                            luasTanah2 = Long.parseLong(properti.getLuasTanah());
                            luasBangunan2 = Long.parseLong(properti.getLuasBangunan());
                            usiaBangunan2 = Long.parseLong(properti.getUsiaBangunan());
                            hargaRataBangunanPerMeter2 = Long.parseLong(properti.getHargaRataPerMeter());
                        }
                        else if(listAngkaNilaiPasar.size()==4){
                            properti = listAngkaNilaiPasar.get(3);
                            hargaJualProperty3 = Long.parseLong(properti.getHargaJualProperti());
                            luasTanah3 = Long.parseLong(properti.getLuasTanah());
                            luasBangunan3 = Long.parseLong(properti.getLuasBangunan());
                            usiaBangunan3 = Long.parseLong(properti.getUsiaBangunan());
                            hargaRataBangunanPerMeter3 = Long.parseLong(properti.getHargaRataPerMeter());
                        }
                        else if(listAngkaNilaiPasar.size()==5) {
                            properti = listAngkaNilaiPasar.get(4);
                            hargaJualProperty4 = Long.parseLong(properti.getHargaJualProperti());
                            luasTanah4 = Long.parseLong(properti.getLuasTanah());
                            luasBangunan4 = Long.parseLong(properti.getLuasBangunan());
                            usiaBangunan4 = Long.parseLong(properti.getUsiaBangunan());
                            hargaRataBangunanPerMeter4 = Long.parseLong(properti.getHargaRataPerMeter());
                        }
                        else if(listAngkaNilaiPasar.size()==6){
                            properti = listAngkaNilaiPasar.get(5);
                            hargaJualProperty5 = Long.parseLong(properti.getHargaJualProperti());
                            luasTanah5 = Long.parseLong(properti.getLuasTanah());
                            luasBangunan5 = Long.parseLong(properti.getLuasBangunan());
                            usiaBangunan5 = Long.parseLong(properti.getUsiaBangunan());
                            hargaRataBangunanPerMeter5 = Long.parseLong(properti.getHargaRataPerMeter());
                        }

                        etHargaJualProperty.setText(String.valueOf(hargaJualProperty));
                        etLuasTanah.setText(""+luasTanah);
                        etLuasBangunan.setText(""+luasBangunan);
                        etUsiaBangunan.setText(""+usiaBangunan);
                        etHargaRataBangunanPerMeter.setText(""+hargaRataBangunanPerMeter);

                        etHargaJualPropertySatu.setText(""+hargaJualProperty1);
                        etLuasTanahSatu.setText(""+luasTanah1);
                        etLuasBangunanSatu.setText(""+luasBangunan1);
                        etUsiaBangunanSatu.setText(""+usiaBangunan1);
                        etHargaRataBangunanPerMeterSatu.setText(""+hargaRataBangunanPerMeter1);

                        etHargaJualPropertyDua.setText(""+hargaJualProperty2);
                        etLuasTanahDua.setText(""+luasTanah2);
                        etLuasBangunanDua.setText(""+luasBangunan2);
                        etUsiaBangunanDua.setText(""+usiaBangunan2);
                        etHargaRataBangunanPerMeterDua.setText(""+hargaRataBangunanPerMeter2);

                        etHargaJualPropertyTiga.setText(""+hargaJualProperty3);
                        etLuasTanahTiga.setText(""+luasTanah3);
                        etLuasBangunanTiga.setText(""+luasBangunan3);
                        etUsiaBangunanTiga.setText(""+usiaBangunan3);
                        etHargaRataBangunanPerMeterTiga.setText(""+hargaRataBangunanPerMeter3);

                        etHargaJualPropertyEmpat.setText(""+hargaJualProperty4);
                        etLuasTanahEmpat.setText(""+luasTanah4);
                        etLuasBangunanEmpat.setText(""+luasBangunan4);
                        etUsiaBangunanEmpat.setText(""+usiaBangunan4);
                        etHargaRataBangunanPerMeterEmpat.setText(""+hargaRataBangunanPerMeter4);

                        etHargaJualPropertyLima.setText(""+hargaJualProperty5);
                        etLuasTanahLima.setText(""+luasTanah5);
                        etLuasBangunanLima.setText(""+luasBangunan5);
                        etUsiaBangunanLima.setText(""+usiaBangunan5);
                        etHargaRataBangunanPerMeterLima.setText(""+hargaRataBangunanPerMeter5);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePropertiNilaiPasar> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.koneksi_internet_bermasalah), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setCurrencyEditText(){
        new CurrencyEditText(etHargaJualProperty);
        new CurrencyEditText(etHargaJualPropertySatu);
        new CurrencyEditText(etHargaJualPropertyDua);
        new CurrencyEditText(etHargaJualPropertyTiga);
        new CurrencyEditText(etHargaJualPropertyEmpat);
        new CurrencyEditText(etHargaJualPropertyLima);
        new CurrencyEditText(etHargaRataBangunanPerMeter);
        new CurrencyEditText(etHargaRataBangunanPerMeterSatu);
        new CurrencyEditText(etHargaRataBangunanPerMeterDua);
        new CurrencyEditText(etHargaRataBangunanPerMeterTiga);
        new CurrencyEditText(etHargaRataBangunanPerMeterEmpat);
        new CurrencyEditText(etHargaRataBangunanPerMeterLima);
    }

    public void tabsHandler(int i){
        if(i==R.id.rb_properti_incaran){
            properti = 0;

            txtNamaProperty.setText(getString(R.string.properti_incaran));
            layoutPropertyIncaran.setVisibility(View.VISIBLE);
            layoutPropertySatu.setVisibility(View.INVISIBLE);
            layoutPropertyDua.setVisibility(View.INVISIBLE);
            layoutPropertyTiga.setVisibility(View.INVISIBLE);
            layoutPropertyEmpat.setVisibility(View.INVISIBLE);
            layoutPropertyLima.setVisibility(View.INVISIBLE);
            btnBack.setVisibility(View.INVISIBLE);
            btnReview.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            layoutCatatan.setVisibility(View.VISIBLE);
            etHargaJualProperty.setFocusable(true);
        }
        else if(i==R.id.rb_properti_satu){
            properti = 1;

            txtNamaProperty.setText(getString(R.string.properti_satu));
            layoutPropertyIncaran.setVisibility(View.INVISIBLE);
            layoutPropertySatu.setVisibility(View.VISIBLE);
            layoutPropertyDua.setVisibility(View.INVISIBLE);
            layoutPropertyTiga.setVisibility(View.INVISIBLE);
            layoutPropertyEmpat.setVisibility(View.INVISIBLE);
            layoutPropertyLima.setVisibility(View.INVISIBLE);
            btnBack.setVisibility(View.VISIBLE);
            btnReview.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            layoutCatatan.setVisibility(View.GONE);
            etHargaJualPropertySatu.setFocusable(true);
        }
        else if(i==R.id.rb_properti_dua){
            properti = 2;

            txtNamaProperty.setText(getString(R.string.properti_dua));
            layoutPropertyIncaran.setVisibility(View.INVISIBLE);
            layoutPropertySatu.setVisibility(View.INVISIBLE);
            layoutPropertyDua.setVisibility(View.VISIBLE);
            layoutPropertyTiga.setVisibility(View.INVISIBLE);
            layoutPropertyEmpat.setVisibility(View.INVISIBLE);
            layoutPropertyLima.setVisibility(View.INVISIBLE);
            btnBack.setVisibility(View.VISIBLE);
            btnReview.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            layoutCatatan.setVisibility(View.GONE);
            etHargaJualPropertyDua.setFocusable(true);
        }
        else if(i==R.id.rb_properti_tiga){
            properti = 3;

            txtNamaProperty.setText(getString(R.string.properti_tiga));
            layoutPropertyIncaran.setVisibility(View.INVISIBLE);
            layoutPropertySatu.setVisibility(View.INVISIBLE);
            layoutPropertyDua.setVisibility(View.INVISIBLE);
            layoutPropertyTiga.setVisibility(View.VISIBLE);
            layoutPropertyEmpat.setVisibility(View.INVISIBLE);
            layoutPropertyLima.setVisibility(View.INVISIBLE);
            btnBack.setVisibility(View.VISIBLE);
            btnReview.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            layoutCatatan.setVisibility(View.GONE);
            etHargaJualPropertyTiga.setFocusable(true);
        }
        else if(i==R.id.rb_properti_empat){
            properti = 4;

            txtNamaProperty.setText(getString(R.string.properti_empat));
            layoutPropertyIncaran.setVisibility(View.INVISIBLE);
            layoutPropertySatu.setVisibility(View.INVISIBLE);
            layoutPropertyDua.setVisibility(View.INVISIBLE);
            layoutPropertyTiga.setVisibility(View.INVISIBLE);
            layoutPropertyEmpat.setVisibility(View.VISIBLE);
            layoutPropertyLima.setVisibility(View.INVISIBLE);
            btnBack.setVisibility(View.VISIBLE);
            btnReview.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            layoutCatatan.setVisibility(View.GONE);
            etHargaJualPropertyEmpat.setFocusable(true);
        }
        else if(i==R.id.rb_properti_lima) {
            properti = 5;

            txtNamaProperty.setText(getString(R.string.properti_lima));
            layoutPropertyIncaran.setVisibility(View.INVISIBLE);
            layoutPropertySatu.setVisibility(View.INVISIBLE);
            layoutPropertyDua.setVisibility(View.INVISIBLE);
            layoutPropertyTiga.setVisibility(View.INVISIBLE);
            layoutPropertyEmpat.setVisibility(View.INVISIBLE);
            layoutPropertyLima.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.INVISIBLE);
            btnBack.setVisibility(View.VISIBLE);
            btnReview.setVisibility(View.VISIBLE);
            layoutCatatan.setVisibility(View.GONE);
            etHargaJualPropertyLima.setFocusable(true);
        }
    }

    public void checkTab(int i){
        if(i==0){
            next = R.id.rb_properti_satu;
        }
        else if(i==1){
            back = R.id.rb_properti_incaran;
            next = R.id.rb_properti_dua;
        }
        else if(i==2){
            back = R.id.rb_properti_satu;
            next = R.id.rb_properti_tiga;
        }
        else if(i==3){
            back = R.id.rb_properti_dua;
            next = R.id.rb_properti_empat;
        }
        else if(i==4){
            back = R.id.rb_properti_tiga;
            next = R.id.rb_properti_lima;
        }
        else if(i==5){
            back = R.id.rb_properti_empat;
        }
    }

    @OnClick(R.id.btn_next)
    public void nextProperti(){
        checkTab(properti);
        tabsHandler(next);
        tabs.check(next);
    }

    @OnClick(R.id.btn_back)
    public void backProperti(){
        checkTab(properti);
        tabs.check(back);
        tabsHandler(back);
    }

    public void checkValue(){
        if(etHargaJualPropertyDua.getText().toString().equalsIgnoreCase("0") || TextUtils.isEmpty(etHargaJualPropertyDua.getText().toString()) || TextUtils.isEmpty(etLuasTanahDua.getText().toString()) || TextUtils.isEmpty(etLuasBangunanDua.getText().toString()) || TextUtils.isEmpty(etUsiaBangunanDua.getText().toString()) || TextUtils.isEmpty(etHargaRataBangunanPerMeterDua.getText().toString())){
            listAngkaNilaiPasar.set(2, new PropertiNilaiPasar(idNilaiPasar,"3",""+0,""+0,""+0,""+0,""+0,""+0,""+0, ""+0));
            count--;
        }
        else{
            String sHargaJualPropertiy2 = (etHargaJualPropertyDua.getText().toString()).replace(".", "");
            hargaJualProperty2 = Long.parseLong(sHargaJualPropertiy2);
            luasTanah2 = Long.parseLong(etLuasTanahDua.getText().toString());
            luasBangunan2 = Long.parseLong(etLuasBangunanDua.getText().toString());
            usiaBangunan2 = Long.parseLong(etUsiaBangunanDua.getText().toString());
            String sHargaRataBangunanPerMeter2 = (etHargaRataBangunanPerMeterDua.getText().toString()).replace(".", "");
            hargaRataBangunanPerMeter2 = Long.parseLong(sHargaRataBangunanPerMeter2);
            hargaBangunanBaru2 = luasBangunan2 * hargaRataBangunanPerMeter2;
            dHargaBangunanSaatIni2 = hargaBangunanBaru2 - (0.025 * usiaBangunan2 * hargaBangunanBaru2);
            hargaBangunanSaatIni2 = (long) dHargaBangunanSaatIni2;
            if(luasTanah2==0){
                hargaTanahPerMeter2 = 0;
            }
            else{
                hargaTanahPerMeter2 = (hargaJualProperty2 - hargaBangunanSaatIni2) / luasTanah2;
            }
            count++;
            listAngkaNilaiPasar.set(2, new PropertiNilaiPasar(idNilaiPasar,"3", ""+hargaJualProperty2, ""+luasTanah2, ""+luasBangunan2, ""+usiaBangunan2, ""+hargaRataBangunanPerMeter2, ""+hargaBangunanBaru2, ""+hargaBangunanSaatIni2, ""+hargaTanahPerMeter2));
        }
        if(etHargaJualPropertyTiga.getText().toString().equalsIgnoreCase("0") || TextUtils.isEmpty(etHargaJualPropertyTiga.getText().toString()) || TextUtils.isEmpty(etLuasTanahTiga.getText().toString()) || TextUtils.isEmpty(etLuasBangunanTiga.getText().toString()) || TextUtils.isEmpty(etUsiaBangunanTiga.getText().toString()) || TextUtils.isEmpty(etHargaRataBangunanPerMeterTiga.getText().toString())){
            listAngkaNilaiPasar.set(3, new PropertiNilaiPasar(idNilaiPasar,"4",""+0,""+0,""+0,""+0,""+0,""+0,""+0, ""+0));
            count--;
        }
        else{
            String sHargaJualPropertiy3 = (etHargaJualPropertyTiga.getText().toString()).replace(".", "");
            hargaJualProperty3 = Long.parseLong(sHargaJualPropertiy3);
            luasTanah3 = Long.parseLong(etLuasTanahTiga.getText().toString());
            luasBangunan3 = Long.parseLong(etLuasBangunanTiga.getText().toString());
            usiaBangunan3 = Long.parseLong(etUsiaBangunanTiga.getText().toString());
            String sHargaRataBangunanPerMeter3 = (etHargaRataBangunanPerMeterTiga.getText().toString()).replace(".", "");
            hargaRataBangunanPerMeter3 = Long.parseLong(sHargaRataBangunanPerMeter3);
            hargaBangunanBaru3 = luasBangunan3 * hargaRataBangunanPerMeter3;
            dHargaBangunanSaatIni3 = hargaBangunanBaru3 - (0.025 * usiaBangunan3 * hargaBangunanBaru3);
            hargaBangunanSaatIni3 = (long) dHargaBangunanSaatIni3;
            if(luasTanah3==0){
                hargaTanahPerMeter3 = 0;
            }
            else{
                hargaTanahPerMeter3 = (hargaJualProperty3 - hargaBangunanSaatIni3) / luasTanah3;
            }
            count++;
            listAngkaNilaiPasar.set(3, new PropertiNilaiPasar(idNilaiPasar,"4", ""+hargaJualProperty3, ""+luasTanah3, ""+luasBangunan3, ""+usiaBangunan3, ""+hargaRataBangunanPerMeter3, ""+hargaBangunanBaru3, ""+hargaBangunanSaatIni3, ""+hargaTanahPerMeter3));
        }
        if(etHargaJualPropertyEmpat.getText().toString().equalsIgnoreCase("0") || TextUtils.isEmpty(etHargaJualPropertyEmpat.getText().toString()) || TextUtils.isEmpty(etLuasTanahEmpat.getText().toString()) || TextUtils.isEmpty(etLuasBangunanEmpat.getText().toString()) || TextUtils.isEmpty(etUsiaBangunanEmpat.getText().toString()) || TextUtils.isEmpty(etHargaRataBangunanPerMeterEmpat.getText().toString())){
            listAngkaNilaiPasar.set(4, new PropertiNilaiPasar(idNilaiPasar,"5",""+0,""+0,""+0,""+0,""+0,""+0,""+0, ""+0));
            count--;
        }
        else{
            String sHargaJualPropertiy4 = (etHargaJualPropertyEmpat.getText().toString()).replace(".", "");
            hargaJualProperty4 = Long.parseLong(sHargaJualPropertiy4);
            luasTanah4 = Long.parseLong(etLuasTanahEmpat.getText().toString());
            luasBangunan4 = Long.parseLong(etLuasBangunanEmpat.getText().toString());
            usiaBangunan4 = Long.parseLong(etUsiaBangunanEmpat.getText().toString());
            String sHargaRataBangunanPerMeter4 = (etHargaRataBangunanPerMeterEmpat.getText().toString()).replace(".", "");
            hargaRataBangunanPerMeter4 = Long.parseLong(sHargaRataBangunanPerMeter4);
            hargaBangunanBaru4 = luasBangunan4 * hargaRataBangunanPerMeter4;
            dHargaBangunanSaatIni4 = hargaBangunanBaru4 - (0.025 * usiaBangunan4 * hargaBangunanBaru4);
            hargaBangunanSaatIni4 = (long) dHargaBangunanSaatIni4;
            if(luasTanah4==0){
                hargaTanahPerMeter4 = 0;
            }
            else{
                hargaTanahPerMeter4 = (hargaJualProperty4 - hargaBangunanSaatIni4) / luasTanah4;
            }
            count++;
            listAngkaNilaiPasar.set(4, new PropertiNilaiPasar(idNilaiPasar,"5", ""+hargaJualProperty4, ""+luasTanah4, ""+luasBangunan4, ""+usiaBangunan4, ""+hargaRataBangunanPerMeter4, ""+hargaBangunanBaru4, ""+hargaBangunanSaatIni4, ""+hargaTanahPerMeter4));
        }
        if(etHargaJualPropertyLima.getText().toString().equalsIgnoreCase("0") || TextUtils.isEmpty(etHargaJualPropertyLima.getText().toString()) || TextUtils.isEmpty(etLuasTanahLima.getText().toString()) || TextUtils.isEmpty(etLuasBangunanLima.getText().toString()) || TextUtils.isEmpty(etUsiaBangunanLima.getText().toString()) || TextUtils.isEmpty(etHargaRataBangunanPerMeterLima.getText().toString())){
            listAngkaNilaiPasar.set(5, new PropertiNilaiPasar(idNilaiPasar,"6",""+0,""+0,""+0,""+0,""+0,""+0,""+0, ""+0));
            count--;
        }
        else{
            String sHargaJualPropertiy5 = (etHargaJualPropertyLima.getText().toString()).replace(".", "");
            hargaJualProperty5 = Long.parseLong(sHargaJualPropertiy5);
            luasTanah5 = Long.parseLong(etLuasTanahLima.getText().toString());
            luasBangunan5 = Long.parseLong(etLuasBangunanLima.getText().toString());
            usiaBangunan5 = Long.parseLong(etUsiaBangunanLima.getText().toString());
            String sHargaRataBangunanPerMeter5 = (etHargaRataBangunanPerMeterLima.getText().toString()).replace(".", "");
            hargaRataBangunanPerMeter5 = Long.parseLong(sHargaRataBangunanPerMeter5);
            hargaBangunanBaru5 = luasBangunan5 * hargaRataBangunanPerMeter5;
            dHargaBangunanSaatIni5 = hargaBangunanBaru5 - (0.025 * usiaBangunan5 * hargaBangunanBaru5);
            hargaBangunanSaatIni5 = (long) dHargaBangunanSaatIni5;
            if(luasTanah5==0){
                hargaTanahPerMeter5 = 0;
            }
            else{
                hargaTanahPerMeter5 = (hargaJualProperty5 - hargaBangunanSaatIni5) / luasTanah5;
            }
            count++;
            listAngkaNilaiPasar.set(5, new PropertiNilaiPasar(""+idNilaiPasar,"6", ""+hargaJualProperty5, ""+luasTanah5, ""+luasBangunan5, ""+usiaBangunan5, ""+hargaRataBangunanPerMeter5, ""+hargaBangunanBaru5, ""+hargaBangunanSaatIni5, ""+hargaTanahPerMeter5));
        }
    }

    @OnClick(R.id.btn_review)
    public void reviewProperty(){
        listAngkaNilaiPasar.clear();
        for(int i=0;i<=5;i++){
            listAngkaNilaiPasar.add(new PropertiNilaiPasar(idNilaiPasar,""+(i+1),"0","0","0","0","0","0", "", ""));
        }

        if(TextUtils.isEmpty(etHargaJualProperty.getText().toString()) || TextUtils.isEmpty(etLuasTanah.getText().toString()) || TextUtils.isEmpty(etLuasBangunan.getText().toString()) || TextUtils.isEmpty(etUsiaBangunan.getText().toString()) || TextUtils.isEmpty(etHargaRataBangunanPerMeter.getText().toString())){
            Toast.makeText(this, getString(R.string.data_properti_incaran_belum_lengkap), Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(etHargaJualPropertySatu.getText().toString()) || TextUtils.isEmpty(etLuasTanahSatu.getText().toString()) || TextUtils.isEmpty(etLuasBangunanSatu.getText().toString()) || TextUtils.isEmpty(etUsiaBangunanSatu.getText().toString()) || TextUtils.isEmpty(etHargaRataBangunanPerMeterSatu.getText().toString())){
            Toast.makeText(this, getString(R.string.properti_satu_wajib_diisi), Toast.LENGTH_SHORT).show();
        }
        else{
            if(TextUtils.isEmpty(etCatatanKondisiBangunan.getText().toString()) || TextUtils.isEmpty(etCatatanSurveyLokasi.getText().toString())){
                catatanKondisi = "";
                catatanSurvey = "";
            }

            count = 6;

            //properti incaran
            String sHargaJualProperti = (etHargaJualProperty.getText().toString()).replace(".", "");
            hargaJualProperty = Long.parseLong(sHargaJualProperti);
            luasTanah = Long.parseLong(etLuasTanah.getText().toString());
            luasBangunan = Long.parseLong(etLuasBangunan.getText().toString());
            usiaBangunan = Long.parseLong(etUsiaBangunan.getText().toString());
            String sHargaRataBangunanPerMeter = (etHargaRataBangunanPerMeter.getText().toString()).replace(".", "");
            hargaRataBangunanPerMeter = Long.parseLong(sHargaRataBangunanPerMeter);
            hargaBangunanBaru = luasBangunan * hargaRataBangunanPerMeter;
            dHargaBangunanSaatIni = hargaBangunanBaru - (0.025 * usiaBangunan * hargaBangunanBaru);
            hargaBangunanSaatIni = (long) dHargaBangunanSaatIni;
            if(luasTanah==0){
                hargaTanahPerMeter = 0;
            }
            else{
                hargaTanahPerMeter = (hargaJualProperty - hargaBangunanSaatIni) / luasTanah;
            }
            listAngkaNilaiPasar.set(0, new PropertiNilaiPasar(idNilaiPasar, "1", ""+hargaJualProperty, ""+luasTanah, ""+luasBangunan, ""+usiaBangunan, ""+hargaRataBangunanPerMeter, ""+hargaBangunanBaru, ""+hargaBangunanSaatIni, ""+hargaTanahPerMeter));

            //properti satu
            String sHargaJualProperti1 = (etHargaJualPropertySatu.getText().toString()).replace(".", "");
            hargaJualProperty1 = Long.parseLong(sHargaJualProperti1);
            luasTanah1 = Long.parseLong(etLuasTanahSatu.getText().toString());
            luasBangunan1 = Long.parseLong(etLuasBangunanSatu.getText().toString());
            usiaBangunan1 = Long.parseLong(etUsiaBangunanSatu.getText().toString());
            String sHargaRataBangunanPerMeter1 = (etHargaRataBangunanPerMeterSatu.getText().toString()).replace(".", "");
            hargaRataBangunanPerMeter1 = Long.parseLong(sHargaRataBangunanPerMeter1);
            hargaBangunanBaru1 = luasBangunan1 * hargaRataBangunanPerMeter1;
            dHargaBangunanSaatIni1 = hargaBangunanBaru1 - (0.025 * usiaBangunan1 * hargaBangunanBaru1);
            hargaBangunanSaatIni1 = (long) dHargaBangunanSaatIni1;
            if(luasTanah1==0){
                hargaTanahPerMeter1 = 0;
            }
            else{
                hargaTanahPerMeter1 = (hargaJualProperty1 - hargaBangunanSaatIni1) / luasTanah1;
            }
            listAngkaNilaiPasar.set(1, new PropertiNilaiPasar(idNilaiPasar, "2", ""+hargaJualProperty1, ""+luasTanah1, ""+luasBangunan1, ""+usiaBangunan1, ""+hargaRataBangunanPerMeter1, ""+hargaBangunanBaru1, ""+hargaBangunanSaatIni1, ""+hargaTanahPerMeter1));

            checkValue();

            catatanKondisi = etCatatanKondisiBangunan.getText().toString();
            catatanSurvey = etCatatanSurveyLokasi.getText().toString();

            if(count==2){
                count = 1;
            }
            else if(count==4){
                count = 2;
            }
            else if(count==6){
                count = 3;
            }
            else if(count==8){
                count = 4;
            }
            else if(count==10){
                count = 5;
            }

            for(int i=5; i>count; i--){
                listAngkaNilaiPasar.remove(i);
            }

//            Toast.makeText(this, "count : "+count+"\n list count :"+listAngkaNilaiPasar.size(), Toast.LENGTH_SHORT).show();

            Intent i = new Intent(NilaiPasarActivity.this, ReviewNilaiPasarActivity.class);
            i.putExtra("Count", count);
            i.putExtra("catatanKondisi", catatanKondisi);
            i.putExtra("catatanSurvey", catatanSurvey);
            i.putExtra("NilaiPasar", (ArrayList<PropertiNilaiPasar>)listAngkaNilaiPasar);
            i.putExtra("idNilaiPasar", idNilaiPasar);
            i.putExtra("keterangan", keterangan);
            i.putExtra("position", position);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(NilaiPasarActivity.this)
                .setTitle(getString(R.string.yakin_ingin_keluar))
                .setPositiveButton(getString(R.string.ya), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home : {
                new AlertDialog.Builder(NilaiPasarActivity.this)
                        .setTitle(getString(R.string.yakin_ingin_keluar))
                        .setPositiveButton(getString(R.string.ya), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            }
                        })
                        .setNegativeButton(getString(R.string.tidak), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setCancelable(false)
                        .show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
