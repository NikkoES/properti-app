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
            setPendahuluan();
        }
        else{
            getSupportActionBar().setTitle(getString(R.string.nav_bantuan));
            setTextView(question);
        }
    }

    private void setPendahuluan(){
        String pendahuluan = "<b>Pendahuluan :</b><br>\n" +
                "Apps ini dibuat untuk mempermudah semua kalangan membeli property dimana ada beberapa tahapan :<br>\n" +
                "1. Hitungan Cicilan ke bank<br>\n" +
                "2. Perbandingan Harga Property<br>\n" +
                "3. Hitungan Income setiap bulan<br><br>\n" +
                "<b>Hitungan Cicilan ke Bank,</b><br>\n" +
                "Kita dapat mengetaui berapa besaran Cicilan per bulan yang harus di bayarkan ke Bank dan kemampuan kita untuk cicil<br><br>\n" +
                "<b>Perbandingan harga Property,</b><br>\n" +
                "Kita pembeli property harus mengetahui target property yang mau dibeli oleh kita apakah harganya dibawah pasaran atau harganya sangat mahal, untuk itu kita perlu alat perbandingan untuk mempermudah cara beli<br><br>\n" +
                "<b>Hitungan Income setiap bulan,</b><br>\n" +
                "Hitungan ini untuk mempermudah hitungan jika kita membeli property dengan income perbulan apakah sudah bisa menutupi cicilan kebank<br><br>\n" +
                "Semoga dengan apps ini dapat membantu seseorang lebih teliti dalam membeli property.<br><br>\n" +
                "Sukses selalu, <br>\n" +
                "“see you on the top”";
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
