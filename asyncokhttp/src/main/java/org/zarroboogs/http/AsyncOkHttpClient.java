package org.zarroboogs.http;

import org.zarroboogs.http.post.AsyncHttpPostFile;
import org.zarroboogs.http.post.AsyncHttpPostFormData;
import org.zarroboogs.http.post.AsyncHttpPostString;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

import java.net.HttpCookie;

import static okhttp3.internal.platform.Platform.WARN;
import static okhttp3.internal.Util.delimiterOffset;
import static okhttp3.internal.Util.trimSubstring;

/**
 * Created by wangdiyuan on 16-2-17.
 */
public class AsyncOkHttpClient {
    private OkHttpClient mOkHttpClient;

    public AsyncOkHttpClient() {
        CookieManager cookieManager = new CookieManager();
        mOkHttpClient = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(cookieManager)).build();
    }

    public void post(String url, Headers headers, AsyncHttpPostFormData formData, final AsyncResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        // 设置 headers
        okhttp3.Headers.Builder headersBuilder = new okhttp3.Headers.Builder();
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
            Map<String, String> formDataMap = formData.getFormData();
            Set<String> postParamKeys = formDataMap.keySet();
            for (String key : postParamKeys) {
                formBodyBuilder.add(key, formDataMap.get(key));
            }
        }
        requestBuilder.post(formBodyBuilder.build());
        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);
    }

    public void post(String url, AsyncHttpPostString postString, final AsyncResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(RequestBody.create(MediaType.parse(postString.getContentType()), postString.getContent()));
        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);
    }

    public void post(String url, AsyncHttpPostFile postBody, final AsyncResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(RequestBody.create(MediaType.parse(postBody.getContentType()), postBody.getContent()));

        Request request = requestBuilder.build();
        executeRequest(mOkHttpClient, request, responseHandler);
    }

    public void post(String url, Headers headers, AsyncHttpPostFile postBody, final AsyncResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(RequestBody.create(MediaType.parse(postBody.getContentType()), postBody.getContent()));
        // 设置 headers
        okhttp3.Headers.Builder headersBuilder = new okhttp3.Headers.Builder();
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


    public void get(String url, final AsyncResponseHandler responseHandler) {
        get(url, null, responseHandler);
    }


    public void get(String url, Headers headers, final AsyncResponseHandler responseHandler) {
        Request.Builder requestBuilder = new Request.Builder();

        okhttp3.Headers.Builder headersBuilder = new okhttp3.Headers.Builder();
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

    public OkHttpClient buildClient(OkHttpClient httpClient, AsyncResponseHandler responseProgressHandler) {

        if (!responseProgressHandler.isProgressListenerEmpty()) {
            final AsyncHttpProgressListener listener = new AsyncHttpProgressListener(responseProgressHandler);
            OkHttpClient progressHttpClient = httpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());

                    return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), listener)).build();
                }
            }).build();
            return progressHttpClient;
        } else {
            return httpClient;
        }
    }

    private void executeRequest(OkHttpClient httpClient, Request request, final AsyncResponseHandler responseProgressHandler) {


        OkHttpClient client = buildClient(httpClient, responseProgressHandler);


        client.newCall(request).enqueue(new Callback() {
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
        private AsyncResponseHandler responseProgressHandler;

        public AsyncHttpProgressListener(AsyncResponseHandler httpResponseProgressHandler) {
            this.responseProgressHandler = httpResponseProgressHandler;
        }

        public void update(long bytesRead, long contentLength, boolean done) {
            if (responseProgressHandler != null) {
                responseProgressHandler.sendUpdateMessage(bytesRead, contentLength);
            }
        }
    }

    /**
     * A cookie jar that delegates to a {@link java.net.CookieHandler}.
     */
    public final class JavaNetCookieJar implements CookieJar {
        private final CookieHandler cookieHandler;

        public JavaNetCookieJar(CookieHandler cookieHandler) {
            this.cookieHandler = cookieHandler;
        }

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookieHandler != null) {
                List<String> cookieStrings = new ArrayList<>();
                for (Cookie cookie : cookies) {
                    cookieStrings.add(cookie.toString());
                }
                Map<String, List<String>> multimap = Collections.singletonMap("Set-Cookie", cookieStrings);
                try {
                    cookieHandler.put(url.uri(), multimap);
                } catch (IOException e) {
                    Platform.get().log(WARN, "Saving cookies failed for " + url.resolve("/..."), e);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            // The RI passes all headers. We don't have 'em, so we don't pass 'em!
            Map<String, List<String>> headers = Collections.emptyMap();
            Map<String, List<String>> cookieHeaders;
            try {
                cookieHeaders = cookieHandler.get(url.uri(), headers);
            } catch (IOException e) {
                Platform.get().log(WARN, "Loading cookies failed for " + url.resolve("/..."), e);
                return Collections.emptyList();
            }

            List<Cookie> cookies = null;
            for (Map.Entry<String, List<String>> entry : cookieHeaders.entrySet()) {
                String key = entry.getKey();
                if (("Cookie".equalsIgnoreCase(key) || "Cookie2".equalsIgnoreCase(key))
                        && !entry.getValue().isEmpty()) {
                    for (String header : entry.getValue()) {
                        if (cookies == null) cookies = new ArrayList<>();
                        cookies.addAll(decodeHeaderAsJavaNetCookies(url, header));
                    }
                }
            }

            return cookies != null
                    ? Collections.unmodifiableList(cookies)
                    : Collections.<Cookie>emptyList();
        }

        /**
         * Convert a request header to OkHttp's cookies via {@link HttpCookie}. That extra step handles
         * multiple cookies in a single request header, which {@link Cookie#parse} doesn't support.
         */
        private List<Cookie> decodeHeaderAsJavaNetCookies(HttpUrl url, String header) {
            List<Cookie> result = new ArrayList<>();
            for (int pos = 0, limit = header.length(), pairEnd; pos < limit; pos = pairEnd + 1) {
                pairEnd = delimiterOffset(header, pos, limit, ";,");
                int equalsSign = delimiterOffset(header, pos, pairEnd, '=');
                String name = trimSubstring(header, pos, equalsSign);
                if (name.startsWith("$")) continue;

                // We have either name=value or just a name.
                String value = equalsSign < pairEnd
                        ? trimSubstring(header, equalsSign + 1, pairEnd)
                        : "";

                // If the value is "quoted", drop the quotes.
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }

                result.add(new Cookie.Builder()
                        .name(name)
                        .value(value)
                        .domain(url.host())
                        .build());
            }
            return result;
        }
    }
}
