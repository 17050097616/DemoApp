package com.a1.chm.myapplication.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.a1.chm.myapplication.R;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author chm on 2017/12/11
 */

//SQLCipher加密已有数据库的方法就只有拷贝表到新建加密数据库,源数据库没有被加密
public class SqlcipherActivity extends AppCompatActivity {

    private final String SDcardPath = "/mnt/sdcard/";
    @BindView(R.id.btn_encry)
    Button mBtnEncry;
    @BindView(R.id.btn_decry)
    Button mBtnDecry;
    @BindView(R.id.btn_testOpen)
    Button mBtnTestOpen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlcipher);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_encry, R.id.btn_decry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_encry:
                encrypt("encryptedtest.db", "test.db", "1234");
                break;
            case R.id.btn_decry:
                decrypt("test.db", "decryptedtest.db", "1234");
                break;
        }
    }

    /**
     * 加密数据库
     *
     * @param encryptedName 加密后的数据库名称
     * @param decryptedName 要加密的数据库名称
     * @param key           密码
     */
    private void encrypt(String encryptedName, String decryptedName, String key) {
        try {
            //            File databaseFile = getDatabasePath(SDcardPath + decryptedName);
            File databaseFile = getDatabasePath(decryptedName);
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "", null);//打开要加密的数据库

            /*String passwordString = "1234"; //只能对已加密的数据库修改密码，且无法直接修改为“”或null的密码
            database.changePassword(passwordString.toCharArray());*/

            File encrypteddatabaseFile = getDatabasePath(SDcardPath + encryptedName);//新建加密后的数据库文件
            //deleteDatabase(SDcardPath + encryptedName);

            //连接到加密后的数据库，并设置密码
            database.rawExecSQL(String.format("ATTACH DATABASE '%s' as " + encryptedName.split("\\.")[0] + " KEY '" + key + "';", encrypteddatabaseFile.getAbsolutePath()));
            //输出要加密的数据库表和数据到加密后的数据库文件中
            database.rawExecSQL("SELECT sqlcipher_export('" + encryptedName.split("\\.")[0] + "');");
            //断开同加密后的数据库的连接
            database.rawExecSQL("DETACH DATABASE " + encryptedName.split("\\.")[0] + ";");

            //打开加密后的数据库，测试数据库是否加密成功
            SQLiteDatabase encrypteddatabase = SQLiteDatabase.openOrCreateDatabase(encrypteddatabaseFile, key, null);
            Log.w(SqlcipherActivity.class.getSimpleName(), "encrypt: success");
            //encrypteddatabase.setVersion(database.getVersion());
            encrypteddatabase.close();//关闭数据库

            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解密数据库
     *
     * @param encryptedName 要解密的数据库名称
     * @param decryptedName 解密后的数据库名称
     * @param key           密码
     */
    private void decrypt(String encryptedName, String decryptedName, String key) {
        try {
            File databaseFile;
            if (encryptedName.equals("test.db")) {
                databaseFile = getDatabasePath(encryptedName);
            } else
                databaseFile = getDatabasePath(SDcardPath + encryptedName);
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFile, key, null);//打开要解密的数据库

            File decrypteddatabaseFile = getDatabasePath(SDcardPath + decryptedName);//新建解密后的数据库文件
            //deleteDatabase(SDcardPath + decryptedName);

            //连接到解密后的数据库，并设置密码为空
            database.rawExecSQL(String.format("ATTACH DATABASE '%s' as " + decryptedName.split("\\.")[0] + " KEY '';", decrypteddatabaseFile.getAbsolutePath()));
            database.rawExecSQL("SELECT sqlcipher_export('" + decryptedName.split("\\.")[0] + "');");
            //输出要解密的数据库表和数据到解密后的数据库文件中
            database.rawExecSQL("DETACH DATABASE " + decryptedName.split("\\.")[0] + ";");
            //断开同解密后的数据库的连接
            SQLiteDatabase decrypteddatabase = SQLiteDatabase.openOrCreateDatabase(decrypteddatabaseFile, "", null);
            //打开解密后的数据库，测试数据库是否解密成功
            //decrypteddatabase.setVersion(database.getVersion());
            decrypteddatabase.close();

            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_testOpen)
    public void onViewClicked() {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(getDatabasePath("test.db"), "", null);

    }
}
