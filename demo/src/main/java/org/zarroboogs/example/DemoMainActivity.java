package org.zarroboogs.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.zarroboogs.http.AsyncHttpRequest;
import org.zarroboogs.http.AsyncHttpResponse;
import org.zarroboogs.http.AsyncHttpResponseHandler;

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
        request.get("http://www.baidu.com", new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mTextView.setText(e.toString());
            }


            @Override
            public void onSuccess(AsyncHttpResponse response) {
                mTextView.setText(response.getBody());
            }
        });

        // 构建Headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Connection", "keep-alive");
        // 构建Form Data
        Map<String , String> formData = new HashMap<>();
        formData.put("username","andforce");

        request.post("http://www.example.com", headers, formData, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                // 更新UI
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                // 更新UI
            }
        });

    }
}
