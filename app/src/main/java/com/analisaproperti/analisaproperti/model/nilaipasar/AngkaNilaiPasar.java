package com.analisaproperti.analisaproperti.model.nilaipasar;

import java.io.Serializable;

public class AngkaNilaiPasar implements Serializable{

    long hargaJualProperty, luasTanah, luasBangunan, usiaBangunan, hargaRataBangunanPerMeter;

    long hargaBangunanBaru, hargaBangunanSaatIni, hargaTanahPerMeter;

    public AngkaNilaiPasar(long hargaJualProperty, long luasTanah, long luasBangunan, long usiaBangunan, long hargaRataBangunanPerMeter, long hargaBangunanBaru, long hargaBangunanSaatIni, long hargaTanahPerMeter) {
        this.hargaJualProperty = hargaJualProperty;
        this.luasTanah = luasTanah;
        this.luasBangunan = luasBangunan;
        this.usiaBangunan = usiaBangunan;
        this.hargaRataBangunanPerMeter = hargaRataBangunanPerMeter;
        this.hargaBangunanBaru = hargaBangunanBaru;
        this.hargaBangunanSaatIni = hargaBangunanSaatIni;
        this.hargaTanahPerMeter = hargaTanahPerMeter;
    }

    public long getHargaJualProperty() {
        return hargaJualProperty;
    }

    public long getLuasTanah() {
        return luasTanah;
    }

    public long getLuasBangunan() {
        return luasBangunan;
    }

    public long getUsiaBangunan() {
        return usiaBangunan;
    }

    public long getHargaRataBangunanPerMeter() {
        return hargaRataBangunanPerMeter;
    }

    public long getHargaBangunanBaru() {
        return hargaBangunanBaru;
    }

    public long getHargaBangunanSaatIni() {
        return hargaBangunanSaatIni;
    }

    public long getHargaTanahPerMeter() {
        return hargaTanahPerMeter;
    }
}
