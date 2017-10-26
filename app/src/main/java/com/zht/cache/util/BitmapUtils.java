package com.zht.cache.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;


/**
 * Created by zhanghaitao on 2017/8/11.
 * 图片三级缓存框架
 */
public class BitmapUtils {

    private static final String TAG = "BitmapUtils";

    static{
        netCacheUtils = new com.zht.cache.util.NetCacheUtils();
        localCacheUtils = new com.zht.cache.util.LocalCacheUtils();
        memoryCacheUtils = new com.zht.cache.util.MemoryCacheUtils();
    }

    private static com.zht.cache.util.NetCacheUtils netCacheUtils;
    private static com.zht.cache.util.LocalCacheUtils localCacheUtils;
    private static com.zht.cache.util.MemoryCacheUtils memoryCacheUtils;

    //显示图片
    public static void display(Context context,ImageView image, String url){
        Bitmap bitmap = null;
        //内存缓存
        bitmap = memoryCacheUtils.readCache(url);
        if(bitmap != null){
            image.setImageBitmap(bitmap);
            Log.i(TAG,"从内存获取图片");
            return;
        }
        //磁盘缓存
        bitmap = localCacheUtils.readCache(context, url);
        if(bitmap != null){
            image.setImageBitmap(bitmap);
            Log.i(TAG,"从磁盘获取图片");
            return;
        }
        //网络缓存
        netCacheUtils.getBitmapFromNet(context,image,url);
    }
}
