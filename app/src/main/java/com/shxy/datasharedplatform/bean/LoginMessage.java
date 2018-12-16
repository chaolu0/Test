package com.shxy.datasharedplatform.bean;

/**
 * Created by shxy on 2018/12/10.
 */

public class LoginMessage extends BaseMsg{
    private Integer id;
    private String SK;
    private String photo_path;

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getSK() {
        return SK;
    }

    public void setSK(String SK) {
        this.SK = SK;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
