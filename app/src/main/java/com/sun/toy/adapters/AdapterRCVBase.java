package com.sun.toy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sunje on 2016-04-19.
 */
public class AdapterRCVBase extends RecyclerView.Adapter {
    protected ArrayList mList = new ArrayList();
    protected HashMap mMap = new HashMap();
    protected int idLayout = -1;
    protected RecyclerView.ViewHolder holder = null;

    public Object getItem(int position) {
        if (mList == null || mList.size() < 1) {
            return null;
        }
        return mList.get(position);
    }

    public interface OnRCVItemListener {
        void onItemClick(View view, int position);
    }

    protected OnRCVItemListener onRCVItemListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size() > mMap.size() ? mList.size() : mMap.size();

    }

    public void setList(ArrayList list) {
        if (list == null) {
            return;
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }
    public void setMap(HashMap map) {
        if (map == null) {
            return;
        }
        mMap.clear();
        mMap.putAll(map);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRCVItemListener onRCVItemListener) {
        this.onRCVItemListener = onRCVItemListener;
    }
}
