# AsyncHttpRequest
封装了OkHttp,可直接再异步回调中更新UI

## 使用方法
``` java
final AsyncHttpRequest request = new AsyncHttpRequest();
        request.get("http://www.example.com", new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                //此处更新UI
            }


            @Override
            public void onSuccess(AsyncHttpResponse response) {
                //此处更新UI
            }
        });
```
