package org.zarroboogs.asynchttprequest;

/**
 * Created by wangdiyuan on 16-2-17.
 */
public class AsyncHttpResponse {
    private int code;
    private String headers;
    private String body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
