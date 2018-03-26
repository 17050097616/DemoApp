package com.a1.chm.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.a1.chm.myapplication.dao.DBManager;
import com.a1.chm.myapplication.di.component.DaggerActivityComponent;
import com.a1.chm.myapplication.di.module.ActivityModule;
import com.a1.chm.myapplication.ui.activity.BasicDownloadActivity;
import com.a1.chm.myapplication.ui.activity.LogActivity;
import com.a1.chm.myapplication.ui.activity.SocketActivity;
import com.a1.chm.myapplication.ui.activity.SqlcipherActivity;
import com.a1.chm.myapplication.util.ActivityUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_delete)
    Button mBtnDelete;
    @BindView(R.id.btnUpdate)
    Button mBtnUpdate;
    @BindView(R.id.queryBtn)
    Button mQueryBtn;
    @BindView(R.id.queryTwoBtn)
    Button mQueryTwoBtn;
    @BindView(R.id.queryMoreBtn)
    Button mQueryMoreBtn;
    @BindView(R.id.btn_basicDownload)
    Button mBtn_basicDownload;
    @BindView(R.id.btn_sqlcipher)
    Button mBtn_sqlcipher;
    @BindView(R.id.btn_log)
    Button mBtn_log;
    @BindView(R.id.btn_socket)
    Button mBtn_socket;
    private Button btn;
    private Button insertBtn;
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
        btn = (Button) findViewById(R.id.btn);
        insertBtn = (Button) findViewById(R.id.btn1);
        tv = (TextView) findViewById(R.id.tv);
        //        final Intent intent = new Intent();
        //        intent.setComponent(new ComponentName("com.echase.cashier", "com.echase.cashier.view.ac.AbcTestActivity"));
        //        intent.putExtra("appName", "Cashier");
        //        intent.putExtra("transId", "农行");
        //        JSONObject jsonObject = new JSONObject();
        //
        //        try {
        //            jsonObject.put("tran","000");
        //            intent.putExtra("transData",jsonObject.toString());
        //        } catch (Exception e) {
        //
        //        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("");
                try {
                    //                    Call<Object> call = ApiManage.getInstance().getApiService().getHttps();
                    Call<Object> call = mService.getHttps();
                    //                    Request request = call.request();
                    call.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {

                            //可处理结果直接传值给页面，接口回掉或者其他方式都行
                            String String = null;
                            if (response.isSuccessful()) {
                                try {
                                    String = response.body() + "";
                                    tv.setText(String);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    showTip("Exception");
                                }
                            } else {
                                showTip("response.isnotSuccessful()");
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable throwable) {
                            throwable.printStackTrace();
                            Toast.makeText(MainActivity.this, "onFailure", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    showTip("onClick Exception");
                }

            }
        });

        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBManager.getInstance().insert();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 2:
                tv.setText("交易成功");
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showTip(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.btn_delete, R.id.btnUpdate, R.id.queryBtn, R.id.queryTwoBtn, R.id.queryMoreBtn, R.id.btn_basicDownload,
            R.id.btn_sqlcipher,R.id.btn_log,R.id.btn_socket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                DBManager.getInstance().delete();
                break;
            case R.id.btnUpdate:
                DBManager.getInstance().update();
                break;
            case R.id.queryBtn:
                DBManager.getInstance().query();
                break;
            case R.id.queryTwoBtn:
                DBManager.getInstance().queryTwo();
                break;
            case R.id.queryMoreBtn:
                DBManager.getInstance().queryMore();
                break;
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
        }

    }
}
