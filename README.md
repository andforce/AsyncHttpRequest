## 为什么要封装这个库
1. 我之前一直使用https://github.com/loopj/android-async-http 这个库,它对Apache的httpclient进行了异步封装,
可以很方便的进行http的请求操作.然而Google从Android5.0开始就不建议使用Apache的Httpcleint了,因而逐渐转向了OkHttp这个库.
2. OkHttp虽然也能进行异步请求,可Callback不是UI线程内,因此不能在Callback中直接更新UI,使用起来有点不方便.

##在Gradle中使用
###For jcenter
```
dependencies {
	    compile 'org.zarroboogs.http:async-http-request:0.1.8'
}
```
###For jitpack
```
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
}

dependencies {
	    compile 'com.github.andforce:AsyncHttpRequest:0.1.8'
}
```

## API使用方法
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
