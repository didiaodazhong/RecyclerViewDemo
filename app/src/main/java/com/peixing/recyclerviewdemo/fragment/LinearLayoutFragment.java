package com.peixing.recyclerviewdemo.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.peixing.recyclerviewdemo.R;
import com.peixing.recyclerviewdemo.adapter.LinearAdapter;
import com.peixing.recyclerviewdemo.adapter.LoadMoreAdapter;
import com.peixing.recyclerviewdemo.view.MyDecoration;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LinearLayoutFragment extends BaseFragment {
    private static final int LOAD_MORE = 1;
    private static final int REGRESHING = 2;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView linearLayout;
    ArrayList<String> lists;
    ArrayList<String> newLists;
    ArrayList<String> moreData;
    LinearAdapter linearAdapter;
    RecyclerView.LayoutManager layoutManager;
    LoadMoreAdapter loadMoreAdapter;


    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_MORE:
                    //加载更多
                    lists.add(null);
                    loadMoreAdapter.notifyDataSetChanged();
                    loadMoreDatas();
                    break;
                case REGRESHING:
                    //下拉刷新
                    newLists = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        newLists.add(" 刷新出来的" + i);
                    }
                    lists.addAll(0, newLists);
                    loadMoreAdapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    protected void goBack() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        lists = new ArrayList<>();
//        lists.clear();
        if (newLists != null) {
            lists.addAll(0, newLists);
        }
        for (int i = 0; i < 15; i++) {
            lists.add("张三" + i);
        }
        if (moreData != null) {
            lists.addAll(moreData);
        }
//        linearAdapter = new LinearAdapter(getActivity(), lists);

        loadMoreAdapter = new LoadMoreAdapter(getActivity(), linearLayout);
        linearLayout.setAdapter(loadMoreAdapter);
        loadMoreAdapter.setData(lists);
        linearLayout.addItemDecoration(new MyDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        loadMoreAdapter.setOnMoreDataLoadListener(new LoadMoreAdapter.LoadMoreDataListener() {
            @Override
            public void loadMoreData() {
                myHandler.sendEmptyMessageDelayed(LOAD_MORE, 3500);
            }
        });


        loadMoreAdapter.setOnItemClickListener(new LoadMoreAdapter.RecyclerOnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), lists.get(position), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_linear_layout, null);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_linear);
        linearLayout = (RecyclerView) view.findViewById(R.id.linear_layout);
        linearLayout.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        linearLayout.setLayoutManager(layoutManager);
        swipeRefresh.setRefreshing(false);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.LightPink), getResources().getColor(R.color.LightSkyBlue), getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.Orchid));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                myHandler.sendEmptyMessageDelayed(REGRESHING, 4000);
            }
        });
        return view;
    }

    private void loadMoreDatas() {
        lists.remove(lists.size() - 1);
        loadMoreAdapter.notifyDataSetChanged();

        moreData = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
//            lists.add("新家在的数据" + i);
            moreData.add("新家在的数据" + i);

        }
        lists.addAll(moreData);
        loadMoreAdapter.notifyDataSetChanged();
        loadMoreAdapter.setLoaded();
    }
}