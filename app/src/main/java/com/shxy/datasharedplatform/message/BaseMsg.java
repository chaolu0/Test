package com.shxy.datasharedplatform.message;

/**
 * Created by shxy on 2018/12/4.
 */

public class BaseMsg {
    private String msg;
    private Integer state;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
