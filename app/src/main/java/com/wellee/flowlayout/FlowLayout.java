package com.wellee.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    //存放容器中所有的View
    private List<List<View>> mAllViews = new ArrayList<>();
    //存放每一行最高View的高度
    private List<Integer> mPerLineMaxHeight = new ArrayList<>();

    private BaseAdapter mAdapter;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        super.generateLayoutParams(p);
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        int totalLineWidth = 0;
        int perLineMaxHeight = 0;
        int totalHeight = 0;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (totalLineWidth + childWidth > widthSize) {
                // 总高度累加
                totalHeight += perLineMaxHeight;
                // 换行了
                totalLineWidth = childWidth;
                perLineMaxHeight = childHeight;
            } else {
                totalLineWidth += childWidth;
                perLineMaxHeight = Math.max(perLineMaxHeight, childHeight);
            }
            if (i == childCount - 1) {
                totalHeight += perLineMaxHeight;
            }
        }
        //如果高度的测量模式是EXACTLY，则高度用测量值，否则用计算出来的总高度（这时高度的设置为wrap_content）
        heightSize = heightMode == MeasureSpec.EXACTLY ? heightSize : totalHeight;
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mPerLineMaxHeight.clear();

        //存放每一行的子View
        List<View> lineViews = new ArrayList<>();
        //记录每一行已存放View的总宽度
        int totalLineWidth = 0;

        //记录每一行最高View的高度
        int lineMaxHeight = 0;

        /*************遍历所有View，将View添加到List<List<View>>集合中***************/
        //获得子View的总个数
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (totalLineWidth + childWidth > getWidth()) {
                mAllViews.add(lineViews);
                mPerLineMaxHeight.add(lineMaxHeight);

                //开启新的一行
                totalLineWidth = 0;
                lineMaxHeight = 0;
                lineViews = new ArrayList<>();
            }
            totalLineWidth += childWidth;
            lineMaxHeight = Math.max(lineMaxHeight, childHeight);
            lineViews.add(child);
        }
        //单独处理最后一行
        mAllViews.add(lineViews);
        mPerLineMaxHeight.add(lineMaxHeight);

        /************遍历集合中的所有View并显示出来*****************/
        //表示一个View和父容器左边的距离
        int mLeft = 0;
        //表示View和父容器顶部的距离
        int mTop = 0;

        for (int i = 0; i < mAllViews.size(); i++) {
            //获得每一行的所有View
            lineViews = mAllViews.get(i);
            lineMaxHeight = mPerLineMaxHeight.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int leftChild = mLeft + lp.leftMargin;
                int topChild = mTop + lp.topMargin;
                int rightChild = leftChild + child.getMeasuredWidth();
                int bottomChild = topChild + child.getMeasuredHeight();
                //四个参数分别表示View的左上角和右下角
                child.layout(leftChild, topChild, rightChild, bottomChild);
                mLeft += lp.leftMargin + child.getMeasuredWidth() + lp.rightMargin;
            }
            mLeft = 0;
            mTop += lineMaxHeight;
        }
    }

    public void setAdapter(BaseAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter must be not null");
        }

        removeAllViews();
        mAdapter = null;
        mAdapter = adapter;

        int childCount = mAdapter.getItemCount();
        for (int i = 0; i < childCount; i++) {
            View child = mAdapter.getItemView(i, this);
            addView(child);
        }
    }

    public interface OnSelectedListener {
        void selected(int position);
    }
}
