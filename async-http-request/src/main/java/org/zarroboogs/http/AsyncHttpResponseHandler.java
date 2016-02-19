package org.zarroboogs.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;

/**
 * Created by wangdiyuan on 16-2-17.
 */
public abstract class AsyncHttpResponseHandler {

    protected static final int SUCCESS_MESSAGE = 0x0001;
    protected static final int FAILURE_MESSAGE = 0x0002;
    protected static final int UPDATE_MESSAGE = 0x0003;

    protected Handler handler;

    private OnProgressListener mProgressListener;

    public AsyncHttpResponseHandler() {
        super();
        handler = new MyHandler(this, Looper.getMainLooper());
    }


    public AsyncHttpResponseHandler(OnProgressListener listener){
        super();
        handler = new MyHandler(this, Looper.getMainLooper());
        this.mProgressListener = listener;
    }

    public final boolean isProgressListenerEmpty(){
        return mProgressListener == null;
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

    protected void handleMessage(Message msg) {
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
            case UPDATE_MESSAGE: {
                if (!isProgressListenerEmpty()){
                    long[] objects = (long[]) msg.obj;
                    mProgressListener.onProgress(objects[0],objects[1]);
                }

                break;
            }
        }
    }

    public abstract void onFailure(IOException e);

    public abstract void onSuccess(AsyncHttpResponse response);

    protected Message obtainMessage(int responseMessageId, Object responseMessageData) {
        return Message.obtain(handler, responseMessageId, responseMessageData);
    }

    final public void sendSuccessMessage(AsyncHttpResponse response) {
        handler.sendMessage(obtainMessage(SUCCESS_MESSAGE, response));
    }

    final public void sendFailureMessage(IOException e) {
        handler.sendMessage(obtainMessage(FAILURE_MESSAGE, e));
    }

    final public void sendUpdateMessage(long bytesRead, long contentLength) {
        handler.sendMessage(obtainMessage(UPDATE_MESSAGE, new long[]{bytesRead,contentLength}));
    }

    public interface OnProgressListener{
        public void onProgress(long bytesRead, long contentLength);
    }
}
