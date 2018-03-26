package com.a1.chm.myapplication.di.module;

import android.support.v4.app.Fragment;

import dagger.Module;

/**
 * Created by codeest on 16/8/7.
 */

@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

//    @Provides
//    @FragmentScope
//    public Activity provideActivity() {
//        return fragment.getActivity();
//    }
}
