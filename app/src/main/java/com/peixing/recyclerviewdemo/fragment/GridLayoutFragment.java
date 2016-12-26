package com.peixing.recyclerviewdemo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.peixing.recyclerviewdemo.Image;
import com.peixing.recyclerviewdemo.R;
import com.peixing.recyclerviewdemo.adapter.GridAdapter;
import com.peixing.recyclerviewdemo.adapter.LinearAdapter;
import com.peixing.recyclerviewdemo.view.MyDecoration;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridLayoutFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshGrid;
    private RecyclerView gridLayout;
    GridAdapter gridAdapter;

    @Override
    protected void goBack() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        ArrayList<String> lists = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            lists.add("李斯" + i);
        }
//        LinearAdapter linearAdapter = new LinearAdapter(getActivity(), lists);
//        gridLayout.setAdapter(linearAdapter);

        gridAdapter = new GridAdapter(getActivity(), Image.imageThumbUrls, gridLayout);
        gridLayout.setAdapter(gridAdapter);
        gridLayout.addItemDecoration(new MyDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        gridAdapter.setItemHeight(250);
       /* gridLayout.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {

            }
        });*/
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_grid_layout, null);
        swipeRefreshGrid = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_grid);
        gridLayout = (RecyclerView) view.findViewById(R.id.grid_layout);
        gridLayout.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayout.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        gridAdapter.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gridAdapter.cancelAllTasks();
    }
}
