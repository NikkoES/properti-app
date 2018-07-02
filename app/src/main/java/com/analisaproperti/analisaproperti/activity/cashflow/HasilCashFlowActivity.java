package com.analisaproperti.analisaproperti.activity.cashflow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.activity.MainActivity;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.cashflow.Kamar;
import com.analisaproperti.analisaproperti.model.cashflow.Pemasukan;
import com.analisaproperti.analisaproperti.model.cashflow.Pengeluaran;
import com.analisaproperti.analisaproperti.model.cashflow.UpgradeFasilitas;
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

public class HasilCashFlowActivity extends AppCompatActivity {

    @BindView(R.id.txt_net_operating_income_future)
    TextView txtNetOperatingIncomeFuture;

    @BindView(R.id.ad_bottom)
    AdView bottomAds;
    @BindView(R.id.ad_top)
    AdView topAds;

    BaseApiService apiService;

    private SharedPreferencesUtils userDataSharedPreferences;

    JSONObject userProfile;

    String idUser, idCashFlow, keterangan;

    int occupancyRate;
    long penghasilanSewaKamar, totalPemasukan, totalPengeluaran, netOperatingIncome, netOperatingIncomeFuture;

    List<Kamar> listKamar = new ArrayList<>();
    List<Pemasukan> listPemasukan = new ArrayList<>();
    List<Pengeluaran> listPengeluaran = new ArrayList<>();
    List<UpgradeFasilitas> listFasilitas = new ArrayList<>();

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

        setContentView(R.layout.activity_hasil_cash_flow);

        ButterKnife.bind(this);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        topAds.loadAd(adRequest);
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

        idCashFlow = getIntent().getStringExtra("idCashFlow");
        keterangan = getIntent().getStringExtra("keterangan");
        occupancyRate = getIntent().getIntExtra("Occupancy",0);
        penghasilanSewaKamar = getIntent().getLongExtra("Penghasilan",0);
        totalPemasukan = getIntent().getLongExtra("Pemasukan",0);
        totalPengeluaran = getIntent().getLongExtra("Pengeluaran",0);
        netOperatingIncome = getIntent().getLongExtra("NetOperatingIncome",0);
        netOperatingIncomeFuture = getIntent().getLongExtra("NetOperatingIncomeFuture", 0);

        listKamar = (ArrayList<Kamar>)getIntent().getSerializableExtra("ListKamar");
        listPemasukan = (ArrayList<Pemasukan>)getIntent().getSerializableExtra("ListPemasukan");
        listPengeluaran = (ArrayList<Pengeluaran>)getIntent().getSerializableExtra("ListPengeluaran");
        listFasilitas = (ArrayList<UpgradeFasilitas>)getIntent().getSerializableExtra("ListFasilitas");

        txtNetOperatingIncomeFuture.setText(concat(currencyFormatter(netOperatingIncomeFuture)));
    }

    @OnClick(R.id.btn_revisi)
    public void revisiCashFlow(){
        finish();
    }

    @OnClick(R.id.btn_reset)
    public void resetCashFlow(){
        finish();
        Intent i = new Intent(this, CashFlowNewActivity.class);
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
        apiService.saveCashFlow(idCashFlow, keterangan, idUser).enqueue(new Callback<ResponsePost>() {
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

    private void saveKamar(String idCashFlow, String tipeKamar, String jumlahKamar, String hargaKamar){
        apiService.saveKamar(idCashFlow, tipeKamar, jumlahKamar, hargaKamar).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){

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

    private void savePemasukan(String idCashFlow, String pemasukan, String jumlahPemasukan){
        apiService.savePemasukan(idCashFlow, pemasukan, jumlahPemasukan).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){

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

    private void savePengeluaran(String idCashFlow, String pengeluaran, String jumlahPengeluaran){
        apiService.savePengeluaran(idCashFlow, pengeluaran, jumlahPengeluaran).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){

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

    private void saveFasilitas(String idCashFlow, String namaFasilitas, String kenaikanHarga, String jumlahHarga){
        apiService.saveFasilitas(idCashFlow, namaFasilitas, kenaikanHarga, jumlahHarga).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){

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

    private void saveExtras(String idCashFlow, String occupation, String penghasilan, String pemasukan, String pengeluaran, String netIncome, String netIncomeFuture){
        apiService.saveExtras(idCashFlow, occupation, penghasilan, pemasukan, pengeluaran, netIncome, netIncomeFuture).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){

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

    private void actionUpdate(){
        apiService.updateCashFlow(idCashFlow, keterangan, idUser).enqueue(new Callback<ResponsePost>() {
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

    private void updateKamar(String idCashFlow, String tipeKamar, String jumlahKamar, String hargaKamar){
        apiService.updateKamar(idCashFlow, tipeKamar, jumlahKamar, hargaKamar).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){

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

    private void updatePemasukan(String idCashFlow, String pemasukan, String jumlahPemasukan){
        apiService.updatePemasukan(idCashFlow, pemasukan, jumlahPemasukan).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){

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

    private void updatePengeluaran(String idCashFlow, String pengeluaran, String jumlahPengeluaran){
        apiService.updatePengeluaran(idCashFlow, pengeluaran, jumlahPengeluaran).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){

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

    private void updateFasilitas(String idCashFlow, String namaFasilitas, String kenaikanHarga, String jumlahHarga){
        apiService.updateFasilitas(idCashFlow, namaFasilitas, kenaikanHarga, jumlahHarga).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){

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

    private void updateExtras(String idCashFlow, String occupation, String penghasilan, String pemasukan, String pengeluaran, String netIncome, String netIncomeFuture){
        apiService.updateExtras(idCashFlow, occupation, penghasilan, pemasukan, pengeluaran, netIncome, netIncomeFuture).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                if (response.isSuccessful()){
                    if (response.body().getData().equalsIgnoreCase("1")){

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

    public void deleteData(String idCashFlow){
        apiService.deleteKamar(idCashFlow).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {

            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable t) {

            }
        });
        apiService.deletePemasukan(idCashFlow).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {

            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable t) {

            }
        });
        apiService.deletePengeluaran(idCashFlow).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {

            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable t) {

            }
        });
        apiService.deleteFasilitas(idCashFlow).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {

            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable t) {

            }
        });
        apiService.deleteExtras(idCashFlow).enqueue(new Callback<ResponsePost>() {
            @Override
            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {

            }

            @Override
            public void onFailure(Call<ResponsePost> call, Throwable t) {

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
                    for(int i=0;i<listKamar.size();i++){
                        Kamar data = listKamar.get(i);
                        saveKamar(idCashFlow, data.getTipeKamar(), data.getJumlahKamar(), data.getHargaKamar());
                    }
                    for(int i=0;i<listPemasukan.size();i++){
                        Pemasukan data = listPemasukan.get(i);
                        savePemasukan(idCashFlow, data.getKeteranganPemasukan(), data.getJumlahPemasukan());
                    }
                    for(int i=0;i<listPengeluaran.size();i++){
                        Pengeluaran data = listPengeluaran.get(i);
                        savePengeluaran(idCashFlow, data.getKeteranganPengeluaran(), data.getJumlahPengeluaran());
                    }
                    for(int i=0;i<listFasilitas.size();i++){
                        UpgradeFasilitas data = listFasilitas.get(i);
                        saveFasilitas(idCashFlow, data.getNamaFasilitas(), data.getKenaikanHarga(), data.getJumlahKamar());
                    }
                    saveExtras(idCashFlow, String.valueOf(occupancyRate), String.valueOf(penghasilanSewaKamar), String.valueOf(totalPemasukan), String.valueOf(totalPengeluaran), String.valueOf(netOperatingIncome), String.valueOf(netOperatingIncomeFuture));

                }
                else{
                    actionUpdate();
                    deleteData(idCashFlow);
                    for(int i=0;i<listKamar.size();i++){
                        Kamar data = listKamar.get(i);
                        saveKamar(idCashFlow, data.getTipeKamar(), data.getJumlahKamar(), data.getHargaKamar());
                    }
                    for(int i=0;i<listPemasukan.size();i++){
                        Pemasukan data = listPemasukan.get(i);
                        savePemasukan(idCashFlow, data.getKeteranganPemasukan(), data.getJumlahPemasukan());
                    }
                    for(int i=0;i<listPengeluaran.size();i++){
                        Pengeluaran data = listPengeluaran.get(i);
                        savePengeluaran(idCashFlow, data.getKeteranganPengeluaran(), data.getJumlahPengeluaran());
                    }
                    for(int i=0;i<listFasilitas.size();i++){
                        UpgradeFasilitas data = listFasilitas.get(i);
                        saveFasilitas(idCashFlow, data.getNamaFasilitas(), data.getKenaikanHarga(), data.getJumlahKamar());
                    }
                    saveExtras(idCashFlow, String.valueOf(occupancyRate), String.valueOf(penghasilanSewaKamar), String.valueOf(totalPemasukan), String.valueOf(totalPengeluaran), String.valueOf(netOperatingIncome), String.valueOf(netOperatingIncomeFuture));

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
