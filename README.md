# HttpApiDemo
         
               此基于okhttp。封装缓存，全局缓存设置，全局缓存过期时间设置，全局请求方式设置，同时也可以每一个接口对应一个缓存过期时间。支持文件
     的上传，下载。图片加载（已经压缩）。
     请求框架和底层请求库分离，可以随时变化请求底层方式，。比如未来出现了更优秀的请求库，那么okhttp就需要更新到这个优秀的请求库，那么调用者
     依然如此调用，只需变化底层方法，预加载的参数设置，请求的接口适配回调设置。统一不变的方法提供给调用者，变化的是底层的适配。
