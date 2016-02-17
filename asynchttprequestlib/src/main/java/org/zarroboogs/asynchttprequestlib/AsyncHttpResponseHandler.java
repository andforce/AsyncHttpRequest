package org.zarroboogs.asynchttprequestlib;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;

/**
 * Created by wangdiyuan on 16-2-17.
 */
public abstract class AsyncHttpResponseHandler {

    private static final int SUCCESS_MESSAGE = 0x0001;
    private static final int FAILURE_MESSAGE = 0x0002;
    private Handler handler;

    public AsyncHttpResponseHandler() {
        handler = new MyHandler(this, Looper.getMainLooper());
    }


    private static class MyHandler extends Handler {
        private final AsyncHttpResponseHandler mResponder;

        MyHandler(AsyncHttpResponseHandler mResponder, Looper looper) {
            super(looper);
            this.mResponder = mResponder;
        }

        @Override
        public void handleMessage(Message msg) {
            mResponder.handleMessage(msg);
        }
    }

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case SUCCESS_MESSAGE: {
                AsyncHttpResponse response = (AsyncHttpResponse) msg.obj;
                onSuccess(response);
                break;
            }
            case FAILURE_MESSAGE: {

                IOException e = (IOException) msg.obj;
                onFailure(e);
                break;
            }
        }
    }

    public abstract void onFailure(IOException e);

    public abstract void onSuccess(AsyncHttpResponse response);

    private Message obtainMessage(int responseMessageId, Object responseMessageData) {
        return Message.obtain(handler, responseMessageId, responseMessageData);
    }

    final public void sendSuccessMessage(AsyncHttpResponse response) {
        handler.sendMessage(obtainMessage(SUCCESS_MESSAGE, response));
    }

    final public void sendFailureMessage(IOException e) {
        handler.sendMessage(obtainMessage(FAILURE_MESSAGE, e));
    }
}
