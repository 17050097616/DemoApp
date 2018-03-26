package com.a1.chm.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;


public interface IIntelligentCoupletService {

    String HOST = "https://kyfw.12306.cn/otn/";

//    @GET("init")
    @GET(" ")
    Call<Object> getHttps();

}
