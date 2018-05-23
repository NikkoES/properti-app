package com.analisaproperti.analisaproperti.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.activity.cicilan.CicilanActivity;
import com.analisaproperti.analisaproperti.adapter.cicilan.CicilanAdapter;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.cicilan.Cicilan;
import com.analisaproperti.analisaproperti.model.response.ResponseCicilan;
import com.analisaproperti.analisaproperti.utils.SharedPreferencesUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CicilanFragment extends Fragment {

    private FloatingActionButton fab;
    EditText etKeterangan;
    TextView txtJudul;

    private RecyclerView rvHome;
    private CicilanAdapter adapter;
    List<Cicilan> cicilanList = new ArrayList<>();

    ProgressDialog loading;

    BaseApiService apiService;

    String idUser;
    int idCicilan;

    private SharedPreferencesUtils userDataSharedPreferences;

    public CicilanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.nav_cicilan));
    }

    public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getContext().getResources().updateConfiguration(config, getContext().getResources().getDisplayMetrics());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cicilan, container, false);

        SharedPreferences pref = getContext().getSharedPreferences("setting", Activity.MODE_PRIVATE);
        setLocale(pref.getString("language", ""));

        userDataSharedPreferences = new SharedPreferencesUtils(getContext(), "UserData");
        try {
            JSONObject userProfile = new JSONObject(userDataSharedPreferences.getPreferenceData("userProfile"));
            idUser = userProfile.get("id_user").toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        rvHome = (RecyclerView) view.findViewById(R.id.rv_cicilan);

        adapter = new CicilanAdapter(getContext(), cicilanList);

        apiService = UtilsApi.getAPIService();

        rvHome.setHasFixedSize(true);
        rvHome.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHome.setAdapter(adapter);

        refresh(idUser);

        fab = view.findViewById(R.id.btn_add_cicilan);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etKeterangan = new EditText(getContext());
                etKeterangan.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);

                txtJudul = new TextView(getContext());
                txtJudul.setText(getString(R.string.sub_judul_tambah_cicilan));
                txtJudul.setGravity(0);

                LinearLayout ll = new LinearLayout(getContext());
                ll.setPadding(48,16,48,16);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(txtJudul);
                ll.addView(etKeterangan);

                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.judul_tambah_cicilan))
                        .setView(ll)
                        .setPositiveButton(getString(R.string.tambah), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                idCicilan = (int)(Math.random() * (999 - 99) + 1) + 99;
                                Intent i = new Intent(getContext(), CicilanActivity.class);
                                i.putExtra("id_cicilan", ""+idCicilan);
                                if(TextUtils.isEmpty(etKeterangan.getText().toString())){
                                    i.putExtra("keterangan", "");
                                }
                                else{
                                    i.putExtra("keterangan", etKeterangan.getText().toString());
                                }
                                startActivity(i);
                            }
                        })
                        .setNegativeButton(getString(R.string.batal), null)
                        .create();
                dialog.show();
            }
        });
        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void refresh(String idUser) {
        loading = ProgressDialog.show(getContext(), null, "Harap Tunggu...", true, false);

        apiService.getAllCicilan(idUser).enqueue(new Callback<ResponseCicilan>() {
            @Override
            public void onResponse(Call<ResponseCicilan> call, Response<ResponseCicilan> response) {
                if (response.isSuccessful()){
                    loading.dismiss();

                    cicilanList = response.body().getData();

                    rvHome.setAdapter(new CicilanAdapter(getContext(), cicilanList));
                    adapter.notifyDataSetChanged();
                } else {
                    loading.dismiss();
                    Toast.makeText(getContext(), getString(R.string.gagal_koneksi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCicilan> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getContext(), getString(R.string.koneksi_internet_bermasalah), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
