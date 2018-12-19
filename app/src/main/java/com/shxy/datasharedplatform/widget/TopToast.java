package com.shxy.datasharedplatform.widget;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shxy.datasharedplatform.R;

/**
 * Created by shxy on 2018/12/19.
 */

public class TopToast extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     * or {@link Activity} object.
     */
    private String text;

    public TopToast(Context context) {
        super(context);
    }

    public static TopToast setText(Context context, String text, int duration) {
        TopToast toast = new TopToast(context);
        View v = LayoutInflater.from(context).inflate(R.layout.tip_view, null, false);
        TextView tv = v.findViewById(R.id.tip);
        tv.setText(text);
        toast.setView(v);
        toast.setDuration(duration);
        toast.setGravity(Gravity.TOP, 0, 0);
        return toast;
    }


}
