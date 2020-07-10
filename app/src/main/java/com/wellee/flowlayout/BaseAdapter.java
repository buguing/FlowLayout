package com.wellee.flowlayout;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public abstract class BaseAdapter {

    /**
     * 获取itemView
     *
     * @param position position
     * @param parent   parent
     * @return View
     */
    public abstract View getItemView(int position, @NonNull ViewGroup parent);

    /**
     * 获取子View数量
     *
     * @return 子View数量
     */
    public abstract int getItemCount();

}
