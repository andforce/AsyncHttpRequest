package org.zarroboogs.http.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.andforce.javademolib.MyClass;

import org.zarroboogs.http.AsyncHttpResponse;
import org.zarroboogs.http.AsyncOkHttpClient;
import org.zarroboogs.http.AsyncResponseHandler;
import org.zarroboogs.http.post.AsyncHttpPostFormData;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.show_http_response);

        mTextView.setText(MyClass.getMyString());

        AsyncOkHttpClient request = new AsyncOkHttpClient();
        AsyncHttpPostFormData formData = new AsyncHttpPostFormData();
        formData.addFormData("search", "Jurassic Park");
        request.post("https://en.wikipedia.org/w/index.php", null, formData, new AsyncResponseHandler(new AsyncResponseHandler.OnProgressListener() {
            @Override
            public void onProgress(long bytesRead, long contentLength) {
                mTextView.setText("" + bytesRead + "  " + contentLength);
            }
        }) {

            @Override
            public void onFailure(IOException e) {
                mTextView.setText(e.getLocalizedMessage());
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                mTextView.setText(response.getBody());
            }
        });

        //        request.get("http://www.aqtxt.com/", new AsyncResponseHandler() {
//
//            @Override
//            public void onFailure(IOException e) {
//                Log.d("MyUpdateLoad", "onFailure");
//
//            }
//
//            @Override
//            public void onSuccess(AsyncHttpResponse response) {
//                Log.d("MyUpdateLoad", "onSuccess");
//
//            }
//        });
//
//
//        request.get("http://www.baidu.com", new AsyncResponseHandler() {
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
//
//        AsyncOkHttpClient request = new AsyncOkHttpClient();
//        request.post("https://api.github.com/markdown/raw", new AsyncHttpPostString("text/x-markdown; charset=utf-8", "test"), new AsyncResponseHandler() {
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
//
//        AsyncOkHttpClient request = new AsyncOkHttpClient();
//        // 构建Headers
//        AsyncHttpPostFormData formData = new AsyncHttpPostFormData();
//        formData.addFormData("search", "Jurassic Park");
//        request.post("https://en.wikipedia.org/w/index.php", null, formData, new AsyncResponseHandler() {
//
//            @Override
//            public void onFailure(IOException e) {
//                mTextView.setText(e.getLocalizedMessage());
//            }
//
//            @Override
//            public void onSuccess(AsyncHttpResponse response) {
//                mTextView.setText(response.getBody());
//            }
//        });

//        AsyncOkHttpClient request = new AsyncOkHttpClient();
//        File uploadFile = new File("your file's path");
//        AsyncHttpPostFile postFile = new AsyncHttpPostFile("application/octet-stream", uploadFile);
//        request.post(url, httpHeaders, postFile, new AsyncResponseHandler() {
//            @Override
//            public void onFailure(IOException e) {
//
//            }
//
//            @Override
//            public void onSuccess(AsyncHttpResponse response) {
//
//            }
//        });
    }
}
