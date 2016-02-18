package org.zarroboogs.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;

/**
 * Created by wangdiyuan on 16-2-17.
 */
public abstract class AsyncHttpResponseProgressHandler extends AsyncHttpResponseHandler {

    protected static final int UPDATE_MESSAGE = 0x0003;


    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case UPDATE_MESSAGE: {
                long[] objects = (long[]) msg.obj;
                onUpdate(objects[0], objects[1]);
                break;
            }
        }
    }

    public abstract void onUpdate(long bytesRead, long contentLength);

    final public void sendUpdateMessage(long bytesRead, long contentLength) {
        handler.sendMessage(obtainMessage(UPDATE_MESSAGE, new long[]{bytesRead,contentLength}));
    }

}
