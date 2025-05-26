package com.example.utsmobile2;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("produk.php")
    Call<List<Produk>> getProduk();

    @GET("getDetailProduk.php")
    Call<Produk> getDetailProduk(@Query("id_produk") String id);

    @FormUrlEncoded
    @POST("get_user.php")
    Call<ResponseUser> getUser(@Field("id_user") String idUser);

    @FormUrlEncoded
    @POST("post_profile.php")
    Call<ResponseUpdate> updateProfile(
            @Field("email") String email,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("kota") String kota,
            @Field("provinsi") String provinsi,
            @Field("telp") String telp,
            @Field("kodepos") String kodepos
    );

    @FormUrlEncoded
    @POST("insert_viewer.php")
    Call<ResponseBody> addViewer(
            @Field("id_user") String id_user,
            @Field("id_produk") String id_produk,
            @Field("nm_viewer") String nm_viewer
    );


}