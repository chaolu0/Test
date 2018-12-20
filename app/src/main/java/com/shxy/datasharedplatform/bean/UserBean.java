package com.shxy.datasharedplatform.bean;

public class UserBean {

    private Integer id;

    private String username;

    private String password;

    private String photo_path;

    private String SK;

    private String nickName;

    private String personal_sign;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPersonal_sign() {
        return personal_sign;
    }

    public void setPersonal_sign(String personal_sign) {
        this.personal_sign = personal_sign;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
}
