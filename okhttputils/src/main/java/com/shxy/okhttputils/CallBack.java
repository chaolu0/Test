package com.shxy.okhttputils;

import java.io.IOException;

/**
 * Created by shxy on 2019/1/11.
 */

public interface CallBack {
    /**
     * UI Thread
     * @param e
     */
    void onFail(IOException e);

    /**
     * UI Thread
     * @param bytes
     */
    void onSuccess(byte[] bytes);

}
