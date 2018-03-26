package com.a1.chm.myapplication.di.module;


import com.a1.chm.myapplication.MyApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by codeest on 16/8/7.
 */

@Module
public class AppModule {
    private final MyApplication application;

    public AppModule(MyApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    MyApplication provideApplicationContext() {
        return application;
    }
//
//    @Provides
//    @Singleton
//    HttpHelper provideHttpHelper(RetrofitHelper retrofitHelper) {
//        return retrofitHelper;
//    }
//
//    @Provides
//    @Singleton
//    DBHelper provideDBHelper(RealmHelper realmHelper) {
//        return realmHelper;
//    }
//
//    @Provides
//    @Singleton
//    PreferencesHelper providePreferencesHelper(ImplPreferencesHelper implPreferencesHelper) {
//        return implPreferencesHelper;
//    }
//
//    @Provides
//    @Singleton
//    DataManager provideDataManager(HttpHelper httpHelper, DBHelper DBHelper, PreferencesHelper preferencesHelper) {
//        return new DataManager(httpHelper, DBHelper, preferencesHelper);
//    }
}
