package org.zarroboogs.http.post;

import java.io.File;

/**
 * Created by wangdiyuan on 16-2-18.
 */
public class AsyncHttpPostFile {
    private String contentType;
    private File content;

    public AsyncHttpPostFile(String contentType, File content) {
        this.contentType = contentType;
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public File getContent() {
        return content;
    }

    public void setContent(File content) {
        this.content = content;
    }
}
