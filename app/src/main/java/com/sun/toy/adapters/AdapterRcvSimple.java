package com.sun.toy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sun.toy.R;

/**
 * Created by hatti on 2016-04-22.
 */
public class AdapterRcvSimple extends AdapterRCVBase {

    public AdapterRcvSimple() {
    }

    public AdapterRcvSimple(int idLayout) {
        super();
        this.idLayout = idLayout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View child = View.inflate(parent.getContext(), R.layout.item_rcv_simple, null);

        holder = new ViewHolder(child);
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        String str = "";
        if (mList.size() > 0) {
            str = (String) mList.get(position);
        } else if (mMap.size() > 0) {
            str = (String) mMap.get(position);
        }
        ((ViewHolder) holder).txt.setText(str);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onRCVItemListener != null) {
                    onRCVItemListener.onItemClick(v, position);
                }
            }
        });
        super.onBindViewHolder(holder, position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt = null;

        public ViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.txt);
        }
    }
}