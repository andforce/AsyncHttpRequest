## 为什么要封装这个库
1. 我之前一直使用https://github.com/loopj/android-async-http 这个库,它对Apache的httpclient进行了异步封装,
可以很方便的进行http的请求操作.然而Google从Android5.0开始就不建议使用Apache的Httpcleint了,因而逐渐转向了OkHttp这个库.
2. OkHttp虽然也能进行异步请求,可Callback不是UI线程内,因此不能在Callback中直接更新UI,使用起来有点不方便.

##在Gradle中使用
###For jcenter
```
dependencies {
	    compile 'org.zarroboogs.http:asyncokhttp:0.1.9'
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
	    compile 'com.github.andforce:asyncokhttp:0.1.9'
}
```

## API使用方法
#### GET
``` java
        AsyncOkHttpClient request = new AsyncOkHttpClient();
        request.get("http://www.baidu.com", new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mTextView.setText(e.toString());
            }


            @Override
            public void onSuccess(AsyncHttpResponse response) {
                mTextView.setText(response.getBody());
            }
        });
```

#### GET 带进度
``` java
        AsyncOkHttpClient request = new AsyncOkHttpClient();
        AsyncHttpPostFormData formData = new AsyncHttpPostFormData();
        formData.addFormData("search", "Jurassic Park");
        request.post("https://en.wikipedia.org/w/index.php", null, formData, new AsyncResponseHandler(new AsyncResponseHandler.OnProgressListener() {
            @Override
            public void onProgress(long bytesRead, long contentLength) {
                mTextView.setText("" + bytesRead + "  " + contentLength);
            }
        }) {

            @Override
            public void onFailure(IOException e) {
                mTextView.setText(e.getLocalizedMessage());
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                mTextView.setText(response.getBody());
            }
        });
```



#### POST
##### POST
``` java
        AsyncOkHttpClient request = new AsyncOkHttpClient();
        request.post("https://api.github.com/markdown/raw", new AsyncHttpPostString("text/x-markdown; charset=utf-8", "test"), new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mTextView.setText(e.toString());
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                mTextView.setText(response.getBody());
            }
        });
```

##### POST FormData
``` java
        AsyncOkHttpClient request = new AsyncOkHttpClient();
        AsyncHttpPostFormData formData = new AsyncHttpPostFormData();
        formData.addFormData("search", "Jurassic Park");
        request.post("https://en.wikipedia.org/w/index.php", null, formData, new AsyncResponseHandler(new AsyncResponseHandler.OnProgressListener() {
            @Override
            public void onProgress(long bytesRead, long contentLength) {
                mTextView.setText("" + bytesRead + "  " + contentLength);
            }
        }) {

            @Override
            public void onFailure(IOException e) {
                mTextView.setText(e.getLocalizedMessage());
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                mTextView.setText(response.getBody());
            }
        });
```

##### POST 上传文件
``` java
        AsyncOkHttpClient request = new AsyncOkHttpClient();
        File uploadFile = new File("your file's path");
        AsyncHttpPostFile postFile = new AsyncHttpPostFile("application/octet-stream", uploadFile);
        request.post(url, httpHeaders, postFile, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {

            }
        });
```
