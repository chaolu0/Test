package com.shxy.imgloader;

import android.widget.ImageView;

/**
 * Created by shxy on 2019/1/16.
 */

public class LoadRequest {

    private String url = null;
    private ImageView imageView;
    LoadRequest(String url) {
        this.url = url;
    }

    public void into(ImageView imageView){
        if (imageView == null){
            return;
        }
        this.imageView = imageView;
        imageView.setTag(R.id.url_tag,url);
        ImgLoader.loadImage(this);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getUrl() {
        return url;
    }
}
