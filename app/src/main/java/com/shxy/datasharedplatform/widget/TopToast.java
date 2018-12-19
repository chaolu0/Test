package com.shxy.datasharedplatform.widget;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.shxy.datasharedplatform.R;

/**
 * Created by shxy on 2018/12/19.
 */

public class TopToast extends Toast{
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public TopToast(Context context) {
        super(context);
//        setView(LayoutInflater.from(context).inflate(R.));
    }
}
