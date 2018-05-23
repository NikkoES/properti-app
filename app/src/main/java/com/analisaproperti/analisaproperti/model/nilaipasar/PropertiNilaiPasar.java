package com.analisaproperti.analisaproperti.model.nilaipasar;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PropertiNilaiPasar implements Serializable {

    @SerializedName("id_nilai_pasar")
    String idNilaiPasar;
    @SerializedName("id_properti")
    String idProperti;
    @SerializedName("harga_jual_properti")
    String hargaJualProperti;
    @SerializedName("luas_tanah")
    String luasTanah;
    @SerializedName("luas_bangunan")
    String luasBangunan;
    @SerializedName("usia_bangunan")
    String usiaBangunan;
    @SerializedName("harga_rata_per_meter")
    String hargaRataPerMeter;
    @SerializedName("harga_bangunan_baru")
    String hargaBangunanBaru;
    @SerializedName("harga_bangunan_saat_ini")
    String hargaBangunanSaatIni;
    @SerializedName("harga_tanah_per_meter")
    String hargaTanahPerMeter;

    public PropertiNilaiPasar(String idNilaiPasar, String idProperti, String hargaJualProperti, String luasTanah, String luasBangunan, String usiaBangunan, String hargaRataPerMeter, String hargaBangunanBaru, String hargaBangunanSaatIni, String hargaTanahPerMeter) {
        this.idNilaiPasar = idNilaiPasar;
        this.idProperti = idProperti;
        this.hargaJualProperti = hargaJualProperti;
        this.luasTanah = luasTanah;
        this.luasBangunan = luasBangunan;
        this.usiaBangunan = usiaBangunan;
        this.hargaRataPerMeter = hargaRataPerMeter;
        this.hargaBangunanBaru = hargaBangunanBaru;
        this.hargaBangunanSaatIni = hargaBangunanSaatIni;
        this.hargaTanahPerMeter = hargaTanahPerMeter;
    }

    public String getIdNilaiPasar() {
        return idNilaiPasar;
    }

    public String getIdProperti() {
        return idProperti;
    }

    public String getHargaJualProperti() {
        return hargaJualProperti;
    }

    public String getLuasTanah() {
        return luasTanah;
    }

    public String getLuasBangunan() {
        return luasBangunan;
    }

    public String getUsiaBangunan() {
        return usiaBangunan;
    }

    public String getHargaRataPerMeter() {
        return hargaRataPerMeter;
    }

    public String getHargaBangunanBaru() {
        return hargaBangunanBaru;
    }

    public String getHargaBangunanSaatIni() {
        return hargaBangunanSaatIni;
    }

    public String getHargaTanahPerMeter() {
        return hargaTanahPerMeter;
    }
}
