package com.wallstcn.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.wallstcn.bean.ChannalIemBean;

import java.util.ArrayList;

/**
 * Created by _GZL on 2016/3/4.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<String> itemsName = null;
    private View[] viewContainer = null;
    private Context mContext = null;
    private ArrayList<ChannalIemBean>[] items = null;

    public ViewPagerAdapter(Context ctxt, ArrayList<String> channalItem, View[] container, ArrayList<ChannalIemBean>[] Items) {
        mContext = ctxt.getApplicationContext();
        this.viewContainer = container;
        this.itemsName = channalItem;
        this.items = Items;
    }

    @Override
    public int getCount() {
        return itemsName == null ? 0 : itemsName.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewContainer[position]);
        return viewContainer[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewContainer[position]);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return itemsName.get(position);
    }
}
