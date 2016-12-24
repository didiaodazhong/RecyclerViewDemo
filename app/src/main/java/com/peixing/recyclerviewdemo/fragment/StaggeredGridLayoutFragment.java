package com.peixing.recyclerviewdemo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.peixing.recyclerviewdemo.R;
import com.peixing.recyclerviewdemo.adapter.LinearAdapter;
import com.peixing.recyclerviewdemo.adapter.StaggeredAdapter;
import com.peixing.recyclerviewdemo.view.MyDecoration;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaggeredGridLayoutFragment extends BaseFragment {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView linearLayout1;



    @Override
    protected void goBack() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        ArrayList<String> lists = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            lists.add("李斯羽" + i);
        }
        StaggeredAdapter staggeredAdapter = new StaggeredAdapter(lists);
        linearLayout1.setAdapter(staggeredAdapter);
        linearLayout1.addItemDecoration(new MyDecoration(getActivity(), LinearLayoutManager.VERTICAL));

    }

    @Override
    protected View initView(LayoutInflater inflater) {
        view  = inflater.inflate(R.layout.fragment_stagger_layout, null);

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_staggered);
        linearLayout1 = (RecyclerView) view.findViewById(R.id.staggergrid_layout);
        linearLayout1.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
        linearLayout1.setLayoutManager(layoutManager);
        return view;
    }
}
