package org.zarroboogs.http.post;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangdiyuan on 16-2-19.
 */
public final class AsyncHttpPostFormData {
    private Map<String, String> mFormData = new HashMap<>();

    public AsyncHttpPostFormData addFormData(String headerName, String headerValue){
        mFormData.put(headerName, headerValue);
        return this;
    }

    public Map<String ,String> getFormData(){
        return mFormData;
    }
}
