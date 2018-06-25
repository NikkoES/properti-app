package com.analisaproperti.analisaproperti.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.response.ResponsePost;
import com.analisaproperti.analisaproperti.model.response.ResponseUser;
import com.analisaproperti.analisaproperti.utils.GmailSender;

import java.security.SecureRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LupaPasswordActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText etEmail;

    private ProgressDialog loadingDaftar;

    BaseApiService apiService;

    String email;

    private SecureRandom random = new SecureRandom();

    /** different dictionaries used */
    private final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private final String NUMERIC = "0123456789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        ButterKnife.bind(this);

        apiService = UtilsApi.getAPIService();

        loadingDaftar = new ProgressDialog(this);
        loadingDaftar.setTitle("Checking Data");
        loadingDaftar.setMessage("Please wait..");
        loadingDaftar.setCancelable(false);
    }

    @OnClick(R.id.btn_kirim)
    public void kirimPassword(){
        if(TextUtils.isEmpty(etEmail.getText().toString())){
            Toast.makeText(getApplicationContext(), "Data belum lengkap !", Toast.LENGTH_SHORT).show();
        }
        else{
            new AlertDialog.Builder(LupaPasswordActivity.this)
                    .setTitle("Anda yakin ingin mengirim password ?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            email = etEmail.getText().toString();
                            checkProfile(email);
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
    }

    private void checkProfile(final String email){
        loadingDaftar.show();
        final String newPassword = generatePassword(8, ALPHA_CAPS + ALPHA + NUMERIC);
        apiService.getUserData(email)
                .enqueue(new Callback<ResponseUser>() {
                    @Override
                    public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                        if (response.isSuccessful()){
                            ResponseUser.Data user = response.body().getData();
                            if(email.equalsIgnoreCase(user.getEmail())){
                                editProfile(user.getIdUser(), user.getNamaUser(), user.getNoHp(), user.getEmail(), newPassword, user.getImgProfile());
                            }
                            else{
                                loadingDaftar.dismiss();
                                Toast.makeText(LupaPasswordActivity.this, "Email yang dimasukkan salah !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseUser> call, Throwable t) {
                        loadingDaftar.dismiss();
                        Toast.makeText(getApplicationContext(), "Email tidak terdaftar !", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void editProfile(String id, final String nama, String noHp, final String email, final String password, String image){
        loadingDaftar.dismiss();
        final ProgressDialog dialog = new ProgressDialog(LupaPasswordActivity.this);
        dialog.setTitle("Changing Password");
        dialog.setMessage("Please wait..");
        dialog.show();
        apiService.editProfile(id ,nama, noHp, email, password, image)
                .enqueue(new Callback<ResponsePost>() {
                    @Override
                    public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                        if (response.isSuccessful()){
                            dialog.dismiss();
                            sendPassword(nama, email, password);
                        }
                        else {
                            loadingDaftar.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePost> call, Throwable t) {
                        loadingDaftar.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.koneksi_internet_bermasalah), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendPassword(final String nama, final String email, final String password){
        final String emailAdmin = "luckynine.cs@gmail.com";
        final String passwordAdmin = "bagusbagus09";
        loadingDaftar.dismiss();
        final ProgressDialog dialog = new ProgressDialog(LupaPasswordActivity.this);
        dialog.setTitle("Sending Password");
        dialog.setMessage("Please wait..");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GmailSender sender = new GmailSender(emailAdmin, passwordAdmin);
                    sender.sendMail("Password Member Analisa Properti",
                            "Berikut adalah data akun anda : \n" +
                                    "Nama User : "+nama+"\n" +
                                    "Email : "+email+"\n" +
                                    "Password Baru : "+password,
                            emailAdmin,
                            email);
                    dialog.dismiss();
                    Toast.makeText(LupaPasswordActivity.this, "Password berhasil dikirim", Toast.LENGTH_SHORT).show();
                    finish();
                }
                catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                    dialog.dismiss();
//                    Toast.makeText(LupaPasswordActivity.this, "Password gagal dikirim !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sender.start();
    }

    public String generatePassword(int len, String dic) {
        String result = "";
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(dic.length());
            result += dic.charAt(index);
        }
        return result;
    }

    @OnClick(R.id.btn_to_login)
    public void toLogin(){
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
