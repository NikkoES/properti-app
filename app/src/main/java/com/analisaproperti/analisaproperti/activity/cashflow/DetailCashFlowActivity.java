package com.analisaproperti.analisaproperti.activity.cashflow;

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

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.model.cashflow.Kamar;
import com.analisaproperti.analisaproperti.model.cashflow.Pemasukan;
import com.analisaproperti.analisaproperti.model.cashflow.Pengeluaran;
import com.analisaproperti.analisaproperti.model.cashflow.UpgradeFasilitas;
import com.analisaproperti.analisaproperti.utils.SharedPreferencesUtils;
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

import static android.text.TextUtils.concat;

public class DetailCashFlowActivity extends AppCompatActivity {

    @BindView(R.id.table_kamar)
    View tableKamar;
    @BindView(R.id.table_pemasukan)
    View tablePemasukan;
    @BindView(R.id.table_pengeluaran)
    View tablePengeluaran;
    @BindView(R.id.table_upgrade)
    View tableUpgrade;

    @BindView(R.id.txt_occupancy_rate)
    TextView txtOccupancy;
    @BindView(R.id.txt_net_operating_income)
    TextView txtNetOperating;
    @BindView(R.id.txt_net_operating_income_future)
    TextView txtNetOperatingFuture;

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

    JSONObject dataKamar, dataPemasukan, dataPengeluaran, dataFasilitas, dataExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        setLocale(pref.getString("language", ""));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cash_flow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.nav_cash_flow));

        ButterKnife.bind(this);

        listKamar = new ArrayList<>();
        listPemasukan = new ArrayList<>();
        listPengeluaran = new ArrayList<>();
        listFasilitas = new ArrayList<>();

        dataSharedPreferences = new SharedPreferencesUtils(this, "UserData");

        Gson gson = new Gson();

        try {
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

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        int size = 8;
        int sizeInDp = (int) (size * getResources().getDisplayMetrics().density + 0.5f);

        //table kamar
        for(int i=0; i<listKamar.size(); i++){
            TableRow rowKamar = new TableRow(this);
            TextView txtTipeKamar = new TextView(this);
            TextView txtHargaKamar = new TextView(this);
            TextView txtJumlahKamar = new TextView(this);
            TextView txtTotalPenghasilan = new TextView(this);

            txtTipeKamar.setText(listKamar.get(i).getTipeKamar());
            txtTipeKamar.setPadding(sizeInDp, sizeInDp, sizeInDp, sizeInDp);
            txtTipeKamar.setGravity(Gravity.CENTER);
            txtTipeKamar.setBackgroundResource(R.drawable.table_cell_bg);
            txtTipeKamar.setTextColor(getResources().getColor(R.color.colorAccent));

            txtHargaKamar.setText(listKamar.get(i).getHargaKamar());
            txtHargaKamar.setPadding(sizeInDp, sizeInDp, sizeInDp, sizeInDp);
            txtHargaKamar.setGravity(Gravity.CENTER);
            txtHargaKamar.setBackgroundResource(R.drawable.table_cell_bg);
            txtHargaKamar.setTextColor(getResources().getColor(R.color.colorAccent));

            txtJumlahKamar.setText(listKamar.get(i).getJumlahKamar());
            txtJumlahKamar.setPadding(sizeInDp, sizeInDp, sizeInDp, sizeInDp);
            txtJumlahKamar.setGravity(Gravity.CENTER);
            txtJumlahKamar.setBackgroundResource(R.drawable.table_cell_bg);
            txtJumlahKamar.setTextColor(getResources().getColor(R.color.colorAccent));

            Long total = Long.parseLong(listKamar.get(i).getHargaKamar()) * Integer.parseInt(listKamar.get(i).getJumlahKamar());

            txtTotalPenghasilan.setText(""+total);
            txtTotalPenghasilan.setPadding(sizeInDp, sizeInDp, sizeInDp, sizeInDp);
            txtTotalPenghasilan.setGravity(Gravity.CENTER);
            txtTotalPenghasilan.setBackgroundResource(R.drawable.table_cell_bg);
            txtTotalPenghasilan.setTextColor(getResources().getColor(R.color.colorAccent));

            ((TableRow) rowKamar).addView(txtTipeKamar);
            ((TableRow) rowKamar).addView(txtHargaKamar);
            ((TableRow) rowKamar).addView(txtJumlahKamar);
            ((TableRow) rowKamar).addView(txtTotalPenghasilan);
        }

        txtOccupancy.setText(occupationRate+"%");
        txtNetOperating.setText(concat(currencyFormatterLong(netOperatingIncome)));
        txtNetOperatingFuture.setText(concat(currencyFormatterLong(netOperatingIncomeFuture)));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
