package com.a1.chm.myapplication.di.module;


import com.a1.chm.myapplication.CommonInterceptor;
import com.a1.chm.myapplication.HttpsUtils;
import com.a1.chm.myapplication.IIntelligentCoupletService;
import com.a1.chm.myapplication.MyApplication;
import com.a1.chm.myapplication.di.qualifier.HttpApi;
import com.a1.chm.myapplication.di.qualifier.HttpsApi;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by codeest on 2017/2/26.
 */

@Module
public class HttpModule {

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }


    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpBuilder() {
        return new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    @HttpApi
    Retrofit provideHttpRetrofit(Retrofit.Builder builder, @HttpApi OkHttpClient client) {
        return createRetrofit(builder, client, IIntelligentCoupletService.HOST);
    }

    @Singleton
    @Provides
    @HttpsApi
    Retrofit provideHttpsRetrofit(Retrofit.Builder builder, @HttpsApi OkHttpClient client) {
        return createRetrofit(builder, client, IIntelligentCoupletService.HOST);
    }

    //    @Singleton
    //    @Provides
    //    @ZhihuUrl
    //    Retrofit provideZhihuRetrofit(Retrofit.Builder builder, OkHttpClient client) {
    //        return createRetrofit(builder, client, ZhihuApis.HOST);
    //    }

    //        @Singleton
    //        @Provides
    //        ZhihuApis provideZhihuService(@ZhihuUrl Retrofit retrofit) {
    //            return retrofit.create(ZhihuApis.class);
    //        }
    @Singleton
    @Provides
    IIntelligentCoupletService provideIIntelligentCoupletService(@HttpsApi Retrofit retrofit) {
        return retrofit.create(IIntelligentCoupletService.class);
    }

    @Singleton
    @Provides
    @HttpApi
    OkHttpClient provideClient(OkHttpClient.Builder builder) {
        //        if (BuildConfig.DEBUG) {
        //            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        //            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        //            builder.addInterceptor(loggingInterceptor);
        //        }
        File cacheFile = new File(MyApplication.getInstance().getCacheDir(), "IntelligentCouplet");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        //        Interceptor cacheInterceptor = new Interceptor() {
        //            @Override
        //            public Response intercept(Chain chain) throws IOException {
        //                Request request = chain.request();
        //                if (!SystemUtil.isNetworkConnected()) {
        //                    request = request.newBuilder()
        //                            .cacheControl(CacheControl.FORCE_CACHE)
        //                            .build();
        //                }
        //                Response response = chain.proceed(request);
        //                if (SystemUtil.isNetworkConnected()) {
        //                    int maxAge = 0;
        //                    // 有网络时, 不缓存, 最大保存时长为0
        //                    response.newBuilder()
        //                            .header("Cache-Control", "public, max-age=" + maxAge)
        //                            .removeHeader("Pragma")
        //                            .build();
        //                } else {
        //                    // 无网络时，设置超时为4周
        //                    int maxStale = 60 * 60 * 24 * 28;
        //                    response.newBuilder()
        //                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
        //                            .removeHeader("Pragma")
        //                            .build();
        //                }
        //                return response;
        //            }
        //        };
        //        Interceptor apikey = new Interceptor() {
        //            @Override
        //            public Response intercept(Chain chain) throws IOException {
        //                Request request = chain.request();
        //                request = request.newBuilder()
        //                        .addHeader("apikey",Constants.KEY_API)
        //                        .build();
        //                return chain.proceed(request);
        //            }
        //        }
        //        设置统一的请求头部参数
        //        builder.addInterceptor(apikey);
        //设置缓存
        CommonInterceptor commonInterceptor = new CommonInterceptor();
        builder.addNetworkInterceptor(commonInterceptor);
        builder.addInterceptor(commonInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    @Singleton
    @Provides
    @HttpsApi
    OkHttpClient provideHttpsClient(OkHttpClient.Builder builder) {
        try {
            //            MyApplication.getInstance().getAssets().open("server.bks");
            File cacheFile = new File(MyApplication.getInstance().getCacheDir(), "IntelligentCouplet");
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

            //设置缓存
            CommonInterceptor commonInterceptor = new CommonInterceptor();
            builder.addNetworkInterceptor(commonInterceptor);
            builder.addInterceptor(commonInterceptor);
            builder.cache(cache);
            //设置超时
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);
            //
            HttpsUtils.SSLParams SSLParams = HttpsUtils.getSslSocketFactory(MyApplication.getInstance().getAssets().open("cert12306.bks"), "pw12306", null);
            builder.sslSocketFactory(SSLParams.sSLSocketFactory, SSLParams.trustManager);
            //错误重连
            builder.retryOnConnectionFailure(true);
            return builder.build();
        } catch (Exception e) {
            return null;
        }
    }

    private Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client, String url) {
        return builder
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();
    }
}
