package com.peixing.recyclerviewdemo.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.peixing.recyclerviewdemo.R;

import java.util.List;

/**
 * Created by peixing on 2016/12/24.
 */

public class LoadMoreAdapter extends RecyclerView.Adapter {
 /*   private static final int VIEW_ITEM = 0;
    private static final int VIEW_FROG = 1;
    private Context context;
    private RecyclerView recyclerView;
    private List<String> lists;

    private boolean isLoading = false;
    LayoutInflater inflater;
    private int totalItemCount;

    private int lastVisibleItemPosition;

    private int visibleThreshold = 5;
    //加载更多
    private LoadMoreDataListener mMoreDataListener;
    //下拉刷新
    private OnRecyclerViewItemClickListener mListener = null;


    public LoadMoreAdapter(Context context, RecyclerView recyclerView, List<String> lists) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
                        if (mMoreDataListener != null) {
                            mMoreDataListener.loadMoreData();
                            isLoading = true;
                        }
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }
    }

    public void setLoaded() {
        isLoading = false;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_ITEM) {
            holder = new MyLoadMoreViewHolder(inflater.inflate(R.layout.item_linearlayout, parent, false));
        } else {
            holder = new MyProgressHolder(inflater.inflate(R.layout.item_footer, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyLoadMoreViewHolder) {
            if (((MyLoadMoreViewHolder) holder).tvName != null) {
                ((MyLoadMoreViewHolder) holder).tvName.setText(lists.get(position));
                if (mListener != null)
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onItemClick(v, position);
                        }
                    });
            } else if (holder instanceof MyProgressHolder) {
                if (((MyProgressHolder) holder).pb != null)
                    ((MyProgressHolder) holder).pb.setIndeterminate(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return lists.get(position) != null ? VIEW_ITEM : VIEW_FROG;
    }

    public class MyLoadMoreViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public MyLoadMoreViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public class MyProgressHolder extends RecyclerView.ViewHolder {
        ProgressBar pb;

        public MyProgressHolder(View itemView) {
            super(itemView);

            pb = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mListener = listener;
    }


    public interface LoadMoreDataListener {
        public abstract void loadMoreData();
    }

    public void setOnMoreDataLoadListener(LoadMoreDataListener loadListener) {
        this.mMoreDataListener = loadListener;
    }*/

    private static final int VIEW_ITEM = 0;
    private static final int VIEW_PROG = 1;
    private final Context mContext;
    private final RecyclerView mRecyclerView;
    //加载数据源
    private List<String> mData;
    private final LayoutInflater inflater;
    //加载更多状态码
    private boolean isLoading;
    //总条目数
    private int totalItemCount;
    //最后一个可显示的条目位置
    private int lastVisibleItemPosition;
    //当前滚动的position下面最小的items的临界值
    private int visibleThreshold = 5;

    RecyclerView.ViewHolder holder;

    //加载更多监听
    private LoadMoreDataListener mMoreDataListener;
    //条目点击监听
    private RecyclerOnItemClickListener mOnitemClickListener;

    //加载更多接口
    public interface LoadMoreDataListener {
        public abstract void loadMoreData();
    }

    //条目点击接口
    public interface RecyclerOnItemClickListener {
        public abstract void onItemClick(View view, int position);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param recyclerView
     */

    public LoadMoreAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mRecyclerView = recyclerView;
        //
        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            //获取layoutManager
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            //mRecyclerView添加滑动事件监听
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    Log.d("test", "totalItemCount =" + totalItemCount + "-----" + "lastVisibleItemPosition =" + lastVisibleItemPosition);
                    if (!isLoading && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
                        //此时是刷新状态
                        if (mMoreDataListener != null)
                            mMoreDataListener.loadMoreData();
                        isLoading = true;
                    }
                }
            });
        }
    }

    public void setLoaded() {
        //反转状态码
        isLoading = false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            //加载显示正常条目
            holder = new MyViewHolder(inflater.inflate(R.layout.item_linearlayout, parent, false));
        } else {
            //加载显示正在加载footer条目
            holder = new MyProgressViewHolder(inflater.inflate(R.layout.item_footer, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            if (((MyViewHolder) holder).tv_name != null)
                ((MyViewHolder) holder).tv_name.setText(mData.get(position));
            if (mOnitemClickListener != null)
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //条目点击方法
                        mOnitemClickListener.onItemClick(v, position);
                    }
                });
        } else if (holder instanceof MyProgressViewHolder) {
            if (((MyProgressViewHolder) holder).pb != null)
                ((MyProgressViewHolder) holder).pb.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        //需要加入一个footerView布局
        return mData == null ? 0 : mData.size() + 1;
    }


    //根据不同的数据返回不同的viewType
    @Override
    public int getItemViewType(int position) {
//        return mData.get(position) != null ? VIEW_ITEM : VIEW_PROG;
        if (position + 1 == getItemCount()) {
            //判断当前是否为最后一个条目
            return VIEW_PROG;
        } else {
            return VIEW_ITEM;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public class MyProgressViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar pb;

        public MyProgressViewHolder(View itemView) {
            super(itemView);
            pb = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }

    //设置数据的方法
    public void setData(List<String> data) {
        mData = data;
    }

    //加载更多监听方法
    public void setOnMoreDataLoadListener(LoadMoreDataListener onMoreDataLoadListener) {
        mMoreDataListener = onMoreDataLoadListener;
    }


    //点击事件监听方法
    public void setOnItemClickListener(RecyclerOnItemClickListener onItemClickListener) {
        mOnitemClickListener = onItemClickListener;
    }
}
