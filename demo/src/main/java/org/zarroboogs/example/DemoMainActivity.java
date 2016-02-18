package org.zarroboogs.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.zarroboogs.http.AsyncHttpPostString;
import org.zarroboogs.http.AsyncHttpRequest;
import org.zarroboogs.http.AsyncHttpResponse;
import org.zarroboogs.http.AsyncHttpResponseHandler;
import org.zarroboogs.http.AsyncHttpResponseProgressHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DemoMainActivity extends AppCompatActivity {

    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.show_http_response);
        final AsyncHttpRequest request = new AsyncHttpRequest();

        request.get("http://www.aqtxt.com/", new AsyncHttpResponseProgressHandler() {
            @Override
            public void onUpdate(long bytesRead, long contentLength) {
                mTextView.setText("" + bytesRead + " / " +contentLength);
                Log.d("MyUpdateLoad", "update");
            }

            @Override
            public void onFailure(IOException e) {
                Log.d("MyUpdateLoad", "onFailure");

            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.d("MyUpdateLoad", "onSuccess");

            }
        });



//        request.get("http://www.baidu.com", new AsyncHttpResponseHandler() {
//            @Override
//            public void onFailure(IOException e) {
//                mTextView.setText(e.toString());
//            }
//
//
//            @Override
//            public void onSuccess(AsyncHttpResponse response) {
//                mTextView.setText(response.getBody());
//            }
//        });

//
//        request.post("https://api.github.com/markdown/raw", new AsyncHttpPostString("text/x-markdown; charset=utf-8", "test"), new AsyncHttpResponseHandler() {
//            @Override
//            public void onFailure(IOException e) {
//                mTextView.setText(e.toString());
//            }
//
//            @Override
//            public void onSuccess(AsyncHttpResponse response) {
//                mTextView.setText(response.getBody());
//            }
//        });

    }
}
