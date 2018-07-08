package com.analisaproperti.analisaproperti.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.analisaproperti.analisaproperti.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BantuanActivity extends AppCompatActivity {

    @BindView(R.id.txt_question)
    TextView txtQuestion;
    @BindView(R.id.txt_deskripsi)
    TextView txtDeskripsi;
    @BindView(R.id.txt_answer)
    TextView txtAnswer;
    @BindView(R.id.txt_pendahuluan)
    TextView txtPendahuluan;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.btn_back)
    Button btnBack;

    @BindView(R.id.layout_bantuan)
    ScrollView layoutBantuan;
    @BindView(R.id.layout_pendahuluan)
    ScrollView layoutPendahuluan;

    int question = 1;

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
        setContentView(R.layout.activity_bantuan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if(getIntent().hasExtra("kode")){
            getSupportActionBar().setTitle(getString(R.string.nav_main));
            layoutPendahuluan.setVisibility(View.VISIBLE);
            layoutBantuan.setVisibility(View.GONE);
            setPendahuluan(pref.getString("language", ""));
        }
        else{
            getSupportActionBar().setTitle(getString(R.string.nav_bantuan));
            setTextView(question);
        }
    }

    private void setPendahuluan(String language){
        String pendahuluan = "";
        if(language.equals("en")){
            pendahuluan = "<b>Preface :</b><br>\n" +
                    "This app is made to make everyone can handle their property easier in some steps :<br>\n" +
                    "1. Bank’s installment calculation<br>\n" +
                    "2. Property’s price comparison<br>\n" +
                    "3. Monthly income calculation<br><br>\n" +
                    "<b>Bank’s Instalment Calculation</b><br>\n" +
                    "We can know how many the installment calculation that we should pay to the bank every month and to measure our capability to pay.<br><br>\n" +
                    "<b>Property’s Price Comparison</b><br>\n" +
                    "We should know our property’s target price wether  it is too expensive or too cheap in the market.so that we need the comparison tool to give us the wiser insight.<br><br>\n" +
                    "<b>Monthly Income Calculation</b><br>\n" +
                    "This feature will help us to calculate our capability to pay the installment. Can our monthly income cover the installment or not? Therefore, it is important to be used.<br><br>\n" +
                    "We hope this apps will help you a lot in the property business and make everyone buys the property more carefully.<br><br>\n" +
                    "Warm regards,<br>\n" +
                    "“see you on the top”";
        }
        else{
            pendahuluan = "<b>Pendahuluan :</b><br>\n" +
                    "Apps ini dibuat untuk mempermudah semua kalangan membeli property dimana ada beberapa tahapan :<br>\n" +
                    "1. Hitungan Cicilan ke bank<br>\n" +
                    "2. Perbandingan Harga Property<br>\n" +
                    "3. Hitungan Income setiap bulan<br><br>\n" +
                    "<b>Hitungan Cicilan ke Bank</b><br>\n" +
                    "Kita dapat mengetaui berapa besaran Cicilan per bulan yang harus di bayarkan ke Bank dan kemampuan kita untuk cicil.<br><br>\n" +
                    "<b>Perbandingan harga Property</b><br>\n" +
                    "Kita pembeli property harus mengetahui target property yang mau dibeli oleh kita apakah harganya dibawah pasaran atau harganya sangat mahal, untuk itu kita perlu alat perbandingan untuk mempermudah cara beli.<br><br>\n" +
                    "<b>Hitungan Income setiap bulan</b><br>\n" +
                    "Hitungan ini untuk mempermudah hitungan jika kita membeli property dengan income perbulan apakah sudah bisa menutupi cicilan kebank.<br><br>\n" +
                    "Semoga dengan apps ini dapat membantu seseorang lebih teliti dalam membeli property.<br><br>\n" +
                    "Sukses selalu, <br>\n" +
                    "“see you on the top”";
        }
        txtPendahuluan.setText(Html.fromHtml(pendahuluan));
    }

    private void setTextView(int question) {
        switch (question){
            case 1 : {
                btnBack.setVisibility(View.INVISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                txtDeskripsi.setVisibility(View.VISIBLE);
                txtQuestion.setText(getString(R.string.question_1));
                txtDeskripsi.setText(getString(R.string.deskripsi_1));
                txtAnswer.setText(getString(R.string.answer_1));
                break;
            }
            case 2 : {
                btnBack.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                txtDeskripsi.setVisibility(View.VISIBLE);
                txtQuestion.setText(getString(R.string.question_2));
                txtDeskripsi.setText(getString(R.string.deskripsi_2));
                txtAnswer.setText(getString(R.string.answer_2));
                break;
            }
            case 3 : {
                btnBack.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                txtDeskripsi.setVisibility(View.VISIBLE);
                txtQuestion.setText(getString(R.string.question_3));
                txtDeskripsi.setText(getString(R.string.deskripsi_3));
                txtAnswer.setText(getString(R.string.answer_3));
                break;
            }
            case 4 : {
                btnBack.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.INVISIBLE);
                txtDeskripsi.setVisibility(View.GONE);
                txtQuestion.setText(getString(R.string.question_4));
                txtAnswer.setText(getString(R.string.answer_4));
                break;
            }
        }
    }

    @OnClick(R.id.btn_next)
    public void nextQuestion(){
        question++;
        setTextView(question);
    }

    @OnClick(R.id.btn_back)
    public void backQuestion(){
        question--;
        setTextView(question);
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
