package org.zarroboogs.http;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wangdiyuan on 16-2-17.
 */
public class AsyncHttpRequest {
    private final OkHttpClient mOkHttpClient = new OkHttpClient();


    public void post(String url,Map<String, String> headers, Map<String , String> formData,final AsyncHttpResponseHandler responseHandler){
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        // 设置 headers
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            Set<String> headerKeys = headers.keySet();
            for (String key : headerKeys) {
                headersBuilder.add(key, headers.get(key));
            }
            requestBuilder.headers(headersBuilder.build());
        }
        // 设置RequestBody
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (formData != null){
            Set<String> postParamKeys = formData.keySet();
            for (String key : postParamKeys) {
                formBodyBuilder.add(key, formData.get(key));
            }
        }
        requestBuilder.post(formBodyBuilder.build());
        Request request = requestBuilder.build();
        executeRequest(responseHandler, request);
    }

    public void post(String url, AsyncHttpPostString strContent, final AsyncHttpResponseHandler responseHandler){
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(RequestBody.create(MediaType.parse(strContent.getContentType()), strContent.getContent()));
        Request request = requestBuilder.build();
        executeRequest(responseHandler, request);
    }


    public void get(String url, final AsyncHttpResponseHandler responseHandler) {
        get(url, null, responseHandler);
    }


    public void get(String url, Map<String, String> headers, final AsyncHttpResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();

        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            Set<String> headerKeys = headers.keySet();
            for (String key : headerKeys) {
                headersBuilder.add(key, headers.get(key));
            }
            requestBuilder.headers(headersBuilder.build());
        }

        requestBuilder.url(url);

        Request request = requestBuilder.build();
        executeRequest(responseHandler, request);

    }


    private void executeRequest(final AsyncHttpResponseHandler responseHandler, Request request) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (responseHandler != null) {
                    responseHandler.sendFailureMessage(e);

                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (responseHandler != null) {
                    AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse();
                    try {
                        asyncHttpResponse.setBody(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    asyncHttpResponse.setCode(response.code());
                    asyncHttpResponse.setHeaders(response.headers().toString());

                    responseHandler.sendSuccessMessage(asyncHttpResponse);
                }
            }
        });
    }
}
