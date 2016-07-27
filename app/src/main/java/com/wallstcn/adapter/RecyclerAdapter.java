package com.wallstcn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wallstcn.R;
import com.wallstcn.bean.ChannalIemBean;
import com.wallstcn.entity.Config;
import com.wallstcn.utils.GlideUtil;

import java.util.List;

/**
 * Created by _GZL on 2016/3/5.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private int footViewCount = 1;
    private List<ChannalIemBean> items;
    private Context context;

    public RecyclerAdapter(Context context, List<ChannalIemBean> mList) {
        this.items = mList;
        this.context = context;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (items.size() == position) {
            holder.showProgress();
            return;
        }
        holder.hidenProgress();
        holder.intro.setText(/*Config.SPACE + */items.get(position).getIntro());
        holder.nickname.setText(items.get(position).getNickname());
        holder.id.setText(items.get(position).getId());
        holder.subscriptionNum.setText(Config.SUBSCRIPTION_NUM + items.get(position).getSubscriptionNum());
        GlideUtil.bindImage(context, items.get(position).getAvatar(), holder.iv);//封装加载图片的Glide方法 设置占位图
    }

    @Override
    public int getItemCount() {
        return null == items ? 0 : items.size() + footViewCount;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv = null;
        public TextView intro = null;
        private TextView nickname = null;
        public TextView id = null;
        public TextView subscriptionNum = null;
        public LinearLayout progressBar = null;
        public LinearLayout content = null;

        public void showProgress() {
            progressBar.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
        }

        public void hidenProgress() {
            progressBar.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        }

        public ViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.item_iv);
            intro = (TextView) view.findViewById(R.id.intro);
            nickname = (TextView) view.findViewById(R.id.nickname);
            id = (TextView) view.findViewById(R.id.item_tv_id);
            subscriptionNum = (TextView) view.findViewById(R.id.subscriptionNum);
            progressBar = (LinearLayout) view.findViewById(R.id.load_progress_bar);
            content = (LinearLayout) view.findViewById(R.id.contain);

        }

    }
}

