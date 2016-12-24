package com.peixing.recyclerviewdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zhy on 16/6/6.
 */
public abstract class BaseFragment extends Fragment {
    public Context context;
    public View view;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.context = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = initView(inflater);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initData(savedInstanceState);
        goBack();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 回掉函数
     */
    protected abstract void goBack();

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 初始化界面
     *
     * @param inflater
     * @return
     */
    protected abstract View initView(LayoutInflater inflater);

}
