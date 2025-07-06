package com.example.utsmobile2;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @GET("produk.php")
    Call<List<Produk>> getProduk();
    @GET("kategori/get_kategori1.php")
    Call<List<Produk>> getKategori1();
    @GET("kategori/get_kategori2.php")
    Call<List<Produk>> getKategori2();
    @GET("kategori/get_kategori3.php")
    Call<List<Produk>> getKategori3();
    @GET("kategori/get_terlaris.php")
    Call<List<Produk>> getTerlaris();
    @GET("getDetailProduk.php")
    Call<Produk> getDetailProduk(@Query("id_produk") String id);

    @FormUrlEncoded
    @POST("get_user.php")
    Call<ResponseUser> getUser(@Field("id_user") String idUser);

    @Multipart
    @POST("post_profile.php") // Ensure this endpoint can handle multipart requests
    Call<ResponseUpdate> updateProfile(
            @Part MultipartBody.Part image, // Add this line for the image
            @Part("email") RequestBody email,
            @Part("nama") RequestBody nama,
            @Part("alamat") RequestBody alamat,
            @Part("kota") RequestBody kota,
            @Part("provinsi") RequestBody provinsi,
            @Part("telp") RequestBody telp,
            @Part("kodepos") RequestBody kodepos
    );

    @FormUrlEncoded
    @POST("insert_viewer.php")
    Call<ResponseBody> addViewer(
            @Field("id_user") String id_user,
            @Field("id_produk") String id_produk,
            @Field("nm_viewer") String nm_viewer
    );


}