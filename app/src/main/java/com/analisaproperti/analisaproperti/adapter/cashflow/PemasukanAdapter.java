package com.analisaproperti.analisaproperti.adapter.cashflow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.model.cashflow.Pemasukan;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.TextUtils.concat;

public class PemasukanAdapter extends ArrayAdapter<Pemasukan> {

    @BindView(R.id.txt_keterangan_pemasukan)
    TextView txtKeteranganPemasukan;
    @BindView(R.id.txt_jumlah_pemasukan)
    TextView txtJumlahPemasukan;

    @BindView(R.id.btn_hapus_pemasukan)
    ImageView btnHapusPemasukan;

    List<Pemasukan> listPemasukan;

    Context context;

    int resource;

    public PemasukanAdapter(Context context, int resource, List<Pemasukan> listPemasukan) {
        super(context, resource, listPemasukan);
        this.context = context;
        this.resource = resource;
        this.listPemasukan = listPemasukan;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        ButterKnife.bind(this, view);

        Pemasukan pemasukan = listPemasukan.get(position);

        txtKeteranganPemasukan.setText(pemasukan.getKeteranganPemasukan());
        txtJumlahPemasukan.setText(""+concat(currencyFormatter(Integer.parseInt(pemasukan.getJumlahPemasukan()))));

        btnHapusPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeHero(position);
            }
        });

        return view;
    }

    private void removeHero(final int position) {
        listPemasukan.remove(position);
        notifyDataSetChanged();
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

}