package com.peixing.recyclerviewdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peixing.recyclerviewdemo.R;

import java.util.List;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class StaggeredAdapter extends RecyclerView.Adapter<StaggeredAdapter.ViewHolder> {
    private static final String TAG = "Adapter";
    List<String> message;
    private LinearAdapter.OnRecyclerViewItemClickListener mListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }


    public StaggeredAdapter(List<String> message) {
        this.message = message;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_linearlayout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder itemViewHolder, final int position) {

        //Here you can fill your row view
        itemViewHolder.tvName.setText(message.get(position));
        itemViewHolder.tvName.setHeight((position%5)*10);
//        itemViewHolder.tvName.setTextSize((position%)*10);
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



    public void setOnItemClickListener(LinearAdapter.OnRecyclerViewItemClickListener listener) {
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
