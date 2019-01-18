package com.shxy.imgloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;
import com.shxy.okhttputils.CallBack;
import com.shxy.okhttputils.OkHttpUtils;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by shxy on 2019/1/16.
 */

public class ImgLoader {

    private static ImgLoader imgLoader = null;
    private static LruCache<String, Bitmap> memoryLruCache;
    private static DiskLruCache diskLruCache;
    private static Handler handler = null;

    public static ImgLoader with(Context context) {
        if (imgLoader == null) {
            synchronized (ImgLoader.class) {
                if (imgLoader == null) {
                    imgLoader = new ImgLoader();
                    initLruCache(context);
                    initHandler();
                }
            }
        }
        return imgLoader;
    }

    private static void initHandler() {
        handler = new Handler();
    }

    private static void initLruCache(Context context) {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        memoryLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        try {
            diskLruCache = DiskLruCache.open(getDiskCacheDir(context, "shxy"), 1, 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    public LoadRequest url(String url) {
        return new LoadRequest(url);
    }

    static void loadImage(final LoadRequest request) {
        Bitmap bitmap = memoryLruCache.get(request.getUrl());
        if (bitmap != null) {
            handler.post(new LoadTask(bitmap, request.getImageView()));
        } else {
            try {
                DiskLruCache.Snapshot snapshot = diskLruCache.get(hashKeyForDisk(request.getUrl()));
                if (snapshot != null) {
                    bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
                    handler.post(new LoadTask(bitmap, request.getImageView()));
                    snapshot.close();
                } else {
                    OkHttpUtils.get().url(request.getUrl()).build().execute(new CallBack() {
                        @Override
                        public void onFail(IOException e) {

                        }

                        @Override
                        public void onSuccess(byte[] bytes) {
                            DiskLruCache.Editor edit = null;
                            try {
                                edit = diskLruCache.edit(hashKeyForDisk(request.getUrl()));
                                if (edit != null) {
                                    edit.newOutputStream(0).write(bytes);
                                    edit.commit();
                                }
                                Bitmap bp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                memoryLruCache.put(request.getUrl(), bp);
                                if (request.getImageView().getTag(R.id.url_tag).equals(request.getUrl())){
                                    handler.post(new LoadTask(bp,request.getImageView()));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private static class LoadTask implements Runnable {

        private ImageView imageView;
        private Bitmap bitmap;

        public LoadTask(Bitmap bitmap, ImageView imageView) {
            this.bitmap = bitmap;
            this.imageView = imageView;
        }

        @Override
        public void run() {
            imageView.setImageBitmap(bitmap);
        }
    }
}
