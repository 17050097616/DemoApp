package com.a1.chm.myapplication.di.component;


import com.a1.chm.myapplication.IIntelligentCoupletService;
import com.a1.chm.myapplication.MyApplication;
import com.a1.chm.myapplication.di.module.AppModule;
import com.a1.chm.myapplication.di.module.HttpModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by codeest on 16/8/7.
 */

@Singleton
@Component(modules = {AppModule.class, HttpModule.class})
public interface AppComponent {

    MyApplication getContext();  // 提供App的Context

    IIntelligentCoupletService getIIntelligentCoupletService();

//    DataManager getDataManager(); //数据中心
//
//    RetrofitHelper retrofitHelper();  //提供http的帮助类
//
//    RealmHelper realmHelper();    //提供数据库帮助类
//
//    ImplPreferencesHelper preferencesHelper(); //提供sp帮助类
}
