package com.analisaproperti.analisaproperti.adapter.cicilan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.analisaproperti.analisaproperti.R;
import com.analisaproperti.analisaproperti.activity.cicilan.CicilanActivity;
import com.analisaproperti.analisaproperti.activity.cicilan.DetailCicilanActivity;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.cicilan.Cicilan;
import com.analisaproperti.analisaproperti.model.response.ResponsePost;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CicilanAdapter extends RecyclerView.Adapter<CicilanAdapter.ViewHolder>{

    private Context context;
    private List<Cicilan> listCicilan;

    BaseApiService apiService;

    String idCicilan, keterangan, tanggal, pinjamanKpr, bungaPerTahun, tenorLamaPinjaman, tenorBungaFix, sisaPokokPinjaman, bungaFloatingPerTahun, cicilan, cicilanSetelahFloating;

    public CicilanAdapter(Context context, List<Cicilan> listCicilan){
        this.context = context;
        this.listCicilan = listCicilan;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_keterangan, null, false);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        apiService = UtilsApi.getAPIService();

        final Cicilan dataCicilan = listCicilan.get(position);
        idCicilan = dataCicilan.getIdCicilan();
        keterangan = dataCicilan.getKeterangan();
        tanggal = dataCicilan.getTanggal();
        pinjamanKpr = dataCicilan.getPinjamanKpr();
        bungaPerTahun = dataCicilan.getBungaPerTahun();
        tenorLamaPinjaman = dataCicilan.getTenorLamaPinjaman();
        tenorBungaFix = dataCicilan.getTenorBungaFix();
        sisaPokokPinjaman = dataCicilan.getSisaPokokPinjaman();
        bungaFloatingPerTahun = dataCicilan.getBungaFloatingPerTahun();
        cicilan = dataCicilan.getCicilan();
        cicilanSetelahFloating = dataCicilan.getCicilanSetelahFloating();

        holder.txtKeterangan.setText(keterangan);
        holder.txtTanggal.setText(tanggal);
        holder.cvCicilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] dialogitem = {"Detail Data", "Edit Data"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(dialogitem, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int item){
                        switch(item){
                            case 0 : {
                                Intent i = new Intent(context, DetailCicilanActivity.class);
                                i.putExtra("Cicilan", (ArrayList<Cicilan>) listCicilan);
                                i.putExtra("position", position);
                                context.startActivity(i);
                                break;
                            }
                            case 1 : {
                                Intent i = new Intent(context, CicilanActivity.class);
                                i.putExtra("Cicilan", (ArrayList<Cicilan>) listCicilan);
                                i.putExtra("position", position);
                                context.startActivity(i);
                                break;
                            }
                        }
                    }
                });
                builder.create().show();
            }
        });
        holder.btnHapusCicilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.deleteCicilan(idCicilan).enqueue(new Callback<ResponsePost>() {
                    @Override
                    public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                        if (response.isSuccessful()){
                            if (response.body().getData().equalsIgnoreCase("1")){
                                Toast.makeText(context, context.getString(R.string.berhasil_dihapus), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context, context.getString(R.string.gagal_dihapus), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, context.getString(R.string.gagal_koneksi), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsePost> call, Throwable t) {
                        Toast.makeText(context, context.getString(R.string.koneksi_internet_bermasalah), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCicilan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtKeterangan;
        TextView txtTanggal;
        CardView cvCicilan;
        ImageView btnHapusCicilan;

        public ViewHolder(View itemView) {
            super(itemView);

            txtKeterangan = itemView.findViewById(R.id.txt_keterangan);
            txtTanggal = itemView.findViewById(R.id.txt_tanggal);
            cvCicilan = itemView.findViewById(R.id.cv);
            btnHapusCicilan = itemView.findViewById(R.id.btn_hapus);
        }
    }

}