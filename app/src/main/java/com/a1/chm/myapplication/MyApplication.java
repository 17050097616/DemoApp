package com.a1.chm.myapplication;

import android.app.Application;
import android.os.Handler;

import com.a1.chm.myapplication.dao.DBManager;
import com.a1.chm.myapplication.di.component.AppComponent;
import com.a1.chm.myapplication.di.component.DaggerAppComponent;
import com.a1.chm.myapplication.di.module.AppModule;
import com.a1.chm.myapplication.di.module.HttpModule;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zlc.season.rxdownload3.core.DownloadConfig;

/**
 * Created by chenhaoming on 2017/6/29 10:43.
 */
public class MyApplication extends Application {

    public static final String TAG = "MyApplication";
    private static Handler mHandler;
    private static long mMainThreadId;
    private static MyApplication myApplication;
    public static AppComponent appComponent;

    //    public int pageSize = 10;
    //    public ArrayList<String> authority;
    //    public int projectId = 1; //项目ID
    //    public static final int CLIENTTYPE = 1;

    private ExecutorService mThreadPool = Executors.newFixedThreadPool(5);
    //    public List<String> permissionList;

    public static MyApplication getInstance() {
        return myApplication;
    }

    /**
     * 得到主线程的handler
     */
    public static Handler getHandler() {
        return mHandler;
    }

    /**
     * 得到主线程的线程id
     */
    public static long getMainThreadId() {
        return mMainThreadId;
    }


    @Override
    public void onCreate() {

        /*--------------- 创建应用里面需要用到的一些共有的属性 ---------------*/
        // 1.上下文
        super.onCreate();
        myApplication = this;
        //        try {
        //            设置证书
        //            ApiManage.getInstance().setTrustrCertificates(this.getAssets().open("server.bks"));
        //            ApiManage.getInstance().build2Https();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        DownloadConfig.Builder builder = DownloadConfig.Builder.Companion.create(this)
                .enableDb(true)
                .setDbActor(new CustomSqliteActor(this))
                .enableService(true)
                .enableNotification(true);
        SQLiteDatabase.loadLibs(this);//引用SQLiteDatabase的方法之前必须先添加这句代码
        DBManager.init(this);
//        Stetho.initializeWithDefaults(this);
        //        MyApplication.getAppComponent();
    }

    public static AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(myApplication))
                    .httpModule(new HttpModule())
                    .build();
        }
        return appComponent;
    }


    public static void runBackground(Runnable runnable) {
        getInstance().mThreadPool.execute(runnable);
    }


    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     *//*
    public void backgroundAlpha(float bgAlpha)
    {

        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }*/
}
