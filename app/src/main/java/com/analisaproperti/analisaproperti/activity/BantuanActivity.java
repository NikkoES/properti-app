package com.analisaproperti.analisaproperti.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.btn_back)
    Button btnBack;

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
        getSupportActionBar().setTitle("FAQ");

        ButterKnife.bind(this);

        setTextView(question);
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
