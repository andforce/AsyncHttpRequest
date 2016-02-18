# AsyncHttpRequest
封装了OkHttp,可直接再异步回调中更新UI

## 使用方法
#### GET
``` java
        final AsyncHttpRequest request = new AsyncHttpRequest();
        request.get("http://www.baidu.com", new AsyncHttpResponseHandler() {
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
#### POST
``` java
        // 构建Headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Connection", "keep-alive");
        // 构建Form Data
        Map<String , String> formData = new HashMap<>();
        formData.put("username","andforce");

        request.post("http://www.example.com", headers, formData, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                // 更新UI
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                // 更新UI
            }
        });
```
