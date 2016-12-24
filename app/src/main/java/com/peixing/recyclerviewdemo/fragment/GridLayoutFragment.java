package com.peixing.recyclerviewdemo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.peixing.recyclerviewdemo.R;
import com.peixing.recyclerviewdemo.adapter.LinearAdapter;
import com.peixing.recyclerviewdemo.view.MyDecoration;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridLayoutFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshGrid;
    private RecyclerView gridLayout;


    @Override
    protected void goBack() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        ArrayList<String> lists = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            lists.add("李斯" + i);
        }
        LinearAdapter linearAdapter = new LinearAdapter(getActivity(), lists);
        gridLayout.setAdapter(linearAdapter);
        gridLayout.addItemDecoration(new MyDecoration(getActivity(), LinearLayoutManager.VERTICAL));

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
}
