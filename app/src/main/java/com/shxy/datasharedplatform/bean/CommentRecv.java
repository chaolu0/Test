package com.shxy.datasharedplatform.bean;

import java.util.List;

/**
 * Created by shxy on 2018/12/13.
 */

public class CommentRecv extends BaseMsg{
    private List<CommentBean> data;

    public List<CommentBean> getData() {
        return data;
    }

    public void setData(List<CommentBean> data) {
        this.data = data;
    }
}
