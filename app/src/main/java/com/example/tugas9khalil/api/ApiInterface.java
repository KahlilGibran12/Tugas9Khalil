package com.example.tugas9khalil.api;

import com.example.tugas9khalil.model.login.Login;
import com.example.tugas9khalil.model.register.RegisterData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<Login> loginResponse(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterData> registerResponse(
            @Field("username") String username,
            @Field("password") String password,
            @Field("name") String name
    );
}
