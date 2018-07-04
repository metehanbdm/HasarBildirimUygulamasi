package com.example.rsyazilim.rs_ihbar.network;

import com.example.rsyazilim.rs_ihbar.model.LoginResponse;
import com.example.rsyazilim.rs_ihbar.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LoginService
{
    @GET("android_phone/login")
    Call<LoginResponse> Login(@Query("Kullanici") String kullanici , @Query("Sifre") String sifre);

    @GET("android_phone/register")
    Call<RegisterResponse> Register(
            @Query("AdSoyad") String adSoyad ,
            @Query("KullaniciAdi") String kullaniciAdi,
            @Query("Parola") String parola);

}
