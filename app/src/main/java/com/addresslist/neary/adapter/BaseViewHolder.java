package com.addresslist.neary.adapter;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 获取item的控件工具类
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    protected final SparseArray<View> mViews;
    protected View mConvertView;


    public BaseViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        mConvertView = itemView;
    }


    /**
     * 通过控件的Id获取对应的控件，如果没有则加入mViews，则从item根控件中查找并保存到mViews中
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public BaseViewHolder setBgColor(@IdRes int resID, int color) {
        getView(resID).setBackgroundColor(color);
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public BaseViewHolder setBgDrawable(@IdRes int resID, Drawable drawable) {
        getView(resID).setBackground(drawable);
        return this;

    }


    public BaseViewHolder setTextColor(@IdRes int resID, int color) {
        ((TextView) getView(resID)).setTextColor(color);
        return this;
    }

    public BaseViewHolder setText(@IdRes int resID, String text) {
        ((TextView) getView(resID)).setText(text);
        return this;
    }

    public BaseViewHolder setTextSize(@IdRes int resID, int spSize) {
        ((TextView) getView(resID)).setTextSize(spSize);
        return this;
    }

    public BaseViewHolder setVisibility(@IdRes int resID, @Visibility int visibility) {
        switch (visibility) {
            case VISIBLE:
                getView(resID).setVisibility(View.VISIBLE);
                break;
            case INVISIBLE:
                getView(resID).setVisibility(View.INVISIBLE);
                break;
            case GONE:
                getView(resID).setVisibility(View.GONE);
                break;

        }
        return this;

    }


    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }

    public static final int VISIBLE = 0x00000000;
    public static final int INVISIBLE = 0x00000004;

    public static final int GONE = 0x00000008;


}
