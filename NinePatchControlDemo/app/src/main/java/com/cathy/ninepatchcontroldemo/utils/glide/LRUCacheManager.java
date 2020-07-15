package com.cathy.ninepatchcontroldemo.utils.glide;

import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.LruCache;

/**
 * +--------------------------------------+
 * + @author Catherine Liu
 * +--------------------------------------+
 * + 2020/7/15 13:47
 * +--------------------------------------+
 * + Des:图片资源管理
 * +--------------------------------------+
 */
public class LRUCacheManager {
    private static LRUCacheManager instance;
    private LruCache<String, Bitmap> mMemoryCache;

    public static LRUCacheManager getInstance() {
        if (instance == null) {
            synchronized (LRUCacheManager.class) {
                if (instance == null) {
                    instance = new LRUCacheManager();
                }
            }
        }
        return instance;
    }

    private LRUCacheManager() {
        /**
         * java虚拟机（这个进程）能构从操纵系统那里挖到的最大的内存，以字节为单位
         * 也可以用
         * ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass()来获取，不过返回值的单位是M
         */
        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        //给LruCache分配1/8
        int cacheSize = maxMemory / 1024 / 4;
        System.out.println("LRUCacheManager:" + cacheSize);
        /**
         *  创建LruCache时需要提供缓存的最大容量
         */
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return super.sizeOf(key, value);
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                //当调用put或remove时触发此方法，可以在这里完成一些资源回收的操作
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };
    }

    public void addDrawable(String url, Bitmap bitmaps) {
        if (TextUtils.isEmpty(url) || bitmaps == null) {
            return;
        }
        if (getDrawableFromMemCache(url) == null) {
            mMemoryCache.put(url, bitmaps);
        }
    }

    public Bitmap getDrawableFromMemCache(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        } else {
            return mMemoryCache.get(url);
        }
    }


    /**
     * 删除缓存对象
     *
     * @param url
     */
    public void removeCacheObject(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        mMemoryCache.remove(url);
    }

    /**
     * 重新设置缓存总容量大小
     */
    public void resizeCache(int cacheSize) {
        if (mMemoryCache == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMemoryCache.resize(cacheSize);
        }
    }

    public void clearAll() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }
    }

}
