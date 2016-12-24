package com.peixing.recyclerviewdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peixing.recyclerviewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.ViewHolder> {
    private static final String TAG = "Adapter";
    List<String> message;
    private  Context context;
    private OnRecyclerViewItemClickListener mListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }


    public LinearAdapter(Context context, ArrayList<String> lists) {
        this.context = context;
        this.message = lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_linearlayout, false);
        View view = mInflater.inflate(R.layout.item_linearlayout, viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder itemViewHolder, final int position) {
        Log.i(TAG, "onBindViewHolder: " + message.get(position));
        itemViewHolder.tvName.setText(message.get(position));

        //Here you can fill your row view
        if (mListener != null) {
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return message.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
