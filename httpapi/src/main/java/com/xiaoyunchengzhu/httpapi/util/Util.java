package com.xiaoyunchengzhu.httpapi.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xiaoyunchengzhu.httpapi.net.APIManager;
import com.xiaoyunchengzhu.httpapi.net.Api;
import com.xiaoyunchengzhu.httpapi.net.DownLoadCallBackResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhangshiyu on 2016/4/26.
 */
public class Util {

    public static void inputStreamToFile(final InputStream inputStream, final long contentLength, final Api api, final DownLoadCallBackResult downLoadCallBackResult)
    {
        ThreadPool.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
        long curreantSize=0;
        final File file=new File(downLoadCallBackResult.getDownloadPath());
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] buffer=new byte[1024];
            int read=0;
            while ((read=inputStream.read(buffer))>0)
            {
                    fileOutputStream.write(buffer,0,read);
                    fileOutputStream.flush();
                curreantSize+=read;
                final long finalCurreantSize = curreantSize;
                APIManager.getInstance().getHandler().post(new Runnable() {
                    @Override
                    public void run() {


                downLoadCallBackResult.onDownloadProgress(finalCurreantSize,contentLength, (double)finalCurreantSize/contentLength);
                    }
                });
            }

            APIManager.getInstance().getHandler().post(new Runnable() {
                @Override
                public void run() {


                    downLoadCallBackResult.success(api, file.getAbsolutePath());
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
            }
        });


    }

    public static void convertBytes(final InputStream inputStream, final ConvertCallback convertCallback)
    {


        ThreadPool threadPool=ThreadPool.getThreadPool();
        Thread runnable=new Thread( new Runnable() {
            @Override
            public void run() {


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int read = 0;
                try {
                    while ((read = inputStream.read(buffer)) > 0) {
                        byteArrayOutputStream.write(buffer, 0, read);
                        byteArrayOutputStream.flush();
                    }
                    final  byte[] data=byteArrayOutputStream.toByteArray();

                    APIManager.getInstance().getHandler().post(new Runnable() {
                      @Override
                      public void run() {
                          convertCallback.success(data);
                      }
                  });

                    byteArrayOutputStream.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                    APIManager.getInstance().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            convertCallback.failure(e.getMessage());
                        }
                    });

                }
            }
        });
        threadPool.execute(runnable);



    }

    /**
     *
     * @param value
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapFromBytes(byte[] value, int width, int height) {
        if (null != value && value.length>0) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();          //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
                opts.inJustDecodeBounds = true;
               BitmapFactory.decodeByteArray(value, 0, value.length);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);           //这里一定要将其设置回false，因为之前我们将其设置成了true
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeByteArray(value, 0, value.length, opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    /**
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


    public static boolean isSuperClass(Class class1,Class class2)
    {
        boolean isSuperClass=false;
        if (class1.getSuperclass().equals(class2))
        {
            isSuperClass= true;
        }else {
            if (!class1.equals(Object.class)) {
               isSuperClass= isSuperClass(class1.getSuperclass(), class2);
            }
            isSuperClass= false;
        }
        return isSuperClass;

    }

}
