package com.analisaproperti.analisaproperti.adapter.nilaipasar;

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
import com.analisaproperti.analisaproperti.activity.nilaipasar.DetailNilaiPasarActivity;
import com.analisaproperti.analisaproperti.activity.nilaipasar.NilaiPasarActivity;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.nilaipasar.NilaiPasar;
import com.analisaproperti.analisaproperti.model.nilaipasar.PropertiNilaiPasar;
import com.analisaproperti.analisaproperti.model.response.ResponsePost;
import com.analisaproperti.analisaproperti.model.response.ResponsePropertiNilaiPasar;
import com.analisaproperti.analisaproperti.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NilaiPasarAdapter extends RecyclerView.Adapter<NilaiPasarAdapter.ViewHolder> {

    private Context context;
    private List<NilaiPasar> listNilaiPasar;
    private List<PropertiNilaiPasar> listProperti;

    BaseApiService apiService;

    private SharedPreferencesUtils propertiSharedPreferences;

//    String idNilaiPasar, keterangan, tanggal, hargaPasaran, perbandinganProperti, catatanKondisi, catatanSurvey, idUser;

    Gson gsonBuilder;
    JsonParser jsonParser;

    public NilaiPasarAdapter(Context context, List<NilaiPasar> listNilaiPasar){
        this.context = context;
        this.listNilaiPasar = listNilaiPasar;
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
        propertiSharedPreferences = new SharedPreferencesUtils(context, "UserData");

        //apabila ingin diberikan action on click maka variabel yang menerima nilai harus (final)
        final NilaiPasar dataNilaiPasar = listNilaiPasar.get(position);
        final String idNilaiPasar = dataNilaiPasar.getIdNilaiPasar();
        final String keterangan = dataNilaiPasar.getKeterangan();
        final String tanggal = dataNilaiPasar.getTanggal();
        final String hargaPasaran = dataNilaiPasar.getHargaPasaranPerMeter();
        final String perbandinganProperti = dataNilaiPasar.getPerbandinganProperti();
        final String catatanKondisi = dataNilaiPasar.getCatatanKondisiBangunan();
        final String catatanSurvey = dataNilaiPasar.getCatatanSurveyLokasi();
        final String idUser = dataNilaiPasar.getIdUser();

        final JsonObject nilaiPasar = new JsonObject();
        final JsonObject properti = new JsonObject();

        gsonBuilder = new GsonBuilder().create();
        jsonParser = new JsonParser();

        apiService.getDataNilaiPasar(idNilaiPasar).enqueue(new Callback<ResponsePropertiNilaiPasar>() {
            @Override
            public void onResponse(Call<ResponsePropertiNilaiPasar> call, Response<ResponsePropertiNilaiPasar> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equalsIgnoreCase("success")){
                        listProperti = response.body().getData();
                        nilaiPasar.addProperty("id_nilai_pasar", idNilaiPasar);
                        nilaiPasar.addProperty("keterangan", keterangan);
                        nilaiPasar.addProperty("tanggal", tanggal);
                        nilaiPasar.addProperty("harga_pasaran_per_meter", hargaPasaran);
                        nilaiPasar.addProperty("perbandingan_properti", perbandinganProperti);
                        nilaiPasar.addProperty("catatan_kondisi_bangunan", catatanKondisi);
                        nilaiPasar.addProperty("catatan_survey_lokasi", catatanSurvey);
                        nilaiPasar.addProperty("id_user", idUser);
                        nilaiPasar.add("properti", jsonParser.parse(gsonBuilder.toJson(listProperti)).getAsJsonArray());
                    }
                    else {
                        Toast.makeText(context, "Data gagal get !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Gagal koneksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePropertiNilaiPasar> call, Throwable t) {
                Toast.makeText(context, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });

        holder.txtKeterangan.setText(keterangan);
        holder.txtTanggal.setText(tanggal);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] dialogitem = {"Detail Data", "Edit Data"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(dialogitem, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int item){
                        switch(item){
                            case 0 : {
                                Intent i = new Intent(context, DetailNilaiPasarActivity.class);
                                i.putExtra("position", position);
                                i.putExtra("properti", (ArrayList<PropertiNilaiPasar>) listProperti);
                                i.putExtra("propertiNilaiPasar", nilaiPasar.toString());
                                context.startActivity(i);
                                break;
                            }
                            case 1 : {
                                Intent i = new Intent(context, NilaiPasarActivity.class);
                                i.putExtra("position", position);
                                i.putExtra("propertiNilaiPasar", nilaiPasar.toString());
                                context.startActivity(i);
                                break;
                            }
                        }
                    }
                });
                builder.create().show();
            }
        });
        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.deleteNilaiPasar(idNilaiPasar).enqueue(new Callback<ResponsePost>() {
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
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNilaiPasar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtKeterangan;
        TextView txtTanggal;
        CardView cv;
        ImageView btnHapus;

        public ViewHolder(View itemView) {
            super(itemView);

            txtKeterangan = itemView.findViewById(R.id.txt_keterangan);
            txtTanggal = itemView.findViewById(R.id.txt_tanggal);
            cv = itemView.findViewById(R.id.cv);
            btnHapus = itemView.findViewById(R.id.btn_hapus);
        }
    }

}
