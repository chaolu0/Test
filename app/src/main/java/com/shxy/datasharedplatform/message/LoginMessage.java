package com.shxy.datasharedplatform.message;

/**
 * Created by shxy on 2018/12/10.
 */

public class LoginMessage extends BaseMsg{
    private Integer id;
    private String SK;

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
