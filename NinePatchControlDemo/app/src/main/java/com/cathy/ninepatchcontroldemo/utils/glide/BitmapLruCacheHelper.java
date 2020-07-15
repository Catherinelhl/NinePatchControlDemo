// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.cathy.ninepatchcontroldemo.utils.glide;


import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapLruCacheHelper {
    private static final String TAG = BitmapLruCacheHelper.class.getSimpleName();
    private static BitmapLruCacheHelper instance = new BitmapLruCacheHelper();
    LruCache<String, Bitmap> cache;

    private BitmapLruCacheHelper() {
        cache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 10F)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount()/1024;
            }
        };
    }

    public static BitmapLruCacheHelper getInstance() {
        return instance;
    }

    public void addBitmapToMemCache(String s, Bitmap bitmap) {
        if (s != null && bitmap != null) {
            if (cache != null && getBitmapFromMemCache(s) == null) {
                cache.put(s, bitmap);
            }
        }
    }

    public Bitmap getBitmapFromMemCache(String s) {
        if (s == null) {
            return null;
        } else {
            return cache.get(s);
        }
    }

    public Bitmap removeBitmapFromMemCache(String s) {
        if (s == null) {
            return null;
        } else {
            return cache.remove(s);
        }
    }


    public void cleanCache() {
        if (cache != null) {
            cache.evictAll();
        }
    }
}
