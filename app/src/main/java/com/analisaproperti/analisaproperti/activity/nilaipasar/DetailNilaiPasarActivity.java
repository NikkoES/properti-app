package com.analisaproperti.analisaproperti.activity.nilaipasar;

import android.app.Activity;
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
import android.widget.Toast;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.nilaipasar.PropertiNilaiPasar;
import com.analisaproperti.analisaproperti.model.response.ResponsePropertiNilaiPasar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.concat;

public class DetailNilaiPasarActivity extends AppCompatActivity {

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

    @BindView(R.id.txt_harga_pasaran_per_meter)
    TextView txtHargaPasaran;
    @BindView(R.id.txt_perbandingan_properti)
    TextView txtPerbandinganProperti;
    @BindView(R.id.txt_catatan_kondisi)
    TextView txtCatatanKondisi;
    @BindView(R.id.txt_catatan_survey)
    TextView txtCatatanSurvey;

    @BindView(R.id.ad_top)
    AdView topAds;
    @BindView(R.id.ad_bottom)
    AdView bottomAds;

    BaseApiService apiService;

    List<PropertiNilaiPasar> listAngkaNilaiPasar = new ArrayList<>();

    String idNilaiPasar, hargaPasaran, perbandinganProperti, catatanKondisi, catatannSurvey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        setLocale(pref.getString("language", ""));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_nilai_pasar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.detail_nilai_pasar));

        ButterKnife.bind(this);

        apiService = UtilsApi.getAPIService();

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        topAds.loadAd(adRequest);
        bottomAds.loadAd(adRequest);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//        listAngkaNilaiPasar = (ArrayList<PropertiNilaiPasar>)getIntent().getSerializableExtra("properti");
        idNilaiPasar = getIntent().getStringExtra("id_nilai_pasar");
        hargaPasaran = getIntent().getStringExtra("harga_pasaran");
        perbandinganProperti = getIntent().getStringExtra("perbandingan_properti");
        catatanKondisi = getIntent().getStringExtra("catatan_kondisi");
        catatannSurvey = getIntent().getStringExtra("catatan_survey");

        getDataProperti(idNilaiPasar);

        txtHargaPasaran.setText(concat(currencyFormatterLong(Long.parseLong(hargaPasaran))));
        txtPerbandinganProperti.setText(""+Double.parseDouble(perbandinganProperti)+"%");
        txtCatatanKondisi.setText(catatanKondisi);
        txtCatatanSurvey.setText(catatannSurvey);
    }

    public void getDataProperti(String idNilaiPasar){
        apiService.getDataNilaiPasar(idNilaiPasar).enqueue(new Callback<ResponsePropertiNilaiPasar>() {
            @Override
            public void onResponse(Call<ResponsePropertiNilaiPasar> call, Response<ResponsePropertiNilaiPasar> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equalsIgnoreCase("success")){
                        listAngkaNilaiPasar = response.body().getData();

                        int size = 8;
                        int sizeInDp = (int) (size * getResources().getDisplayMetrics().density + 0.5f);
                        //row table
                        for(int a=0; a<6; a++){
                            TextView txtHargaJualProperti = new TextView(getApplicationContext());
                            TextView txtLuasTanah = new TextView(getApplicationContext());
                            TextView txtLuasBangunan = new TextView(getApplicationContext());
                            TextView txtUsiaBangunan = new TextView(getApplicationContext());
                            TextView txtHargaRata = new TextView(getApplicationContext());

                            if(a < listAngkaNilaiPasar.size()){
                                txtHargaJualProperti.setText(""+concat(currencyFormatterLong(Long.parseLong(listAngkaNilaiPasar.get(a).getHargaJualProperti()))));
                                txtLuasTanah.setText(""+listAngkaNilaiPasar.get(a).getLuasTanah());
                                txtLuasBangunan.setText(""+listAngkaNilaiPasar.get(a).getLuasBangunan());
                                txtUsiaBangunan.setText(""+listAngkaNilaiPasar.get(a).getUsiaBangunan());
                                txtHargaRata.setText(""+concat(currencyFormatterLong(Long.parseLong(listAngkaNilaiPasar.get(a).getHargaRataPerMeter()))));
                            }
                            else{
                                txtHargaJualProperti.setText("0");
                                txtLuasTanah.setText("0");
                                txtLuasBangunan.setText("0");
                                txtUsiaBangunan.setText("0");
                                txtHargaRata.setText("0");
                            }
                            txtHargaJualProperti.setPadding(sizeInDp, sizeInDp, sizeInDp, sizeInDp);
                            txtHargaJualProperti.setGravity(Gravity.CENTER);
                            txtHargaJualProperti.setBackgroundResource(R.drawable.table_cell_bg);
                            txtHargaJualProperti.setTextColor(getResources().getColor(R.color.colorAccent));

                            txtLuasTanah.setPadding(sizeInDp, sizeInDp, sizeInDp, sizeInDp);
                            txtLuasTanah.setGravity(Gravity.CENTER);
                            txtLuasTanah.setBackgroundResource(R.drawable.table_cell_bg);
                            txtLuasTanah.setTextColor(getResources().getColor(R.color.colorAccent));

                            txtLuasBangunan.setPadding(sizeInDp, sizeInDp, sizeInDp, sizeInDp);
                            txtLuasBangunan.setGravity(Gravity.CENTER);
                            txtLuasBangunan.setBackgroundResource(R.drawable.table_cell_bg);
                            txtLuasBangunan.setTextColor(getResources().getColor(R.color.colorAccent));

                            txtUsiaBangunan.setPadding(sizeInDp, sizeInDp, sizeInDp, sizeInDp);
                            txtUsiaBangunan.setGravity(Gravity.CENTER);
                            txtUsiaBangunan.setBackgroundResource(R.drawable.table_cell_bg);
                            txtUsiaBangunan.setTextColor(getResources().getColor(R.color.colorAccent));

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
                }
            }

            @Override
            public void onFailure(Call<ResponsePropertiNilaiPasar> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.koneksi_internet_bermasalah), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    public String currencyFormatterLong(long number){
        DecimalFormat kursIndo = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndo.setDecimalFormatSymbols(formatRp);
        return kursIndo.format(number);
    }

    public String currencyFormatter(int number){
        DecimalFormat kursIndo = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
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
