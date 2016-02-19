package org.zarroboogs.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangdiyuan on 16-2-19.
 */
public final class AsyncHttpHeaders {
    private Map<String, String> mHeaders = new HashMap<>();

    public AsyncHttpHeaders addHost(String host) {
        mHeaders.put("Host", host);
        return this;
    }

    public AsyncHttpHeaders addReferer(String referer) {
        mHeaders.put("Referer", referer);
        return this;
    }

    public AsyncHttpHeaders addOrigin(String origin) {
        mHeaders.put("Origin", origin);
        return this;
    }

    public AsyncHttpHeaders addAccept(String accept) {
        mHeaders.put("Accept", accept);
        return this;
    }

    public AsyncHttpHeaders addUserAgent(String userAgent) {
        mHeaders.put("User-Agent", userAgent);
        return this;
    }

    public AsyncHttpHeaders addAcceptEncoding(String acceptEncoding) {
        mHeaders.put("Accept-Encoding", acceptEncoding);
        return this;
    }

    public AsyncHttpHeaders addAcceptLanguage(String acceptLanguage) {
        mHeaders.put("Accept-Language", acceptLanguage);
        return this;
    }

    public AsyncHttpHeaders addCookie(String cookie) {
        mHeaders.put("Cookie", cookie);
        return this;
    }

    public AsyncHttpHeaders addConnection(String connection) {
        mHeaders.put("Connection", connection);
        return this;
    }

    public AsyncHttpHeaders addContentType(String contentType) {
        mHeaders.put("Content-Type", contentType);
        return this;
    }

    public AsyncHttpHeaders add(String key, String value) {
        mHeaders.put(key, value);
        return this;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }
}
