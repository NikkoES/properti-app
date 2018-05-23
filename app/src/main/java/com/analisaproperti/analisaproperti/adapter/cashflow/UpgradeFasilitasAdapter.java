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
import com.analisaproperti.analisaproperti.model.cashflow.UpgradeFasilitas;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.TextUtils.concat;

public class UpgradeFasilitasAdapter extends ArrayAdapter<UpgradeFasilitas> {

    @BindView(R.id.txt_nama_fasilitas)
    TextView txtNamaFasilitas;
    @BindView(R.id.txt_kenaikan_harga)
    TextView txtKenaikanHarga;
    @BindView(R.id.txt_jumlah_kamar)
    TextView txtJumlahKamar;

    @BindView(R.id.btn_hapus_fasilitas)
    ImageView btnHapusFasilitas;

    List<UpgradeFasilitas> listFasilitas;

    Context context;

    int resource;

    public UpgradeFasilitasAdapter(Context context, int resource, List<UpgradeFasilitas> listFasilitas) {
        super(context, resource, listFasilitas);
        this.context = context;
        this.resource = resource;
        this.listFasilitas = listFasilitas;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        ButterKnife.bind(this, view);

        UpgradeFasilitas fasilitas = listFasilitas.get(position);

        txtNamaFasilitas.setText(fasilitas.getNamaFasilitas());
        txtKenaikanHarga.setText(""+concat(currencyFormatter(Integer.parseInt(fasilitas.getKenaikanHarga()))));
        txtJumlahKamar.setText(""+fasilitas.getJumlahKamar());

        btnHapusFasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFasilitas(position);
            }
        });

        return view;
    }

    private void removeFasilitas(final int position) {
        listFasilitas.remove(position);
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