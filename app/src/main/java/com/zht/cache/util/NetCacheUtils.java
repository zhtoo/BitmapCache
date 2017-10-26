package com.zht.cache.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhanghaitao on 2017/8/11.
 * 网络缓存
 * 联网加载数据
 */
public class NetCacheUtils {

    private static final String TAG = "NetCacheUtils";
    private Context context;

    /**
     * 从网络获取图片
     * @param context
     * @param image
     * @param url
     */
    public void getBitmapFromNet(Context context,ImageView image, String url){
        this.context = context;
        //让ImageView和url关联起来
        image.setTag(url);
        //异步操作
        new BitmapTask().execute(image,url);
    }

    //异步任务
    class BitmapTask extends AsyncTask<Object,Void,Bitmap>{

        private ImageView image;
        private String url;

        @Override
        protected Bitmap doInBackground(Object... params) {
            //获取参数
            image = (ImageView) params[0];
            url = (String) params[1];

            //下载图片
            Bitmap bitmap = downloadBitmap(url);
            Log.i(TAG,"从网络上加载了图片");
            //执行磁盘缓存
            LocalCacheUtils.saveCache(context,bitmap,url);
            //把数据缓存在内存中
            MemoryCacheUtils.saveCache(bitmap,url);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //获取ImageView对应的url
            String url = (String) image.getTag();
            if(bitmap != null && this.url.equals(url)){
                image.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * 下载图片
     * @param url
     * @return
     */
    private Bitmap downloadBitmap(String url) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");//设置请求方式
            conn.setConnectTimeout(3000);//设置连接超时的时间
            conn.setReadTimeout(6000);//设置传递数据的超时时间
            conn.connect();//连接
            int responseCode = conn.getResponseCode();
            //响应码为200即成功
            if(responseCode == 200){
                InputStream inputStream = conn.getInputStream();
                //把流转换成Bitmap对象
                bitmap = BitmapFactory.decodeStream(inputStream);
                //TODO:在此可以执行图片压缩，不建议在网络缓存时压缩。
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn != null){
                conn.disconnect();
            }
        }
        return bitmap;
    }
}
