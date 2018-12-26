package com.shxy.datasharedplatform;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by shxy on 2018/12/11.
 */

public class RecyclerItemDecoration extends RecyclerView.ItemDecoration {
    private int topSpace;
    private int itemNum;
    private int leftSpace;

    /**
     * @param topSpace item间隔
     * @param itemNum   每行item的个数
     */
    public RecyclerItemDecoration(int topSpace, int leftSpace,int itemNum) {
        this.topSpace = topSpace;
        this.itemNum = itemNum;
        this.leftSpace = leftSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = topSpace;
        if (parent.getChildLayoutPosition(view) % itemNum == 0) {  //parent.getChildLayoutPosition(view) 获取view的下标
            outRect.left = 0;
        } else {
            outRect.left = leftSpace;
        }
        if (parent.getChildLayoutPosition(view) / itemNum == 0) {
            outRect.top = 0;
        } else {
            outRect.top = topSpace;
        }

    }
}
