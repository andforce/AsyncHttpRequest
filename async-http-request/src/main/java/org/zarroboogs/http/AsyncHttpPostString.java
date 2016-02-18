package org.zarroboogs.http;

/**
 * Created by wangdiyuan on 16-2-18.
 */
public class AsyncHttpPostString {
    private String contentType;
    private String content;

    public AsyncHttpPostString(String contentType, String content) {
        this.contentType = contentType;
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
