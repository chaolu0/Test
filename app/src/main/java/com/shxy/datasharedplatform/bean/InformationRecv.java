package com.shxy.datasharedplatform.bean;

import java.util.List;

/**
 * Created by shxy on 2018/12/7.
 */

public class InformationRecv extends BaseMsg{
    private List<InformationBean> data;

    public List<InformationBean> getData() {
        return data;
    }

    public void setData(List<InformationBean> data) {
        this.data = data;
    }
}
