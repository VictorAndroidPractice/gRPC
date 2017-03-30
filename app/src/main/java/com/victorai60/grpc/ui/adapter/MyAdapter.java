package com.victorai60.grpc.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.victorai60.grpc.R;
import com.victorai60.grpc.entity.TitleBean;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<TitleBean> titleBeen;
    private int currentPosition;
    private OnItemSelectedListener onItemSelectedListener;

    public MyAdapter(List<TitleBean> titleBeen) {
        this.titleBeen = titleBeen;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        myViewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvTitle.setText(titleBeen.get(position).getTitle());
        if (titleBeen.get(position).isSelected()) {
            holder.tvTitle.setTextSize(18);
            holder.tvTitle.setTextColor(Color.BLUE);
        } else {
            holder.tvTitle.setTextSize(16);
            holder.tvTitle.setTextColor(Color.BLACK);
        }
        holder.tvTitle.setOnClickListener(new MyOnClickListener(position));
    }

    @Override
    public int getItemCount() {
        return titleBeen.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        private int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            titleBeen.get(currentPosition).setSelected(false);
            titleBeen.get(position).setSelected(true);
            MyAdapter.this.notifyItemChanged(currentPosition);
            MyAdapter.this.notifyItemChanged(position);
            if (onItemSelectedListener != null) {
                onItemSelectedListener.onItemSelected(getItemCount(), currentPosition, position);
            }
            currentPosition = position;
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int itemCount, int currentPosition, int position);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }
}
