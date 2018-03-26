package com.a1.chm.myapplication.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.a1.chm.myapplication.R;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chm on 2017/12/11
 */

public class LogActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ButterKnife.bind(this);
        try{
            InputStream is = getAssets().open("threadDump-20171218-165755.txt");
//            InputStream is = getAssets().open("idea.log");
            int lenght = is.available();
            byte[]  buffer = new byte[lenght];
            is.read(buffer);
//            String result = new String(buffer, "utf8");
            mTv.setText(new String(buffer, "utf8"));
            is.close();
        }catch(Exception e){
            e.printStackTrace();
            mTv.setText("Exception");
        }

    }


}
