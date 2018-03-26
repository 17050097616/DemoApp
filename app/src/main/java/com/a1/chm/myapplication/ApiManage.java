package com.a1.chm.myapplication;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiManage {

    public static ApiManage apiManage;
    private static File httpCacheDirectory = new File(MyApplication.getInstance().getCacheDir(), "IntelligentCouplet");
    private static int cacheSize = 10 * 1024 * 1024; // 10 MiB
    private static Cache cache = new Cache(httpCacheDirectory, cacheSize);

    private static final String TAG = "ApiManage";
    private static final String CERTIFICATEALIAS = "intelligentcouplet";
    private static final String STOREPASSWORD = "intelligentcouplet123456";
    public static final String BASE_URL = "http://10.187.0.92:8080/smart-order/api/";
    //    @Inject
    //    public ApiManage(IIntelligentCoupletService service){
    //        this.service=service;
    //    }

    public retrofit2.Call<Object> getHttps() {
        return service.getHttps();
    }


    private IIntelligentCoupletService service;
    //    private IApkService apkService;
    //    private ISyncService syncService;

    private Object monitor = new Object();

    private static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(30, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
            .addNetworkInterceptor(new CommonInterceptor())
            .addInterceptor(new CommonInterceptor())
            .addNetworkInterceptor(new LogInterceptor())
            .cache(cache)
            .build();

    //    private OkHttpClient httpsClient = build2Https();
    private OkHttpClient httpsClient;

    private Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    public static ApiManage getInstance() {
        if (apiManage == null) {
            synchronized (ApiManage.class) {
                if (apiManage == null) {
                    apiManage = new ApiManage();
                }
            }
        }
        return apiManage;
    }

    public IIntelligentCoupletService getApiService() {
        if (service == null) {
            synchronized (monitor) {
                if (service == null) {
                    service = new Retrofit.Builder()
                            .baseUrl("https://kyfw.12306.cn/otn/")//测试修改
                            //                            .baseUrl("https://www.baidu.com")//测试修改
                            //                            .baseUrl("https://youku.com/")//测试修改
                            //                            .baseUrl("https://certs.cac.washington.edu/CAtest/")//测试修改
                            //                            .baseUrl("https://192.168.3.111:8443/")//测试修改
                            //                            .client(client)
                            .client(httpsClient)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build().create(IIntelligentCoupletService.class);
                }
            }
        }

        return service;
    }

    //https
    private InputStream mTrustrCertificate;

    public OkHttpClient build2Https() {
        try {
            HttpsUtils.SSLParams SSLParams = HttpsUtils.getSslSocketFactory(MyApplication.getInstance().getAssets().open("server.bks"), "server", null);
            httpsClient = new OkHttpClient.Builder()
                    //                    .certificatePinner(new CertificatePinner.Builder()
                    //                            .add("https://192.168.3.111:8443/").build())
                    //                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .sslSocketFactory(SSLParams.sSLSocketFactory, SSLParams.trustManager)
                    .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(30, TimeUnit.SECONDS)//设置写的超时时间
                    .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
                    //                    .addNetworkInterceptor(
                    //                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                    .addNetworkInterceptor(new CommonInterceptor())
                    .addInterceptor(new CommonInterceptor())
                    .addNetworkInterceptor(new LogInterceptor())
                    .cache(cache)
                    .build();
            Log.d(TAG, "build2HttpsClient: ");
            return httpsClient;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
