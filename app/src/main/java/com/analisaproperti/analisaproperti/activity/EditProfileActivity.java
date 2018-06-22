package com.analisaproperti.analisaproperti.activity;

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
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.response.ResponsePost;
import com.analisaproperti.analisaproperti.model.response.ResponseUser;
import com.analisaproperti.analisaproperti.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.et_no_hp)
    EditText etNoHp;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password_lama)
    EditText etPasswordLama;
    @BindView(R.id.et_password_baru)
    EditText etPasswordBaru;
    @BindView(R.id.et_password_baru_2)
    EditText etPasswordBaruDua;

    private SharedPreferencesUtils userDataPref;

    JSONObject userProfile;

    String userData;

    String id, nama, noHp, email, password, img;

    BaseApiService apiService;

    private ProgressDialog loadingDaftar;

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
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.nav_edit_profil));

        ButterKnife.bind(this);

        apiService = UtilsApi.getAPIService();

        userDataPref = new SharedPreferencesUtils(getApplicationContext(), "UserData");

        try {
            userData = userDataPref.getPreferenceData("userProfile");

            userProfile = new JSONObject(userData);
            id = userProfile.get("id_user").toString();
            nama = userProfile.get("name").toString();
            noHp = userProfile.get("phonenumber").toString();
            email = userProfile.get("email").toString();
            password = userProfile.get("password").toString();
            img = userProfile.get("profile_photo").toString();

            etId.setText(id);
            etNama.setText(nama);
            etNoHp.setText(noHp);
            etEmail.setText(email);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        loadingDaftar = new ProgressDialog(this);
        loadingDaftar.setTitle("Loading");
        loadingDaftar.setMessage("Saving data...");
        loadingDaftar.setCancelable(false);
    }

    private void editProfile(String nama, String noHp, final String email, String password, String image){
        apiService.editProfile(id ,nama, noHp, email, password, image)
                .enqueue(new Callback<ResponsePost>() {
                    @Override
                    public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                        if (response.isSuccessful()){
                            loadingDaftar.dismiss();
                            getUserData(email);
                            Toast.makeText(EditProfileActivity.this, getString(R.string.berhasil_disimpan), Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        }
                        else {
                            loadingDaftar.dismiss();
                            Toast.makeText(EditProfileActivity.this, getString(R.string.gagal_disimpan), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePost> call, Throwable t) {
                        loadingDaftar.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.koneksi_internet_bermasalah), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.btn_simpan)
    public void btnSimpan(){
        new AlertDialog.Builder(EditProfileActivity.this)
                .setTitle("Apakah anda yakin ingin mengubah ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadingDaftar.show();
                        if(TextUtils.isEmpty(etNama.getText().toString()) || TextUtils.isEmpty(etNoHp.getText().toString()) || TextUtils.isEmpty(etEmail.getText().toString())){
                            Toast.makeText(getApplicationContext(), getString(R.string.data_belum_lengkap), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            nama = etNama.getText().toString();
                            noHp = etNoHp.getText().toString();
                            email = etEmail.getText().toString();
                            if(TextUtils.isEmpty(etPasswordLama.getText().toString()) && TextUtils.isEmpty(etPasswordBaru.getText().toString()) && TextUtils.isEmpty(etPasswordBaruDua.getText().toString())){
                                loadingDaftar.dismiss();
                                editProfile(nama, noHp, email, password, "");
                            }
                            else if(TextUtils.isEmpty(etPasswordLama.getText().toString()) || TextUtils.isEmpty(etPasswordBaru.getText().toString()) || TextUtils.isEmpty(etPasswordBaruDua.getText().toString())){
                                loadingDaftar.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.data_password_belum_lengkap), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(password.equalsIgnoreCase(etPasswordLama.getText().toString())){
                                    if(etPasswordBaru.getText().toString().equalsIgnoreCase(etPasswordBaruDua.getText().toString())){
                                        editProfile(nama, noHp, email, etPasswordBaru.getText().toString(), "");
                                    }
                                    else{
                                        loadingDaftar.dismiss();
                                        Toast.makeText(getApplicationContext(), getString(R.string.password_baru_tidak_match), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    loadingDaftar.dismiss();
                                    Toast.makeText(getApplicationContext(), getString(R.string.password_lama_salah), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .show();
    }

    private void getUserData(String email){
        userDataPref.clearAllData();
        apiService.getUserData(email).enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if (response.body().getStatus().equals("success")){
                    loadingDaftar.dismiss();
                    ResponseUser.Data data = response.body().getData();

                    userProfile = new JSONObject();
                    try {
                        userProfile.put("id_user", data.getIdUser());
                        userProfile.put("name", data.getNamaUser());
                        userProfile.put("email", data.getEmail());
                        userProfile.put("password", data.getPassword());
                        userProfile.put("phonenumber", data.getNoHp());
                        userProfile.put("profile_photo", ""+data.getImgProfile());

                        userDataPref.storeData("userProfile", userProfile.toString());

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    loadingDaftar.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.koneksi_internet_bermasalah), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(EditProfileActivity.this)
                .setTitle(getString(R.string.yakin_ingin_keluar))
                .setPositiveButton(getString(R.string.ya), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                })
                .setNegativeButton(getString(R.string.batal), new DialogInterface.OnClickListener() {
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
                new AlertDialog.Builder(EditProfileActivity.this)
                        .setTitle(getString(R.string.yakin_ingin_keluar))
                        .setPositiveButton(getString(R.string.ya), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            }
                        })
                        .setNegativeButton(getString(R.string.batal), new DialogInterface.OnClickListener() {
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
