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
import com.analisaproperti.analisaproperti.model.cashflow.Kamar;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.TextUtils.concat;

public class KamarAdapter extends ArrayAdapter<Kamar> {

    @BindView(R.id.txt_tipe_kamar)
    TextView txtTipeKamar;
    @BindView(R.id.txt_jumlah_kamar)
    TextView txtJumlahKamar;
    @BindView(R.id.txt_harga_kamar)
    TextView txtHargaKamar;

    @BindView(R.id.btn_hapus_kamar)
    ImageView btnHapusKamar;

    List<Kamar> listKamar;

    Context context;

    int resource;

    public KamarAdapter(Context context, int resource, List<Kamar> listKamar) {
        super(context, resource, listKamar);
        this.context = context;
        this.resource = resource;
        this.listKamar = listKamar;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        ButterKnife.bind(this, view);

        Kamar kamar = listKamar.get(position);

        txtTipeKamar.setText(kamar.getTipeKamar());
        txtJumlahKamar.setText(""+kamar.getJumlahKamar());
        txtHargaKamar.setText(""+concat(currencyFormatter(Integer.parseInt(kamar.getHargaKamar()))));

        btnHapusKamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeHero(position);
            }
        });

        return view;
    }

    private void removeHero(final int position) {
        listKamar.remove(position);
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