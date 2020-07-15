package com.cathy.ninepatchcontroldemo.utils;

import android.graphics.Bitmap;

import com.cathy.ninepatchcontroldemo.utils.glide.BitmapLruCacheHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: haoshuaihui
 * @CreateDate: 2019/7/2 15:05
 */
public class ImageSplitter {
    public static List<Bitmap> split(String url, Bitmap bitmap, int xPiece, int yPiece) {
        List<Bitmap> pieces = new ArrayList<>(xPiece * yPiece);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = width / xPiece;
        int pieceHeight = height / yPiece;
        for (int i = 0; i < yPiece; i++) {
            for (int j = 0; j < xPiece; j++) {
                int xValue = j * pieceWidth;
                int yValue = i * pieceHeight;
                StringBuilder key = new StringBuilder();
                key.append(url);
                key.append(i);
                key.append(j);
                Bitmap piece = BitmapLruCacheHelper.getInstance().getBitmapFromMemCache(key.toString());
                if (piece == null){
                    try {
                        //使用二级缓存避免对象频繁创建  可以减少oom
                        piece = Bitmap.createBitmap(bitmap, xValue, yValue,
                                pieceWidth, pieceHeight);
                        BitmapLruCacheHelper.getInstance().addBitmapToMemCache(key.toString(), piece);
                        pieces.add(piece);
                    } catch (Exception ignored) {

                    } catch (OutOfMemoryError error) {
                        error.printStackTrace();
                    }
                }else {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    public static Bitmap split1stFrame(String url, Bitmap bitmap, int xPiece, int yPiece) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = width / xPiece;
        int pieceHeight = height / yPiece;

        int i = 0, j = xPiece / 2;

        int xValue = j * pieceWidth;
        int yValue = i * pieceHeight;
        Bitmap piece;
        piece = BitmapLruCacheHelper.getInstance().getBitmapFromMemCache(url+"1"+"1");
        try {
            if (piece == null){
                piece = Bitmap.createBitmap(bitmap, xValue, yValue,
                        pieceWidth, pieceHeight);
                BitmapLruCacheHelper.getInstance().addBitmapToMemCache(url+"1"+"1",piece);
            }
        } catch (Exception ignored) {

        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }

        return piece;
    }
}
