package org.zarroboogs.http;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by wangdiyuan on 16-2-17.
 */
public class AsyncHttpRequest {
    private final OkHttpClient mOkHttpClient = new OkHttpClient();


    public void post(String url, AsyncHttpHeaders headers, Map<String, String> formData, final AsyncHttpResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        // 设置 headers
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            Map<String, String> headersMap = headers.getHeaders();
            Set<String> headerKeys = headersMap.keySet();
            for (String key : headerKeys) {
                headersBuilder.add(key, headersMap.get(key));
            }
            requestBuilder.headers(headersBuilder.build());
        }
        // 设置RequestBody
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (formData != null) {
            Set<String> postParamKeys = formData.keySet();
            for (String key : postParamKeys) {
                formBodyBuilder.add(key, formData.get(key));
            }
        }
        requestBuilder.post(formBodyBuilder.build());
        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);
    }

    public void post(String url, AsyncHttpPostString postString, final AsyncHttpResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(RequestBody.create(MediaType.parse(postString.getContentType()), postString.getContent()));
        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);
    }

    public void post(String url, AsyncHttpPostFile postBody, final AsyncHttpResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(RequestBody.create(MediaType.parse(postBody.getContentType()), postBody.getContent()));

        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);
    }

    public void post(String url, AsyncHttpHeaders headers, AsyncHttpPostFile postBody, final AsyncHttpResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(RequestBody.create(MediaType.parse(postBody.getContentType()), postBody.getContent()));
        // 设置 headers
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            Map<String, String> headersMap = headers.getHeaders();
            Set<String> headerKeys = headersMap.keySet();
            for (String key : headerKeys) {
                headersBuilder.add(key, headersMap.get(key));
            }
            requestBuilder.headers(headersBuilder.build());
        }

        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);
    }

    //
    public void post(String url, AsyncHttpHeaders headers, Map<String, String> formData, AsyncHttpResponseProgressHandler responseProgressHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        // 设置 headers
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            Map<String, String> headersMap = headers.getHeaders();
            Set<String> headerKeys = headersMap.keySet();
            for (String key : headerKeys) {
                headersBuilder.add(key, headersMap.get(key));
            }
            requestBuilder.headers(headersBuilder.build());
        }
        // 设置RequestBody
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (formData != null) {
            Set<String> postParamKeys = formData.keySet();
            for (String key : postParamKeys) {
                formBodyBuilder.add(key, formData.get(key));
            }
        }
        requestBuilder.post(formBodyBuilder.build());
        Request request = requestBuilder.build();
        executeProgressRequest(mOkHttpClient, request, responseProgressHandler);
    }

    public void post(String url, AsyncHttpPostString postString, AsyncHttpResponseProgressHandler responseProgressHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(RequestBody.create(MediaType.parse(postString.getContentType()), postString.getContent()));
        Request request = requestBuilder.build();
        executeProgressRequest(mOkHttpClient, request, responseProgressHandler);
    }

    public void post(String url, AsyncHttpPostFile postBody, AsyncHttpResponseProgressHandler responseProgressHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(RequestBody.create(MediaType.parse(postBody.getContentType()), postBody.getContent()));
        Request request = requestBuilder.build();
        executeProgressRequest(mOkHttpClient, request, responseProgressHandler);
    }
    //


    public void get(String url, final AsyncHttpResponseHandler responseHandler) {
        get(url, null, responseHandler);
    }


    public void get(String url, AsyncHttpHeaders headers, final AsyncHttpResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();

        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            Map<String, String> headersMap = headers.getHeaders();
            Set<String> headerKeys = headersMap.keySet();
            for (String key : headerKeys) {
                headersBuilder.add(key, headersMap.get(key));
            }
            requestBuilder.headers(headersBuilder.build());
        }

        requestBuilder.url(url);

        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);

    }

    public void get(String url, final AsyncHttpResponseProgressHandler responseProgressHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        Request request = requestBuilder.build();

        executeProgressRequest(mOkHttpClient, request, responseProgressHandler);
    }

    public void get(String url, AsyncHttpHeaders headers, final AsyncHttpResponseProgressHandler responseProgressHandler) {
        Request.Builder requestBuilder = new Request.Builder();

        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null) {
            Map<String, String> headersMap = headers.getHeaders();
            Set<String> headerKeys = headersMap.keySet();
            for (String key : headerKeys) {
                headersBuilder.add(key, headersMap.get(key));
            }
            requestBuilder.headers(headersBuilder.build());
        }

        requestBuilder.url(url);

        Request request = requestBuilder.build();
        executeProgressRequest(mOkHttpClient, request, responseProgressHandler);

    }

    private void executeRequest(OkHttpClient httpClient, Request request, final AsyncHttpResponseHandler responseHandler) {
        Call call = httpClient.newCall(request);
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


    private void executeProgressRequest(OkHttpClient httpClient, Request request, final AsyncHttpResponseProgressHandler responseProgressHandler) {
        final AsyncHttpProgressListener listener = new AsyncHttpProgressListener(responseProgressHandler);

        OkHttpClient progressHttpClient = httpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());

                return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), listener)).build();
            }
        }).build();


        progressHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (responseProgressHandler != null) {
                    responseProgressHandler.sendFailureMessage(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (responseProgressHandler != null) {
                    AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse();
                    try {
                        asyncHttpResponse.setBody(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    asyncHttpResponse.setCode(response.code());
                    asyncHttpResponse.setHeaders(response.headers().toString());

                    responseProgressHandler.sendSuccessMessage(asyncHttpResponse);
                }
            }
        });
    }


    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final AsyncHttpProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, AsyncHttpProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

    private class AsyncHttpProgressListener {
        private AsyncHttpResponseProgressHandler responseProgressHandler;

        public AsyncHttpProgressListener(AsyncHttpResponseProgressHandler httpResponseProgressHandler) {
            this.responseProgressHandler = httpResponseProgressHandler;
        }

        public void update(long bytesRead, long contentLength, boolean done) {
            if (responseProgressHandler != null) {
                responseProgressHandler.sendUpdateMessage(bytesRead, contentLength);
            }
        }
    }
}
