package com.peixing.recyclerviewdemo.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by peixing on 2016/12/24.
 */

public abstract class LoadMoreListener extends RecyclerView.OnScrollListener {

    //声明一个Managers
    private LinearLayoutManager layoutManager;
    //已经加载出来的数量
    private int totalItemCount;

    //存储上一个totalItemCount
    private int previousTotal = 0;
    //N当前屏幕现实的第一个
    private int firstVisableItem;
    //当前屏幕可见的数量
    private int visibleItemCount;
    //当前页
    private int currentPage = 0;

    //当前是否在上拉加载
    private boolean loading = true;

    public LoadMoreListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        firstVisableItem = layoutManager.findFirstVisibleItemPosition();
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (!loading && totalItemCount - visibleItemCount <= firstVisableItem) {
            currentPage++;
            onLoadingMore(currentPage);
            loading = true;

        }
    }

    public abstract void onLoadingMore(int currentPage);
}
