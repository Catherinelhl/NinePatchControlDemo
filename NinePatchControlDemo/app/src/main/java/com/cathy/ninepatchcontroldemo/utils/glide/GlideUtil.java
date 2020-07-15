package com.cathy.ninepatchcontroldemo.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cathy.ninepatchcontroldemo.NinePatchBuilder;
import com.cathy.ninepatchcontroldemo.base.BaseApplication;
import com.cathy.ninepatchcontroldemo.utils.ImageSplitter;

import java.util.ArrayList;
import java.util.List;

/**
 * +--------------------------------------+
 * + @author Catherine Liu
 * +--------------------------------------+
 * + 2020/5/25 15:30
 * +--------------------------------------+
 * + Des:Glide加载动图
 * +--------------------------------------+
 */

public class GlideUtil {
    private Drawable drawable;
    private AnimationDrawable animationDrawable;

    public void loadAsNinePatchGif(final Context context, final String url, final View view, final int xPiece, @Nullable final OnAnimStartListener listener) {
        drawable = null;
        animationDrawable = null;
        view.setTag(view.getId(), url);
        if (xPiece == 1) {
            Bitmap bitmap = LRUCacheManager.getInstance().getDrawableFromMemCache(url);
            if (bitmap != null) {
                drawable = new NinePatchBuilder(context.getResources(), bitmap).build();
                BaseApplication.getInstance().getAppExecutors().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        view.setBackground(drawable);
                    }
                });
                return;
            }

        }
        final List<Bitmap> bitmapList = new ArrayList<>();
        for (int i = 0; i < xPiece; i++) {
            Bitmap bitmap = LRUCacheManager.getInstance().getDrawableFromMemCache(url + i);
            if (bitmap != null) {
                bitmapList.add(bitmap);
            }
        }
        if (bitmapList.size() > 0) {
            if (bitmapList.size() == 1) {
                Bitmap bitmap = bitmapList.get(0);
                if (bitmap != null) {
                    drawable = new NinePatchBuilder(context.getResources(), bitmap).build();
                    BaseApplication.getInstance().getAppExecutors().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            view.setBackground(drawable);
                            bitmapList.clear();
                            return;
                        }
                    });
                }

            } else {
                animationDrawable = new AnimationDrawable();
                for (int i = 0; i < bitmapList.size(); i++) {
                    Bitmap bitmap = bitmapList.get(i);
                    Drawable drawable = new NinePatchBuilder(context.getResources(), bitmap).build();
                    animationDrawable.addFrame(drawable, 750 / xPiece);
                }
                if (animationDrawable != null) {
                    BaseApplication.getInstance().getAppExecutors().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            view.setBackground(animationDrawable);
                            animationDrawable.setOneShot(false);
                            animationDrawable.start();

                        }
                    });
                    if (listener != null) {
                        listener.onAnimStart(animationDrawable);
                    }
                    bitmapList.clear();
                    return;
                }

            }


        }

        Glide.with(context)
                .asBitmap()
                .fitCenter()
                .skipMemoryCache(false)
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<?
                            super Bitmap> transition) {
                        if (!url.equals(view.getTag(view.getId()))) {
                            return;
                        }
                        if (xPiece > 1) {
                            animationDrawable = new AnimationDrawable();
                            List<Bitmap> splits = ImageSplitter.split(url, resource,
                                    xPiece, 1);
                            for (int i = 0; i < splits.size(); i++) {
                                Bitmap bitmap = splits.get(i);
                                LRUCacheManager.getInstance().addDrawable(url + i, bitmap);
                                Drawable drawable = new NinePatchBuilder(context.getResources(), bitmap).build();
                                animationDrawable.addFrame(drawable, 750 / xPiece);
                            }
                            splits.clear();
                            BaseApplication.getInstance().getAppExecutors().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    view.setBackground(animationDrawable);
                                    animationDrawable.setOneShot(false);
                                    animationDrawable.start();

                                }
                            });
                            if (listener != null) {
                                listener.onAnimStart(animationDrawable);
                            }
                        } else {
                            LRUCacheManager.getInstance().addDrawable(url, resource);
                            drawable = new NinePatchBuilder(context.getResources(), resource).build();
                            BaseApplication.getInstance().getAppExecutors().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    view.setBackground(drawable);
                                }
                            });
                        }

                    }
                });
    }


    public interface OnAnimStartListener {
        void onAnimStart(AnimationDrawable drawable);
    }
}
