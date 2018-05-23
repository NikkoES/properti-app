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
import com.analisaproperti.analisaproperti.model.cashflow.Pengeluaran;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.TextUtils.concat;

public class PengeluaranAdapter extends ArrayAdapter<Pengeluaran> {

    @BindView(R.id.txt_keterangan_pengeluaran)
    TextView txtKeteranganPemasukan;
    @BindView(R.id.txt_jumlah_pengeluaran)
    TextView txtJumlahPemasukan;

    @BindView(R.id.btn_hapus_pengeluaran)
    ImageView btnHapusPemasukan;

    List<Pengeluaran> listPengeluaran;

    Context context;

    int resource;

    public PengeluaranAdapter(Context context, int resource, List<Pengeluaran> listPengeluaran) {
        super(context, resource, listPengeluaran);
        this.context = context;
        this.resource = resource;
        this.listPengeluaran = listPengeluaran;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        ButterKnife.bind(this, view);

        Pengeluaran pengeluaran = listPengeluaran.get(position);

        txtKeteranganPemasukan.setText(pengeluaran.getKeteranganPengeluaran());
        txtJumlahPemasukan.setText(""+concat(currencyFormatter(Integer.parseInt(pengeluaran.getJumlahPengeluaran()))));

        btnHapusPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePengeluaran(position);
            }
        });

        return view;
    }

    private void removePengeluaran(final int position) {
        listPengeluaran.remove(position);
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