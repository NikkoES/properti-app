package com.analisaproperti.analisaproperti.activity.cashflow;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.adapter.cashflow.KamarAdapter;
import com.analisaproperti.analisaproperti.adapter.cashflow.PemasukanAdapter;
import com.analisaproperti.analisaproperti.adapter.cashflow.PengeluaranAdapter;
import com.analisaproperti.analisaproperti.adapter.cashflow.UpgradeFasilitasAdapter;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.cashflow.Kamar;
import com.analisaproperti.analisaproperti.model.cashflow.Pemasukan;
import com.analisaproperti.analisaproperti.model.cashflow.Pengeluaran;
import com.analisaproperti.analisaproperti.model.cashflow.UpgradeFasilitas;
import com.analisaproperti.analisaproperti.utils.CurrencyEditText;
import com.analisaproperti.analisaproperti.utils.SharedPreferencesUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.concat;

public class CashFlowNewActivity extends AppCompatActivity {

    @BindView(R.id.rg_tabs)
    RadioGroup tabs;
    @BindView(R.id.txt_judul)
    TextView txtJudul;

    @BindView(R.id.layout_et_kamar)
    LinearLayout layoutEditKamar;
    @BindView(R.id.layout_et_pemasukan)
    LinearLayout layoutEditPemasukan;
    @BindView(R.id.layout_et_pengeluaran)
    LinearLayout layoutEditPengeluaran;
    @BindView(R.id.layout_et_fasilitas)
    LinearLayout layoutEditFasilitas;

    @BindView(R.id.layout_occupancy)
    CardView layoutOccupancy;

    @BindView(R.id.btn_tambah_kamar)
    Button btnTambahKamar;
    @BindView(R.id.btn_tambah_pemasukan)
    Button btnTambahPemasukan;
    @BindView(R.id.btn_tambah_pengeluaran)
    Button btnTambahPengeluaran;
    @BindView(R.id.btn_tambah_fasilitas)
    Button btnTambahFasilitas;

    @BindView(R.id.hasil_kamar)
    LinearLayout hasilKamar;
    @BindView(R.id.hasil_pemasukan)
    LinearLayout hasilPemasukan;
    @BindView(R.id.hasil_pengeluaran)
    LinearLayout hasilPengeluaran;
    @BindView(R.id.hasil_fasilitas)
    LinearLayout hasilFasilitas;

    //kamar
    @BindView(R.id.et_tipe_kamar)
    EditText etTipeKamar;
    @BindView(R.id.et_jumlah_kamar)
    EditText etJumlahKamar;
    @BindView(R.id.et_harga_kamar)
    EditText etHargaKamar;
    @BindView(R.id.et_occupancy_rate)
    EditText etOccupancyRate;
    @BindView(R.id.txt_total_penghasilan)
    TextView txtTotalPenghasilan;
    @BindView(R.id.list_kamar)
    ListView lvKamar;

    //pemasukan
    @BindView(R.id.et_keterangan_pemasukan)
    EditText etKeteranganPemasukan;
    @BindView(R.id.et_jumlah_pemasukan)
    EditText etJumlahPemasukan;
    @BindView(R.id.txt_penghasilan_sewa_kamar)
    TextView txtPenghasilanSewaKamar;
    @BindView(R.id.txt_total_pemasukan)
    TextView txtTotalPemasukan;
    @BindView(R.id.list_pemasukan)
    ListView lvPemasukan;

    //pengeluaran
    @BindView(R.id.et_keterangan_pengeluaran)
    EditText etKeteranganPengeluaran;
    @BindView(R.id.et_jumlah_pengeluaran)
    EditText etJumlahPengeluaran;
    @BindView(R.id.txt_total_pemasukan_hasil)
    TextView txtTotalPemasukanHasil;
    @BindView(R.id.txt_total_pengeluaran)
    TextView txtTotalPengeluaran;
    @BindView(R.id.list_pengeluaran)
    ListView lvPengeluaran;

    //fasilitas
    @BindView(R.id.et_nama_fasilitas)
    EditText etNamaFasilitas;
    @BindView(R.id.et_kenaikan_harga)
    EditText etKenaikanHarga;
    @BindView(R.id.et_jumlah_kamar_fasilitas)
    EditText etJumlahKamarFasilitas;
    @BindView(R.id.txt_net_operating_income)
    TextView txtNetOperatingIncome;
    @BindView(R.id.txt_total_upgrade)
    TextView txtTotalUpgrade;
    @BindView(R.id.list_fasilitas)
    ListView lvFasilitas;

    //button
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_hitung)
    Button btnHitung;
    @BindView(R.id.btn_finish)
    Button btnFinish;

    int tipe, next, back, total, position;

    List<Kamar> listKamar;
    List<Pemasukan> listPemasukan;
    List<Pengeluaran> listPengeluaran;
    List<UpgradeFasilitas> listFasilitas;

    int occupationRate;

    String tipeKamar;
    int jumlahKamar, hargaKamar;

    String keteranganPemasukan;
    int jumlahPemasukan;

    String keteranganPengeluaran;
    int jumlahPengeluaran;

    String namaFasilitas;
    int kenaikanHarga, jumlahKamarFasilitas;

    long penghasilanSewaKamar, totalPemasukan, totalPengeluaran, netOperatingIncome, netOperatingIncomeFuture;

    private SharedPreferencesUtils dataSharedPreferences;

    BaseApiService apiService;

    JSONObject userProfile, dataCashFlow, dataKamar, dataPemasukan, dataPengeluaran, dataFasilitas, dataExtras;

    String idCashFlow, keterangan, tanggal, idUser;

    @BindView(R.id.ad_bottom)
    AdView bottomAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        setLocale(pref.getString("language", ""));

        setContentView(R.layout.activity_cash_flow_new);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.nav_cash_flow));

        ButterKnife.bind(this);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        bottomAds.loadAd(adRequest);

        listKamar = new ArrayList<>();
        listPemasukan = new ArrayList<>();
        listPengeluaran = new ArrayList<>();
        listFasilitas = new ArrayList<>();

        dataSharedPreferences = new SharedPreferencesUtils(this, "UserData");

        apiService = UtilsApi.getAPIService();

        try {
            userProfile = new JSONObject(dataSharedPreferences.getPreferenceData("userProfile"));
            idUser = userProfile.get("id_user").toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();

        Intent intent = getIntent();
        if(intent.hasExtra("position")) {
            try {
                dataCashFlow = new JSONObject(getIntent().getStringExtra("dataCashFlow"));
                idCashFlow = dataCashFlow.get("id_cash_flow").toString();
                keterangan = dataCashFlow.get("keterangan").toString();

                dataKamar = new JSONObject(getIntent().getStringExtra("dataKamar"));
                dataPemasukan = new JSONObject(getIntent().getStringExtra("dataPemasukan"));
                dataPengeluaran = new JSONObject(getIntent().getStringExtra("dataPengeluaran"));
                dataFasilitas = new JSONObject(getIntent().getStringExtra("dataFasilitas"));
                dataExtras = new JSONObject(getIntent().getStringExtra("dataExtras"));

                JSONArray jsonKamar = dataKamar.getJSONArray("kamar");
                listKamar = gson.fromJson(jsonKamar.toString(), new TypeToken<ArrayList<Kamar>>(){}.getType());

                JSONArray jsonPemasukan = dataPemasukan.getJSONArray("pemasukan");
                listPemasukan = gson.fromJson(jsonPemasukan.toString(), new TypeToken<ArrayList<Pemasukan>>(){}.getType());

                JSONArray jsonPengeluaran = dataPengeluaran.getJSONArray("pengeluaran");
                listPengeluaran = gson.fromJson(jsonPengeluaran.toString(), new TypeToken<ArrayList<Pengeluaran>>(){}.getType());

                JSONArray jsonFasilitas = dataFasilitas.getJSONArray("fasilitas");
                listFasilitas = gson.fromJson(jsonFasilitas.toString(), new TypeToken<ArrayList<UpgradeFasilitas>>(){}.getType());

                JSONArray jsonExtras = dataExtras.getJSONArray("extras");
                JSONObject extras = jsonExtras.getJSONObject(0);
                occupationRate = Integer.parseInt(extras.get("occupancy_rate").toString());
                penghasilanSewaKamar = Long.parseLong(extras.get("total_penghasilan").toString());
                totalPemasukan = Long.parseLong(extras.get("total_pemasukan").toString());
                totalPengeluaran = Long.parseLong(extras.get("total_pengeluaran").toString());
                netOperatingIncome = Long.parseLong(extras.get("net_operating_income").toString());
                netOperatingIncomeFuture = Long.parseLong(extras.get("net_operating_income_future").toString());

                KamarAdapter adapter = new KamarAdapter(this, R.layout.list_item_kamar, listKamar);
                lvKamar.setAdapter(adapter);

                PemasukanAdapter adapter1 = new PemasukanAdapter(this, R.layout.list_item_pemasukan, listPemasukan);
                lvPemasukan.setAdapter(adapter1);

                PengeluaranAdapter adapter2 = new PengeluaranAdapter(this, R.layout.list_item_pengeluaran, listPengeluaran);
                lvPengeluaran.setAdapter(adapter2);

                UpgradeFasilitasAdapter adapter3 = new UpgradeFasilitasAdapter(this, R.layout.list_item_upgrade, listFasilitas);
                lvFasilitas.setAdapter(adapter3);

                etOccupancyRate.setText(""+occupationRate);
                txtPenghasilanSewaKamar.setText(concat(currencyFormatter(penghasilanSewaKamar)));
                txtTotalPenghasilan.setText(concat(currencyFormatter(penghasilanSewaKamar)));
                txtTotalPemasukan.setText(concat(currencyFormatter(totalPemasukan)));
                txtTotalPemasukanHasil.setText(concat(currencyFormatter(totalPemasukan)));
                txtTotalPengeluaran.setText(concat(currencyFormatter(totalPengeluaran)));
                txtNetOperatingIncome.setText(concat(currencyFormatter(netOperatingIncome)));
                txtTotalUpgrade.setText(concat(currencyFormatter(netOperatingIncomeFuture-netOperatingIncome)));

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            idCashFlow = getIntent().getStringExtra("id_cash_flow");
            keterangan = getIntent().getStringExtra("keterangan");
            position = 999;
        }

        txtJudul.setText("Data "+getString(R.string.kamar));
        btnBack.setVisibility(View.INVISIBLE);
        btnFinish.setVisibility(View.GONE);

        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup,  @IdRes int i) {
                tabsHandler(i);
            }
        });

        setCurrencyEditText();
    }

    public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    public void setCurrencyEditText(){
        new CurrencyEditText(etHargaKamar);
        new CurrencyEditText(etJumlahPemasukan);
        new CurrencyEditText(etJumlahPengeluaran);
        new CurrencyEditText(etKenaikanHarga);
    }

    public void checkTab(int i){
        if(i==0){
            next = R.id.rb_pemasukan;
        }
        else if(i==1){
            back = R.id.rb_kamar;
            next = R.id.rb_pengeluaran;
        }
        else if(i==2){
            back = R.id.rb_pemasukan;
            next = R.id.rb_fasilitas;
        }
        else if(i==3){
            back = R.id.rb_pengeluaran;
        }
    }

    public void tabsHandler(int i){
        if(i==R.id.rb_kamar){
            tipe = 0;

            txtJudul.setText("Data "+getString(R.string.kamar));
            lvKamar.setVisibility(View.VISIBLE);
            lvPemasukan.setVisibility(View.GONE);
            lvPengeluaran.setVisibility(View.GONE);
            lvFasilitas.setVisibility(View.GONE);
            layoutEditKamar.setVisibility(View.VISIBLE);
            layoutEditPemasukan.setVisibility(View.GONE);
            layoutEditPengeluaran.setVisibility(View.GONE);
            layoutEditFasilitas.setVisibility(View.GONE);
            btnTambahKamar.setVisibility(View.VISIBLE);
            btnTambahPemasukan.setVisibility(View.GONE);
            btnTambahPengeluaran.setVisibility(View.GONE);
            btnTambahFasilitas.setVisibility(View.GONE);
            layoutOccupancy.setVisibility(View.VISIBLE);
            hasilKamar.setVisibility(View.VISIBLE);
            hasilPemasukan.setVisibility(View.GONE);
            hasilPengeluaran.setVisibility(View.GONE);
            hasilFasilitas.setVisibility(View.GONE);
            btnBack.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
            btnHitung.setVisibility(View.VISIBLE);
            etTipeKamar.requestFocus();
        }
        else if(i==R.id.rb_pemasukan){
            tipe = 1;

            txtJudul.setText("Data "+getString(R.string.pemasukan));
            lvKamar.setVisibility(View.GONE);
            lvPemasukan.setVisibility(View.VISIBLE);
            lvPengeluaran.setVisibility(View.GONE);
            lvFasilitas.setVisibility(View.GONE);
            layoutEditKamar.setVisibility(View.GONE);
            layoutEditPemasukan.setVisibility(View.VISIBLE);
            layoutEditPengeluaran.setVisibility(View.GONE);
            layoutEditFasilitas.setVisibility(View.GONE);
            btnTambahKamar.setVisibility(View.GONE);
            btnTambahPemasukan.setVisibility(View.VISIBLE);
            btnTambahPengeluaran.setVisibility(View.GONE);
            btnTambahFasilitas.setVisibility(View.GONE);
            layoutOccupancy.setVisibility(View.GONE);
            hasilKamar.setVisibility(View.GONE);
            hasilPemasukan.setVisibility(View.VISIBLE);
            hasilPengeluaran.setVisibility(View.GONE);
            hasilFasilitas.setVisibility(View.GONE);
            btnBack.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
            btnHitung.setVisibility(View.VISIBLE);
            etKeteranganPemasukan.requestFocus();
        }
        else if(i==R.id.rb_pengeluaran){
            tipe = 2;

            txtJudul.setText("Data "+getString(R.string.pengeluaran));
            lvKamar.setVisibility(View.GONE);
            lvPemasukan.setVisibility(View.GONE);
            lvPengeluaran.setVisibility(View.VISIBLE);
            lvFasilitas.setVisibility(View.GONE);
            layoutEditKamar.setVisibility(View.GONE);
            layoutEditPemasukan.setVisibility(View.GONE);
            layoutEditPengeluaran.setVisibility(View.VISIBLE);
            layoutEditFasilitas.setVisibility(View.GONE);
            btnTambahKamar.setVisibility(View.GONE);
            btnTambahPemasukan.setVisibility(View.GONE);
            btnTambahPengeluaran.setVisibility(View.VISIBLE);
            btnTambahFasilitas.setVisibility(View.GONE);
            layoutOccupancy.setVisibility(View.GONE);
            hasilKamar.setVisibility(View.GONE);
            hasilPemasukan.setVisibility(View.GONE);
            hasilPengeluaran.setVisibility(View.VISIBLE);
            hasilFasilitas.setVisibility(View.GONE);
            btnBack.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
            btnHitung.setVisibility(View.VISIBLE);
            etKeteranganPengeluaran.requestFocus();
        }
        else if(i==R.id.rb_fasilitas) {
            tipe = 3;

            txtJudul.setText("Data "+getString(R.string.upgrade_fasilitas));
            lvKamar.setVisibility(View.GONE);
            lvPemasukan.setVisibility(View.GONE);
            lvPengeluaran.setVisibility(View.GONE);
            lvFasilitas.setVisibility(View.VISIBLE);
            layoutEditKamar.setVisibility(View.GONE);
            layoutEditPemasukan.setVisibility(View.GONE);
            layoutEditPengeluaran.setVisibility(View.GONE);
            layoutEditFasilitas.setVisibility(View.VISIBLE);
            btnTambahKamar.setVisibility(View.GONE);
            btnTambahPemasukan.setVisibility(View.GONE);
            btnTambahPengeluaran.setVisibility(View.GONE);
            btnTambahFasilitas.setVisibility(View.VISIBLE);
            layoutOccupancy.setVisibility(View.GONE);
            hasilKamar.setVisibility(View.GONE);
            hasilPemasukan.setVisibility(View.GONE);
            hasilPengeluaran.setVisibility(View.GONE);
            hasilFasilitas.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.GONE);
            btnFinish.setVisibility(View.VISIBLE);
            btnHitung.setVisibility(View.VISIBLE);
            etNamaFasilitas.requestFocus();
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

    @OnClick(R.id.btn_tambah_kamar)
    public void tambahKamar(){
        if(TextUtils.isEmpty(etTipeKamar.getText().toString()) || TextUtils.isEmpty(etJumlahKamar.getText().toString()) || TextUtils.isEmpty(etHargaKamar.getText().toString())){
            Toast.makeText(this, getString(R.string.data_belum_lengkap), Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            tipeKamar = etTipeKamar.getText().toString();
            jumlahKamar = Integer.parseInt(etJumlahKamar.getText().toString());
            String sHargaKamar = (etHargaKamar.getText().toString()).replace(".", "");
            hargaKamar = Integer.parseInt(sHargaKamar);
        }

        listKamar.add(new Kamar(""+idCashFlow, ""+tipeKamar, ""+jumlahKamar, ""+hargaKamar));

        KamarAdapter adapter = new KamarAdapter(this, R.layout.list_item_kamar, listKamar);
        lvKamar.setAdapter(adapter);

        etTipeKamar.setText(null);
        etJumlahKamar.setText(null);
        etHargaKamar.setText(null);
        etTipeKamar.requestFocus();
    }

    @OnClick(R.id.btn_tambah_pemasukan)
    public void tambahPemasukan(){
        if(TextUtils.isEmpty(etKeteranganPemasukan.getText().toString()) || TextUtils.isEmpty(etJumlahPemasukan.getText().toString())){
            Toast.makeText(this, getString(R.string.data_belum_lengkap), Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            keteranganPemasukan = etKeteranganPemasukan.getText().toString();
            String sJumlahPemasukan = (etJumlahPemasukan.getText().toString()).replace(".", "");
            jumlahPemasukan = Integer.parseInt(sJumlahPemasukan);
        }

        listPemasukan.add(new Pemasukan(""+idCashFlow, ""+keteranganPemasukan, ""+jumlahPemasukan));

        PemasukanAdapter adapter = new PemasukanAdapter(this, R.layout.list_item_pemasukan, listPemasukan);
        lvPemasukan.setAdapter(adapter);

        etKeteranganPemasukan.setText(null);
        etJumlahPemasukan.setText(null);
        etKeteranganPemasukan.requestFocus();
    }

    @OnClick(R.id.btn_tambah_pengeluaran)
    public void tambahPengeluaran(){
        if(TextUtils.isEmpty(etKeteranganPengeluaran.getText().toString()) || TextUtils.isEmpty(etJumlahPengeluaran.getText().toString())){
            Toast.makeText(this, getString(R.string.data_belum_lengkap), Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            keteranganPengeluaran = etKeteranganPengeluaran.getText().toString();
            String sJumlahPengeluaran = (etJumlahPengeluaran.getText().toString()).replace(".", "");
            jumlahPengeluaran = Integer.parseInt(sJumlahPengeluaran);
        }

        listPengeluaran.add(new Pengeluaran(""+idCashFlow, ""+keteranganPengeluaran, ""+jumlahPengeluaran));

        PengeluaranAdapter adapter = new PengeluaranAdapter(this, R.layout.list_item_pengeluaran, listPengeluaran);
        lvPengeluaran.setAdapter(adapter);

        etKeteranganPengeluaran.setText(null);
        etJumlahPengeluaran.setText(null);
        etKeteranganPengeluaran.requestFocus();
    }

    @OnClick(R.id.btn_tambah_fasilitas)
    public void tambahFasilitas(){
        if(TextUtils.isEmpty(etNamaFasilitas.getText().toString()) || TextUtils.isEmpty(etKenaikanHarga.getText().toString()) || TextUtils.isEmpty(etJumlahKamarFasilitas.getText().toString())){
            Toast.makeText(this, getString(R.string.data_belum_lengkap), Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            namaFasilitas = etNamaFasilitas.getText().toString();
            String sKenaikanHarga = (etKenaikanHarga.getText().toString()).replace(".", "");
            kenaikanHarga = Integer.parseInt(sKenaikanHarga);
            jumlahKamarFasilitas = Integer.parseInt(etJumlahKamarFasilitas.getText().toString());
        }

        listFasilitas.add(new UpgradeFasilitas(""+idCashFlow, ""+namaFasilitas, ""+kenaikanHarga, ""+jumlahKamarFasilitas));

        UpgradeFasilitasAdapter adapter = new UpgradeFasilitasAdapter(this, R.layout.list_item_upgrade, listFasilitas);
        lvFasilitas.setAdapter(adapter);

        etNamaFasilitas.setText(null);
        etKenaikanHarga.setText(null);
        etJumlahKamarFasilitas.setText(null);
        etNamaFasilitas.requestFocus();
    }

    @OnClick(R.id.btn_next)
    public void nextTipe(){
        checkTab(tipe);
        tabsHandler(next);
        tabs.check(next);
    }

    @OnClick(R.id.btn_hitung)
    public void hitungCashFlow(){
        if(tipe==0){
            if(TextUtils.isEmpty(etOccupancyRate.getText().toString())){
                Toast.makeText(this, getString(R.string.occupancy_rate_belum_diisi), Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                total = 0;
                occupationRate = Integer.parseInt(etOccupancyRate.getText().toString());
                for(int i=0; i<listKamar.size();i++){
                    Kamar hasilKamar = listKamar.get(i);
                    total = total + (Integer.parseInt(hasilKamar.getJumlahKamar()) * Integer.parseInt(hasilKamar.getHargaKamar()));
                }
                double dTotalPenghasilan = (occupationRate * 0.01) * total;
                penghasilanSewaKamar = (int) dTotalPenghasilan;
                txtTotalPenghasilan.setText(""+concat(currencyFormatter(penghasilanSewaKamar)));
                txtPenghasilanSewaKamar.setText(""+concat(currencyFormatter(penghasilanSewaKamar)));
            }
        }
        else if(tipe==1){
            total = 0;
            for(int i=0; i<listPemasukan.size();i++){
                Pemasukan hasilPemasukan = listPemasukan.get(i);
                total = total + Integer.parseInt(hasilPemasukan.getJumlahPemasukan());
            }
            totalPemasukan = total + penghasilanSewaKamar;
            txtTotalPemasukan.setText(""+concat(currencyFormatter(totalPemasukan)));
            txtTotalPemasukanHasil.setText(""+concat(currencyFormatter(totalPemasukan)));
        }
        else if(tipe==2){
            total = 0;
            for(int i=0; i<listPengeluaran.size();i++){
                Pengeluaran hasilPengeluaran = listPengeluaran.get(i);
                total = total + Integer.parseInt(hasilPengeluaran.getJumlahPengeluaran());
            }
            netOperatingIncome = totalPemasukan - total;
            txtTotalPengeluaran.setText(""+concat(currencyFormatter(total)));
            txtNetOperatingIncome.setText(""+concat(currencyFormatter(netOperatingIncome)));
        }
        else if(tipe==3){
            total = 0;
            for(int i=0; i<listFasilitas.size();i++){
                UpgradeFasilitas upgrade = listFasilitas.get(i);
                total = total + (Integer.parseInt(upgrade.getKenaikanHarga()) * Integer.parseInt(upgrade.getJumlahKamar()));
            }
            netOperatingIncomeFuture = netOperatingIncome + total;
            txtTotalUpgrade.setText(""+concat(currencyFormatter(total)));
        }
    }

    @OnClick(R.id.btn_back)
    public void backTipe(){
        checkTab(tipe);
        tabs.check(back);
        tabsHandler(back);
    }

    @OnClick(R.id.btn_finish)
    public void finishCashFlow(){
        if(txtTotalUpgrade.getText().toString().equalsIgnoreCase("Rp. -")){
            Toast.makeText(this, getString(R.string.data_belum_dihitung), Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            Intent i = new Intent(getApplicationContext(), HasilCashFlowActivity.class);
            i.putExtra("idCashFlow", idCashFlow);
            i.putExtra("keterangan", keterangan);
            i.putExtra("Occupancy", occupationRate);
            i.putExtra("Penghasilan", penghasilanSewaKamar);
            i.putExtra("Pemasukan", totalPemasukan);
            i.putExtra("Pengeluaran", totalPengeluaran);
            i.putExtra("NetOperatingIncome", netOperatingIncome);
            i.putExtra("NetOperatingIncomeFuture", netOperatingIncomeFuture);
            i.putExtra("position", position);
            i.putExtra("ListKamar", (ArrayList<Kamar>)listKamar);
            i.putExtra("ListPemasukan", (ArrayList<Pemasukan>)listPemasukan);
            i.putExtra("ListPengeluaran", (ArrayList<Pengeluaran>)listPengeluaran);
            i.putExtra("ListFasilitas", (ArrayList<UpgradeFasilitas>)listFasilitas);
            startActivity(i);
        }
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
