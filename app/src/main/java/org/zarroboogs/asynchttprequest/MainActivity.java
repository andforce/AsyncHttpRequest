package org.zarroboogs.asynchttprequest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.zarroboogs.asynchttprequestlib.AsyncHttpRequest;
import org.zarroboogs.asynchttprequestlib.AsyncHttpResponse;
import org.zarroboogs.asynchttprequestlib.AsyncHttpResponseHandler;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

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

    }
}
