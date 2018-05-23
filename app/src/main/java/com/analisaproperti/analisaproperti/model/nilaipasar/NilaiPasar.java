package com.analisaproperti.analisaproperti.model.nilaipasar;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NilaiPasar implements Serializable{

    @SerializedName("id_nilai_pasar")
    String idNilaiPasar;
    @SerializedName("keterangan")
    String keterangan;
    @SerializedName("tanggal")
    String tanggal;
    @SerializedName("harga_pasaran_per_meter")
    String hargaPasaranPerMeter;
    @SerializedName("perbandingan_properti")
    String perbandinganProperti;
    @SerializedName("catatan_kondisi_bangunan")
    String catatanKondisiBangunan;
    @SerializedName("catatan_survey_lokasi")
    String catatanSurveyLokasi;
    @SerializedName("id_user")
    String idUser;

    public NilaiPasar(String idNilaiPasar, String keterangan, String tanggal, String hargaPasaranPerMeter, String perbandinganProperti, String catatanKondisiBangunan, String catatanSurveyLokasi, String idUser) {
        this.idNilaiPasar = idNilaiPasar;
        this.keterangan = keterangan;
        this.tanggal = tanggal;
        this.hargaPasaranPerMeter = hargaPasaranPerMeter;
        this.perbandinganProperti = perbandinganProperti;
        this.catatanKondisiBangunan = catatanKondisiBangunan;
        this.catatanSurveyLokasi = catatanSurveyLokasi;
        this.idUser = idUser;
    }

    public String getIdNilaiPasar() {
        return idNilaiPasar;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getHargaPasaranPerMeter() {
        return hargaPasaranPerMeter;
    }

    public String getPerbandinganProperti() {
        return perbandinganProperti;
    }

    public String getCatatanKondisiBangunan() {
        return catatanKondisiBangunan;
    }

    public String getCatatanSurveyLokasi() {
        return catatanSurveyLokasi;
    }

    public String getIdUser() {
        return idUser;
    }
}
