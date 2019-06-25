package com.a1.chm.myapplication.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.a1.chm.myapplication.R;

import java.util.List;

import butterknife.BindBitmap;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.Optional;

/**
 * @author chm on 2017/12/11
 */

public class ButterKnifeActivity extends AppCompatActivity {

    @BindViews({R.id.tv1, R.id.tv2, R.id.tv3})
    List<TextView> tvList;
    @BindView(R.id.cbA)
    CheckBox cbA;
    @BindView(R.id.cbB)
    CheckBox cbB;
    @BindString(R.string.app_name)
    String appName;//绑定一个String id为一个String变量
    @BindColor(R.color.colorAccent)
    int colorAccent;//绑定一个color id为一个int变量
    @BindBitmap(R.mipmap.ic_launcher)
    Bitmap bitmap;//绑定图片资源为Bitmap

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife);
        ButterKnife.bind(this);

    }

    @Optional
    @OnCheckedChanged({R.id.cbA, R.id.cbB})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        System.out.println(buttonView.getId());
        switch (buttonView.getId()) {
            case R.id.cbA:
                if (isChecked)
                    ButterKnife.apply(tvList, TEXT, "haha_checkCba");
                else
                    ButterKnife.apply(tvList, TEXT, appName);
                break;
            case R.id.cbB:
                if (isChecked)
                    ButterKnife.apply(tvList, COLOR, colorAccent);
                else
                    ButterKnife.apply(tvList, COLOR, Color.GRAY);
                break;
        }
    }


    //Action接口设置属性
    static final ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setEnabled(false);
        }
    };

    //Setter接口设置属性
    static final ButterKnife.Setter<View, Integer> COLOR = new ButterKnife.Setter<View, Integer>() {
        @Override
        public void set(View view, Integer value, int index) {
            ((TextView) view).setTextColor(value);
        }
    };//一起设置颜色
    //Setter接口设置属性
    static final ButterKnife.Setter<View, String> TEXT = new ButterKnife.Setter<View, String>() {
        @Override
        public void set(View view, String msg, int index) {
            ((TextView) view).setText(msg);
        }
    };//一起设置text


}
