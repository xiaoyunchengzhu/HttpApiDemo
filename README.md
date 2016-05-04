
       此基于okhttp。封装缓存，全局缓存设置，全局缓存过期时间设置，全局请求方式设置，同时也可以每一个接口对应一个缓存过期时间。
    支持文件的上传，下载。图片加载（已经压缩）。请求框架和底层请求库分离，可以随时变化请求底层方式，。比如未来出现了更优秀的请
    求库，那么okhttp就需要更新到这个优秀的请求库，那么调用者依然如此调用，只需变化底层方法，预加载的参数设置，请求的接口适配回
    调设置。统一不变的方法提供给调用者，变化的是底层的适配。
    
     使用：
     你自己的项目中。
     1、在module 的build.gradle中添加项目依赖：
      dependencies {
 
        compile 'com.xiaoyunchengzhu:httpapi:1.0.1'
 
     }
     2、在project的buid.gradle 中添加mavenLocal()
     allprojects {
    repositories {
        jcenter()
        mavenLocal()
      }
    }
     3、make project一下，就可以使用工具进行玩耍了。
     4、代码使用：
        (1)首先在组建的oncreate开始进行初始化。
          APIManager.getInstance().init(context);
        (2)就可以使用了，
        比如字符串的请求：
        param是参数，键值对方式，post表单。get也可以。默认get请求
         APIManager.createApi(new HttpApi(String url)).param("name","gege").execute(new StringCallBackResult() {
                    @Override
                    public void success(Api api, String result) {
                        //result便是请求的字符串结果。
                    }

                    @Override
                    public void failure(Api api, String error) {
                      
                    }
                });
     post请求：
    APIManager.createApi(new HttpApi(String url)).param("name", "gege").httpMode(HttpMode.post).execute(new StringCallBackResult() {
      @Override
      public void success(Api api, String result) {
         
        }

      @Override
      public void failure(Api api, String error) {
      
        }
    });
    支持缓存：
     APIManager.createApi(new HttpApi(String url)).cacheMode(CacheMode.is_cache).execute(new StringCallBackResult() {
                    @Override
                    public void success(Api api, String result) {
                 
                    }

                    @Override
                    public void failure(Api api, String error) {
                       
                    }
                });
    支持下载：
         APIManager.createApi(new HttpApi(Test.URL_DOWNLOAD)).execute(new 
         DownLoadCallBackResult(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test") {
         @Override
         public void success(Api api, String result) {
                        LogManger.i("下载成功，路径："+result);
                        show.setText(result);
                    }

                    @Override
                    public void failure(Api api, String error) {
                        LogManger.i("下载失败，："+error);
                        show.setText(error);
                    }

                    @Override
                    public void onDownloadProgress(long currentSize, long totalSize, double progress) {
                        LogManger.i("下载中，进度：currentSize:"+currentSize+"--totalSize:"+totalSize+"--progress:"+(int)(progress*100)+"%");
                        show.setText("进度：" + (int) (progress*100)+"%");
                    }
                });
         还有上传，头文件，图片。https。
         
