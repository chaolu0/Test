package com.shxy.datasharedplatform.bean;

/**
 * Created by shxy on 2018/12/20.
 */

public class UploadPhotoMessage extends BaseMsg{
    private String photo_path;

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }
}
