package com.a1.chm.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.a1.chm.myapplication.di.component.DaggerActivityComponent;
import com.a1.chm.myapplication.di.module.ActivityModule;
import com.a1.chm.myapplication.ui.activity.BasicDownloadActivity;
import com.a1.chm.myapplication.ui.activity.ButterKnifeActivity;
import com.a1.chm.myapplication.ui.activity.GreenDaoActivity;
import com.a1.chm.myapplication.ui.activity.LogActivity;
import com.a1.chm.myapplication.ui.activity.SocketActivity;
import com.a1.chm.myapplication.ui.activity.SqlcipherActivity;
import com.a1.chm.myapplication.util.ActivityUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_basicDownload)
    Button mBtn_basicDownload;
    @BindView(R.id.btn_sqlcipher)
    Button mBtn_sqlcipher;
    @BindView(R.id.btn_log)
    Button mBtn_log;
    @BindView(R.id.btn_socket)
    Button mBtn_socket;
    @BindView(R.id.greenDaoBtn)
    Button greenDaoBtn;
    @BindView(R.id.butterKnifeBtn)
    Button butterKnifeBtn;
    private TextView tv;

    @Inject
    IIntelligentCoupletService mService;
    //    ApiManage mApiManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //        MyApplication.getAppComponent();
        DaggerActivityComponent.builder()
                .appComponent(MyApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
        tv = (TextView) findViewById(R.id.tv);
    }

    private void showTip(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.btn_basicDownload,R.id.btn_sqlcipher,R.id.btn_log,R.id.btn_socket,R.id.greenDaoBtn,R.id.butterKnifeBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_basicDownload:
                ActivityUtil.startWithoutFinish(MainActivity.this, BasicDownloadActivity.class);
                break;
            case R.id.btn_sqlcipher:
                ActivityUtil.startWithoutFinish(MainActivity.this, SqlcipherActivity.class);
                break;
            case R.id.btn_log:
                ActivityUtil.startWithoutFinish(MainActivity.this, LogActivity.class);
                break;
            case R.id.btn_socket:
                ActivityUtil.startWithoutFinish(MainActivity.this, SocketActivity.class);
                break;
            case R.id.greenDaoBtn:
                ActivityUtil.startWithoutFinish(MainActivity.this, GreenDaoActivity.class);
                break;
            case R.id.butterKnifeBtn:
                ActivityUtil.startWithoutFinish(MainActivity.this, ButterKnifeActivity.class);
                break;
        }

    }
}
