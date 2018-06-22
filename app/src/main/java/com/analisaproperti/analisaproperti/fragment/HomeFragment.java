package com.analisaproperti.analisaproperti.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.activity.cashflow.CashFlowNewActivity;
import com.analisaproperti.analisaproperti.activity.cicilan.CicilanActivity;
import com.analisaproperti.analisaproperti.activity.nilaipasar.NilaiPasarActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private AdView topAds, bottomAds;
    private CardView menuCicilan, menuNilaiPasar, menuCashFlow;
    private EditText etKeterangan;
    private TextView txtJudul;

    int idCicilan, idNilaiPasar, idCashFlow;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.nav_home));
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
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences pref = getContext().getSharedPreferences("setting", Activity.MODE_PRIVATE);
        setLocale(pref.getString("language", ""));

        topAds = (AdView) view.findViewById(R.id.ad_top);
        bottomAds = (AdView) view.findViewById(R.id.ad_bottom);

        menuCicilan = view.findViewById(R.id.menu_cicilan);
        menuNilaiPasar = view.findViewById(R.id.menu_nilai_pasar);
        menuCashFlow = view.findViewById(R.id.menu_cashflow);

        showBannerAd();

        menuCicilan.setOnClickListener(new View.OnClickListener() {
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

        menuNilaiPasar.setOnClickListener(new View.OnClickListener() {
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
                                idNilaiPasar = (int)(Math.random() * (999 - 99) + 1) + 99;
                                Intent i = new Intent(getContext(), NilaiPasarActivity.class);
                                i.putExtra("id_nilai_pasar", ""+idNilaiPasar);
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

        menuCashFlow.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    private void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        topAds.loadAd(adRequest);
        bottomAds.loadAd(adRequest);
    }
}
