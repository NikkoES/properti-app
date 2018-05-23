package com.analisaproperti.analisaproperti.adapter.cashflow;

import android.content.Context;
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
import com.analisaproperti.analisaproperti.activity.cashflow.CashFlowNewActivity;
import com.analisaproperti.analisaproperti.api.BaseApiService;
import com.analisaproperti.analisaproperti.api.UtilsApi;
import com.analisaproperti.analisaproperti.model.cashflow.CashFlow;
import com.analisaproperti.analisaproperti.model.cashflow.Extras;
import com.analisaproperti.analisaproperti.model.cashflow.Kamar;
import com.analisaproperti.analisaproperti.model.cashflow.Pemasukan;
import com.analisaproperti.analisaproperti.model.cashflow.Pengeluaran;
import com.analisaproperti.analisaproperti.model.cashflow.UpgradeFasilitas;
import com.analisaproperti.analisaproperti.model.response.ResponseExtras;
import com.analisaproperti.analisaproperti.model.response.ResponseFasilitas;
import com.analisaproperti.analisaproperti.model.response.ResponseKamar;
import com.analisaproperti.analisaproperti.model.response.ResponsePemasukan;
import com.analisaproperti.analisaproperti.model.response.ResponsePengeluaran;
import com.analisaproperti.analisaproperti.model.response.ResponsePost;
import com.analisaproperti.analisaproperti.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CashFlowAdapter extends RecyclerView.Adapter<CashFlowAdapter.ViewHolder> {

    private Context context;
    private List<CashFlow> listCashFlow;
    private List<Kamar> listKamar;
    private List<Pemasukan> listPemasukan;
    private List<Pengeluaran> listPengeluaran;
    private List<UpgradeFasilitas> listFasilitas;
    private List<Extras> listExtras;

    BaseApiService apiService;

    private SharedPreferencesUtils sharedPreferencesUtils;

    Gson gsonBuilder;
    JsonParser jsonParser;

    final JsonObject dataCashFlow = new JsonObject();
    final JsonObject dataKamar = new JsonObject();
    final JsonObject dataPemasukan = new JsonObject();
    final JsonObject dataPengeluaran = new JsonObject();
    final JsonObject dataFasilitas = new JsonObject();
    final JsonObject dataExtras = new JsonObject();

    double occupancyRate = 0;

    public CashFlowAdapter(Context context, List<CashFlow> listCashFlow){
        this.context = context;
        this.listCashFlow = listCashFlow;
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
        sharedPreferencesUtils = new SharedPreferencesUtils(context, "UserData");

        //apabila ingin diberikan action on click maka variabel yang menerima nilai harus (final)
        final CashFlow dataNilaiPasar = listCashFlow.get(position);
        final String idCashFlow = dataNilaiPasar.getIdCashFlow();
        final String keterangan = dataNilaiPasar.getKeterangan();
        final String tanggal = dataNilaiPasar.getTanggal();
        final String idUser = dataNilaiPasar.getIdUser();

        gsonBuilder = new GsonBuilder().create();
        jsonParser = new JsonParser();

        holder.txtKeterangan.setText(keterangan);
        holder.txtTanggal.setText(tanggal);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataCashFlow.addProperty("id_cash_flow", idCashFlow);
                dataCashFlow.addProperty("keterangan", keterangan);
                dataCashFlow.addProperty("tanggal", tanggal);
                dataCashFlow.addProperty("id_user", idUser);
                sharedPreferencesUtils.storeData("dataCashFlow", dataCashFlow.toString());

                getDataKamar(idCashFlow);
                getDataPemasukan(idCashFlow);
                getDataPengeluaran(idCashFlow);
                getDataFasilitas(idCashFlow);
                getDataExtras(idCashFlow);

                Intent i = new Intent(context, CashFlowNewActivity.class);
                i.putExtra("position", position);
                i.putExtra("dataCashFlow", dataCashFlow.toString());
                i.putExtra("dataKamar", dataKamar.toString());
                i.putExtra("dataPemasukan", dataPemasukan.toString());
                i.putExtra("dataPengeluaran", dataPengeluaran.toString());
                i.putExtra("dataFasilitas", dataFasilitas.toString());
                i.putExtra("dataExtras", dataExtras.toString());
                context.startActivity(i);
            }
        });
        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.deleteCashFlow(idCashFlow).enqueue(new Callback<ResponsePost>() {
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

    private void getDataKamar(String idCashFlow){
        apiService.getKamarCashFlow(idCashFlow).enqueue(new Callback<ResponseKamar>() {
            @Override
            public void onResponse(Call<ResponseKamar> call, Response<ResponseKamar> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equalsIgnoreCase("success")){
                        listKamar = response.body().getData();
                        dataKamar.add("kamar", jsonParser.parse(gsonBuilder.toJson(listKamar)).getAsJsonArray());
                        sharedPreferencesUtils.storeData("kamar", dataKamar.toString());
                    }
                    else {
                        Toast.makeText(context, "Data gagal get !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Gagal koneksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseKamar> call, Throwable t) {
                Toast.makeText(context, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataPemasukan(String idCashFlow){
        apiService.getPemasukanCashFlow(idCashFlow).enqueue(new Callback<ResponsePemasukan>() {
            @Override
            public void onResponse(Call<ResponsePemasukan> call, Response<ResponsePemasukan> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equalsIgnoreCase("success")){
                        listPemasukan = response.body().getData();
                        dataPemasukan.add("pemasukan", jsonParser.parse(gsonBuilder.toJson(listPemasukan)).getAsJsonArray());
                        sharedPreferencesUtils.storeData("pemasukan", dataPemasukan.toString());
                    }
                    else {
                        Toast.makeText(context, "Data gagal get !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Gagal koneksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePemasukan> call, Throwable t) {
                Toast.makeText(context, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataPengeluaran(String idCashFlow){
        apiService.getPengeluaranCashFlow(idCashFlow).enqueue(new Callback<ResponsePengeluaran>() {
            @Override
            public void onResponse(Call<ResponsePengeluaran> call, Response<ResponsePengeluaran> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equalsIgnoreCase("success")){
                        listPengeluaran = response.body().getData();
                        dataPengeluaran.add("pengeluaran", jsonParser.parse(gsonBuilder.toJson(listPengeluaran)).getAsJsonArray());
                        sharedPreferencesUtils.storeData("pengeluaran", dataPengeluaran.toString());
                    }
                    else {
                        Toast.makeText(context, "Data gagal get !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Gagal koneksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePengeluaran> call, Throwable t) {
                Toast.makeText(context, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataFasilitas(String idCashFlow){
        apiService.getFasilitasCashFlow(idCashFlow).enqueue(new Callback<ResponseFasilitas>() {
            @Override
            public void onResponse(Call<ResponseFasilitas> call, Response<ResponseFasilitas> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equalsIgnoreCase("success")){
                        listFasilitas = response.body().getData();
                        dataFasilitas.add("fasilitas", jsonParser.parse(gsonBuilder.toJson(listFasilitas)).getAsJsonArray());
                        sharedPreferencesUtils.storeData("fasilitas", dataFasilitas.toString());
                    }
                    else {
                        Toast.makeText(context, "Data gagal get !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Gagal koneksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseFasilitas> call, Throwable t) {
                Toast.makeText(context, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataExtras(String idCashFlow){
        apiService.getExtrasCashFlow(idCashFlow).enqueue(new Callback<ResponseExtras>() {
            @Override
            public void onResponse(Call<ResponseExtras> call, Response<ResponseExtras> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus().equalsIgnoreCase("success")){
                        listExtras = response.body().getData();
                        if(listExtras.size()!=0){
                            final Extras extras = listExtras.get(0);
                            occupancyRate = Double.parseDouble(extras.getOccupancyRate());
                        }
                        else{
                            occupancyRate = 0;
                        }
                        dataExtras.add("extras", jsonParser.parse(gsonBuilder.toJson(listExtras)).getAsJsonArray());
                        sharedPreferencesUtils.storeData("extras", dataExtras.toString());
                    }
                    else {
                        Toast.makeText(context, "Data gagal get !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Gagal koneksi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseExtras> call, Throwable t) {
                Toast.makeText(context, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCashFlow.size();
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
