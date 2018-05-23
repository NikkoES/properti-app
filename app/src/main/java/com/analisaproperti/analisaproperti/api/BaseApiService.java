package com.analisaproperti.analisaproperti.api;

import com.analisaproperti.analisaproperti.model.response.ResponseCashFlow;
import com.analisaproperti.analisaproperti.model.response.ResponseCicilan;
import com.analisaproperti.analisaproperti.model.response.ResponseExtras;
import com.analisaproperti.analisaproperti.model.response.ResponseFasilitas;
import com.analisaproperti.analisaproperti.model.response.ResponseKamar;
import com.analisaproperti.analisaproperti.model.response.ResponseNilaiPasar;
import com.analisaproperti.analisaproperti.model.response.ResponsePemasukan;
import com.analisaproperti.analisaproperti.model.response.ResponsePengeluaran;
import com.analisaproperti.analisaproperti.model.response.ResponsePost;
import com.analisaproperti.analisaproperti.model.response.ResponsePropertiNilaiPasar;
import com.analisaproperti.analisaproperti.model.response.ResponseUser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("login/")
    Call<ResponsePost> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("register/")
    Call<ResponseBody> register(@Field("id_user") String idUser, @Field("nama_user") String nama,
                                    @Field("no_hp") String noHp, @Field("email") String email, @Field("password") String password, @Field("img_profile") String imgProfile );

    @GET("profile/{email}")
    Call<ResponseUser> getUserData(@Path("email") String email);

    @GET("cicilan/{id_user}")
    Call<ResponseCicilan> getAllCicilan(@Path("id_user") String id_user);

    @FormUrlEncoded
    @POST("cicilan/")
    Call<ResponsePost> saveCicilan(@Field("id_cicilan") String idCicilan,
                                   @Field("keterangan") String keterangan,
                                   @Field("pinjaman_kpr") String pinjamanKpr,
                                   @Field("bunga_per_tahun") String bungaPerTahun,
                                   @Field("tenor_lama_pinjaman") String tenorLamaPinjaman,
                                   @Field("tenor_bunga_fix") String tenorBungaFix,
                                   @Field("sisa_pokok_pinjaman") String sisaPokokPinjaman,
                                   @Field("bunga_floating_per_tahun") String bungaFloating,
                                   @Field("cicilan") String cicilan,
                                   @Field("cicilan_setelah_floating") String cicilanFloating,
                                   @Field("id_user") String idUser);
    @FormUrlEncoded
    @PUT("cicilan/{id_cicilan}")
    Call<ResponsePost> updateCicilan(@Path("id_cicilan") String idCicilan,
                                   @Field("keterangan") String keterangan,
                                   @Field("pinjaman_kpr") String pinjamanKpr,
                                   @Field("bunga_per_tahun") String bungaPerTahun,
                                   @Field("tenor_lama_pinjaman") String tenorLamaPinjaman,
                                   @Field("tenor_bunga_fix") String tenorBungaFix,
                                   @Field("sisa_pokok_pinjaman") String sisaPokokPinjaman,
                                   @Field("bunga_floating_per_tahun") String bungaFloating,
                                   @Field("cicilan") String cicilan,
                                   @Field("cicilan_setelah_floating") String cicilanFloating);

    @DELETE("cicilan/{id_cicilan}")
    Call<ResponsePost> deleteCicilan(@Path("id_cicilan") String idCicilan);

    @GET("nilaipasar/{id_user}")
    Call<ResponseNilaiPasar> getAllNilaiPasar(@Path("id_user") String id_user);

    @GET("nilaipasar/data/{id_nilai_pasar}")
    Call<ResponsePropertiNilaiPasar> getDataNilaiPasar(@Path("id_nilai_pasar") String id_nilai_pasar);

    @FormUrlEncoded
    @POST("nilaipasar/data/")
    Call<ResponsePost> saveNilaiPasar(@Field("id_nilai_pasar") String idNilaiPasar,
                                   @Field("keterangan") String keterangan,
                                   @Field("harga_pasaran_per_meter") String hargaPasaranPerMeter,
                                   @Field("perbandingan_properti") String perbandinganProperti,
                                   @Field("catatan_kondisi_bangunan") String catatanKondisiBangunan,
                                   @Field("catatan_survey_lokasi") String catatanSurveyLokasi,
                                   @Field("id_user") String idUser);

    @FormUrlEncoded
    @POST("nilaipasar/properti/")
    Call<ResponsePost> savePropertiNilaiPasar(@Field("id_nilai_pasar") String idNilaiPasar,
                                      @Field("id_properti") String idProperti,
                                      @Field("harga_jual_properti") String hargaJualProperti,
                                      @Field("luas_tanah") String luasTanah,
                                      @Field("luas_bangunan") String luasBangunan,
                                      @Field("usia_bangunan") String usiaBangunan,
                                      @Field("harga_rata_per_meter") String hargaRataPerMeter,
                                      @Field("harga_bangunan_baru") String hargaBangunanBaru,
                                      @Field("harga_bangunan_saat_ini") String hargaBangunanSaatIni,
                                      @Field("harga_tanah_per_meter") String hargaTanahPerMeter);

    @FormUrlEncoded
    @PUT("nilaipasar/data/{id_nilai_pasar}")
    Call<ResponsePost> updateNilaiPasar(@Path("id_nilai_pasar") String idNilaiPasar,
                                      @Field("keterangan") String keterangan,
                                      @Field("harga_pasaran_per_meter") String hargaPasaranPerMeter,
                                      @Field("perbandingan_properti") String perbandinganProperti,
                                      @Field("catatan_kondisi_bangunan") String catatanKondisiBangunan,
                                      @Field("catatan_survey_lokasi") String catatanSurveyLokasi,
                                      @Field("id_user") String idUser);

    @FormUrlEncoded
    @PUT("nilaipasar/properti/{id_nilai_pasar}/{id_properti}")
    Call<ResponsePost> updatePropertiNilaiPasar(@Path("id_nilai_pasar") String idNilaiPasar,
                                              @Path("id_properti") String idProperti,
                                              @Field("harga_jual_properti") String hargaJualProperti,
                                              @Field("luas_tanah") String luasTanah,
                                              @Field("luas_bangunan") String luasBangunan,
                                              @Field("usia_bangunan") String usiaBangunan,
                                              @Field("harga_rata_per_meter") String hargaRataPerMeter,
                                              @Field("harga_bangunan_baru") String hargaBangunanBaru,
                                              @Field("harga_bangunan_saat_ini") String hargaBangunanSaatIni,
                                              @Field("harga_tanah_per_meter") String hargaTanahPerMeter);

    @DELETE("nilaipasar/data/{id_nilai_pasar}")
    Call<ResponsePost> deleteNilaiPasar(@Path("id_nilai_pasar") String idNilaiPasar);

    @GET("cashflow/{id_user}")
    Call<ResponseCashFlow> getAllCashFlow(@Path("id_user") String idUser);

    @GET("cashflow/data/{id_cash_flow}")
    Call<ResponseCashFlow> getDataCashFlow(@Path("id_cash_flow") String idCashFlow);

    @GET("cashflow/kamar/{id_cash_flow}")
    Call<ResponseKamar> getKamarCashFlow(@Path("id_cash_flow") String idCashFlow);

    @GET("cashflow/pemasukan/{id_cash_flow}")
    Call<ResponsePemasukan> getPemasukanCashFlow(@Path("id_cash_flow") String idCashFlow);

    @GET("cashflow/pengeluaran/{id_cash_flow}")
    Call<ResponsePengeluaran> getPengeluaranCashFlow(@Path("id_cash_flow") String idCashFlow);

    @GET("cashflow/fasilitas/{id_cash_flow}")
    Call<ResponseFasilitas> getFasilitasCashFlow(@Path("id_cash_flow") String idCashFlow);

    @GET("cashflow/extras/{id_cash_flow}")
    Call<ResponseExtras> getExtrasCashFlow(@Path("id_cash_flow") String idCashFlow);

    @FormUrlEncoded
    @POST("cashflow/data/")
    Call<ResponsePost> saveCashFlow(@Field("id_cash_flow") String idCashFlow,
                                      @Field("keterangan") String keterangan,
                                      @Field("id_user") String idUser);

    @FormUrlEncoded
    @POST("cashflow/kamar/")
    Call<ResponsePost> saveKamar(@Field("id_cash_flow") String idCashFlow,
                                    @Field("tipe_kamar") String tipeKamar,
                                    @Field("jumlah_kamar") String jumlahKamar,
                                    @Field("harga_kamar") String hargaKamar);

    @FormUrlEncoded
    @POST("cashflow/pemasukan/")
    Call<ResponsePost> savePemasukan(@Field("id_cash_flow") String idCashFlow,
                                 @Field("pemasukan") String pemasukan,
                                 @Field("jumlah_pemasukan") String jumlahPemasukan);

    @FormUrlEncoded
    @POST("cashflow/pengeluaran/")
    Call<ResponsePost> savePengeluaran(@Field("id_cash_flow") String idCashFlow,
                                     @Field("pengeluaran") String pengeluaran,
                                     @Field("jumlah_pengeluaran") String jumlahPengeluaran);

    @FormUrlEncoded
    @POST("cashflow/fasilitas/")
    Call<ResponsePost> saveFasilitas(@Field("id_cash_flow") String idCashFlow,
                                     @Field("nama_fasilitas") String namaFasilitas,
                                     @Field("kenaikan_harga") String kenaikanHarga,
                                     @Field("jumlah_kamar") String jumlahKamar);

    @FormUrlEncoded
    @POST("cashflow/extras/")
    Call<ResponsePost> saveExtras(@Field("id_cash_flow") String idCashFlow,
                                    @Field("occupancy_rate") String occupancyRate,
                                    @Field("total_penghasilan") String totalPenghasilan,
                                    @Field("total_pemasukan") String totalPemasukan,
                                    @Field("total_pengeluaran") String totalPengeluaran,
                                    @Field("net_operating_income") String netOperatingIncome,
                                    @Field("net_operating_income_future") String netOperatingIncomeFuture);

    @FormUrlEncoded
    @PUT("cashflow/data/{id_cash_flow}")
    Call<ResponsePost> updateCashFlow(@Path("id_cash_flow") String idCashFlow,
                                    @Field("keterangan") String keterangan,
                                    @Field("id_user") String idUser);

    @FormUrlEncoded
    @PUT("cashflow/kamar/{id_cash_flow}")
    Call<ResponsePost> updateKamar(@Path("id_cash_flow") String idCashFlow,
                                 @Field("tipe_kamar") String tipeKamar,
                                 @Field("jumlah_kamar") String jumlahKamar,
                                 @Field("harga_kamar") String hargaKamar);

    @FormUrlEncoded
    @PUT("cashflow/pemasukan/{id_cash_flow}")
    Call<ResponsePost> updatePemasukan(@Path("id_cash_flow") String idCashFlow,
                                     @Field("pemasukan") String pemasukan,
                                     @Field("jumlah_pemasukan") String jumlahPemasukan);

    @FormUrlEncoded
    @PUT("cashflow/pengeluaran/{id_cash_flow}")
    Call<ResponsePost> updatePengeluaran(@Path("id_cash_flow") String idCashFlow,
                                       @Field("pengeluaran") String pengeluaran,
                                       @Field("jumlah_pengeluaran") String jumlahPengeluaran);

    @FormUrlEncoded
    @PUT("cashflow/fasilitas/{id_cash_flow}")
    Call<ResponsePost> updateFasilitas(@Path("id_cash_flow") String idCashFlow,
                                     @Field("nama_fasilitas") String namaFasilitas,
                                     @Field("kenaikan_harga") String kenaikanHarga,
                                     @Field("jumlah_kamar") String jumlahKamar);

    @FormUrlEncoded
    @PUT("cashflow/extras/{id_cash_flow}")
    Call<ResponsePost> updateExtras(@Path("id_cash_flow") String idCashFlow,
                                  @Field("occupancy_rate") String occupancyRate,
                                  @Field("total_penghasilan") String totalPenghasilan,
                                  @Field("total_pemasukan") String totalPemasukan,
                                  @Field("total_pengeluaran") String totalPengeluaran,
                                  @Field("net_operating_income") String netOperatingIncome,
                                  @Field("net_operating_income_future") String netOperatingIncomeFuture);

    @DELETE("cashflow/data/{id_cash_flow}")
    Call<ResponsePost> deleteCashFlow(@Path("id_cash_flow") String idCashFlow);

}