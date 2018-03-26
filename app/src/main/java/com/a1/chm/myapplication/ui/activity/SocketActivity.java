package com.a1.chm.myapplication.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dhh.websocket.RxWebSocketUtil;

import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

/**
 * @author chm on 2018/1/4
 */


public class SocketActivity extends AppCompatActivity {

    private Disposable mDisposable;
    private WebSocket mWebSocket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // url是下载的URL地址。
        String url="";
//        AsyncHttpClient.getDefaultInstance().getString(url, new AsyncHttpClient.StringCallback() {
//            // 如果可能，任何异常错误、结果都会请求回调
//            @Override
//            public void onCompleted(Exception e, AsyncHttpResponse response, String result) {
//                if (e != null) {
//                    e.printStackTrace();
//                    return;
//                }
//                System.out.println("I got a string: " + result);
//            }
//        });
//        Uri uri=Uri.parse(url);
//        AsyncHttpRequest request=new AsyncHttpRequest(Uri.parse(url),"");
//        AsyncHttpClient.getDefaultInstance().executeString(new AsyncHttpRequest(Uri.parse(url),""), new AsyncHttpClient.StringCallback() {
//            @Override
//            public void onCompleted(Exception e, AsyncHttpResponse asyncHttpResponse, String s) {
//
//            }
//        });

        //if you want to use your okhttpClient
        OkHttpClient yourClient = new OkHttpClient();
        RxWebSocketUtil.getInstance().setClient(yourClient);
        // show log,default false
        RxWebSocketUtil.getInstance().setShowLog(true);


//        mDisposable = RxWebSocketUtil.getInstance().getWebSocketInfo(url)
//                //bind on life
//                .takeUntil(bindOndestroy())
//                .subscribe(new Consumer<WebSocketInfo>() {
//                    @Override
//                    public void accept(WebSocketInfo webSocketInfo) throws Exception {
//                        mWebSocket = webSocketInfo.getWebSocket();
//                        if (webSocketInfo.isOnOpen()) {
//                            Log.d("MainActivity", " on WebSocket open");
//                        } else {
//
//                            String string = webSocketInfo.getString();
//                            if (string != null) {
//                                Log.d("MainActivity", string);
////                                textview.setText(Html.fromHtml(string));
//
//                            }
//
//                            ByteString byteString = webSocketInfo.getByteString();
//                            if (byteString != null) {
//                                Log.d("MainActivity", "webSocketInfo.getByteString():" + byteString);
//
//                            }
//                        }
//
//                    }
//                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
