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
import com.analisaproperti.analisaproperti.activity.cashflow.CashFlowNewActivity;
import com.analisaproperti.analisaproperti.adapter.cashflow.CashFlowAdapter;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.cashflow.CashFlow;
import com.analisaproperti.analisaproperti.model.response.ResponseCashFlow;
import com.analisaproperti.analisaproperti.utils.SharedPreferencesUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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
public class CashFlowFragment extends Fragment {

    private FloatingActionButton fab;
    EditText etKeterangan;
    TextView txtJudul;
    TextView tekanTombol;

    private RecyclerView rvHome;
    private CashFlowAdapter adapter;
    List<CashFlow> cashFlowList = new ArrayList<>();

    ProgressDialog loading;

    BaseApiService apiService;

    String idUser;
    int idCashFlow;

    AdView adBottom;

    private SharedPreferencesUtils userDataSharedPreferences;


    public CashFlowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.nav_cash_flow));
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
        View view = inflater.inflate(R.layout.fragment_cash_flow, container, false);

        SharedPreferences pref = getContext().getSharedPreferences("setting", Activity.MODE_PRIVATE);
        setLocale(pref.getString("language", ""));

        adBottom = view.findViewById(R.id.ad_bottom);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adBottom.loadAd(adRequest);

        userDataSharedPreferences = new SharedPreferencesUtils(getContext(), "UserData");
        try {
            JSONObject userProfile = new JSONObject(userDataSharedPreferences.getPreferenceData("userProfile"));
            idUser = userProfile.get("id_user").toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        rvHome = (RecyclerView) view.findViewById(R.id.rv_cash_flow);
        tekanTombol = (TextView) view.findViewById(R.id.tekan_untuk_memulai);

        adapter = new CashFlowAdapter(getContext(), cashFlowList);

        apiService = UtilsApi.getAPIService();

        rvHome.setHasFixedSize(true);
        rvHome.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHome.setAdapter(adapter);

        refresh(idUser);

        fab = view.findViewById(R.id.btn_add_cash_flow);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etKeterangan = new EditText(getContext());
                etKeterangan.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);

                txtJudul = new TextView(getContext());
                txtJudul.setText(getString(R.string.masukan_nama_file));
                txtJudul.setGravity(0);

                LinearLayout ll = new LinearLayout(getContext());
                ll.setPadding(48,16,48,16);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(txtJudul);
                ll.addView(etKeterangan);

                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.nama_file))
                        .setView(ll)
                        .setPositiveButton(getString(R.string.tambah), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                idCashFlow = (int)(Math.random() * (999 - 99) + 1) + 99;
                                Intent i = new Intent(getContext(), CashFlowNewActivity.class);
                                i.putExtra("id_cash_flow", ""+idCashFlow);
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

    public void refresh(String idUser) {
        loading = ProgressDialog.show(getContext(), null, "Harap Tunggu...", true, false);

        apiService.getAllCashFlow(idUser).enqueue(new Callback<ResponseCashFlow>() {
            @Override
            public void onResponse(Call<ResponseCashFlow> call, Response<ResponseCashFlow> response) {
                if (response.isSuccessful()){
                    loading.dismiss();

                    cashFlowList = response.body().getData();

                    if(cashFlowList.size()==0){
                        tekanTombol.setVisibility(View.VISIBLE);
                        rvHome.setVisibility(View.GONE);
                    }
                    else{
                        tekanTombol.setVisibility(View.GONE);
                        rvHome.setVisibility(View.VISIBLE);
                    }

                    rvHome.setAdapter(new CashFlowAdapter(getContext(), cashFlowList));
                    adapter.notifyDataSetChanged();
                } else {
                    loading.dismiss();
                    Toast.makeText(getContext(), getString(R.string.gagal_koneksi), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCashFlow> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getContext(), getString(R.string.koneksi_internet_bermasalah), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
