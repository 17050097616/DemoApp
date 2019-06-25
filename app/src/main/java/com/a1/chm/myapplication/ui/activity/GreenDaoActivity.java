package com.a1.chm.myapplication.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.a1.chm.myapplication.R;
import com.a1.chm.myapplication.dao.DBManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author chm on 2017/12/11
 */

public class GreenDaoActivity extends AppCompatActivity {

    @BindView(R.id.insertBtn)
    Button insertBtn;
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
    @BindView(R.id.tv)
    TextView mTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greendao);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.insertBtn, R.id.btn_delete,R.id.btnUpdate, R.id.queryBtn, R.id.queryTwoBtn, R.id.queryMoreBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.insertBtn:
                DBManager.getInstance().insert();
                break;
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
        }

    }


}
