package com.a1.chm.myapplication.di.component;

import android.app.Activity;

import com.a1.chm.myapplication.MainActivity;
import com.a1.chm.myapplication.di.module.ActivityModule;
import com.a1.chm.myapplication.di.scope.ActivityScope;

import dagger.Component;

/**
 * Created by codeest on 16/8/7.
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();//提供activity

    void inject(MainActivity mainActivity);
}
