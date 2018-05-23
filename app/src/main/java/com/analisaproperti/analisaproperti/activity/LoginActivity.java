package com.analisaproperti.analisaproperti.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.response.ResponsePost;
import com.analisaproperti.analisaproperti.model.response.ResponseUser;
import com.analisaproperti.analisaproperti.utils.SharedPreferencesUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.btn_login_facebook)
    LoginButton btnLoginFacebook;

    GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;

    CallbackManager callbackManager;

    private SharedPreferencesUtils session;

    JSONObject userProfile;

    private ProgressDialog loadingDaftar;

    String email, password;

    BaseApiService apiService;

    String id, nama, sEmail, pict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        SharedPreferences pref = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        setLocale(pref.getString("language", ""));

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        printKeyHash();

        callbackManager = CallbackManager.Factory.create();

        btnLoginFacebook.setReadPermissions(Arrays.asList("public_profile", "email"));
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("response", response.toString());
                        userProfile = new JSONObject();
                        try {
                            URL profilePict = new URL("https://graph.facebook.com/"+object.getString("id")+"/picture?width=250&height=250");

                            userProfile.put("id_user", object.getString("id").substring(10));
                            userProfile.put("name", object.getString("name"));
                            userProfile.put("email", object.getString("email"));
                            userProfile.put("phonenumber", "");
                            userProfile.put("profile_photo", profilePict.toString());

                            session.storeData("userProfile", userProfile.toString());

                            simpanData(object.getString("id").substring(10), object.getString("name"), object.getString("email"), "", "", profilePict.toString());
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields","id, name, email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "User cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Error on Login, check your facebook app_id", Toast.LENGTH_LONG).show();
            }
        });

        apiService = UtilsApi.getAPIService();

        session = new SharedPreferencesUtils(this, "UserData");

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();

        if(session.checkIfDataExists("userProfile")){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }

        loadingDaftar = new ProgressDialog(this);
        loadingDaftar.setTitle("Loading");
        loadingDaftar.setMessage("Checking Data");
        loadingDaftar.setCancelable(false);

    }

    private void simpanData(String idUser, String nama, final String email, String phone, String password, String img){
        apiService.register(idUser, nama, phone, email, password, img)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loadingDaftar.dismiss();
                            checkLogin(email, "");
                        }
                        else {
                            loadingDaftar.dismiss();
                            checkLogin(email, "");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loadingDaftar.dismiss();
                        Toast.makeText(getApplicationContext(), "Koneksi internet bermasalah !", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.btn_login)
    public void login(){
        loadingDaftar.show();
        if(TextUtils.isEmpty(etEmail.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString())){
            Toast.makeText(this, getString(R.string.data_kosong), Toast.LENGTH_SHORT).show();
            loadingDaftar.dismiss();
        }
        else{
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            checkLogin(email, password);
        }
    }

    private void checkLogin(String email, String password){
        final String sEmail = email;
        apiService.login(sEmail, password)
                .enqueue(new Callback<ResponsePost>() {
                    @Override
                    public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                        if (response.body().getData().equalsIgnoreCase("1")){
                            loadingDaftar.dismiss();
                            getUserData(sEmail);
                            Toast.makeText(getApplicationContext(), getString(R.string.login_berhasil), Toast.LENGTH_SHORT).show();
                        } else {
                            loadingDaftar.dismiss();
                            Toast.makeText(getApplicationContext(), getString(R.string.gagal_login_not_match), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePost> call, Throwable t) {
                        loadingDaftar.dismiss();
                        Toast.makeText(getApplicationContext(), "Koneksi internet bermasalah !", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getUserData(String email){
        apiService.getUserData(email).enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if (response.body().getStatus().equals("success")){
                    loadingDaftar.dismiss();
                    ResponseUser.Data data = response.body().getData();

                    id = data.getIdUser();
                    nama = data.getNamaUser();
                    sEmail = data.getEmail();

                    userProfile = new JSONObject();
                    try {
                        userProfile.put("id_user", data.getIdUser());
                        userProfile.put("name", data.getNamaUser());
                        userProfile.put("email", data.getEmail());
                        userProfile.put("phonenumber", data.getNoHp());
                        userProfile.put("profile_photo", ""+data.getImgProfile());

                        session.storeData("userProfile", userProfile.toString());

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
                Toast.makeText(getApplicationContext(), "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_to_register)
    public void toRegister(){
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @OnClick(R.id.btn_login_google)
    public void loginGoogle(){
        Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i, REQ_CODE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void printKeyHash(){
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.analisaproperti.analisaproperti", PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void handleResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            userProfile = new JSONObject();
            try {
                userProfile.put("id_user", account.getId().toString().substring(15));
                userProfile.put("name", account.getDisplayName());
                userProfile.put("email", account.getEmail());
                userProfile.put("phonenumber", "");
                userProfile.put("profile_photo", ""+account.getPhotoUrl());

                session.storeData("userProfile", userProfile.toString());

                simpanData(account.getId().toString().substring(15), account.getDisplayName(), account.getEmail(),"", "", account.getPhotoUrl().toString());
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

}
