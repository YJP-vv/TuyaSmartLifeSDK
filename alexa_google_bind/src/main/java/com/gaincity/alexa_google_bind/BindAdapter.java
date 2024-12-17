package com.gaincity.alexa_google_bind;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thingclips.smart.social.auth.manager.api.AuthorityBean;

import java.util.List;

public class BindAdapter extends RecyclerView.Adapter<BindAdapter.ViewHolder> {
    private List<AuthorityBean> mData;
    private OnItemClickListener mOnItemClickListener;

    public BindAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bind_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final AuthorityBean bean = this.mData.get(position);
        if (bean != null) {
            if (!TextUtils.isEmpty(bean.getPlatformName())) {
                viewHolder.title.setText(bean.getPlatformName());
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(bean, viewHolder.getBindingAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (mData != null && !mData.isEmpty()) ? mData.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setData(List<AuthorityBean> beans) {
        this.mData = beans;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(AuthorityBean authorityBean, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.tv_bind_title);
        }
    }
}
