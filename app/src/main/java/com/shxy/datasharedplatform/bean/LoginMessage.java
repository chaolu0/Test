package com.shxy.datasharedplatform.bean;

/**
 * Created by shxy on 2018/12/10.
 */

public class LoginMessage extends BaseMsg{
    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
