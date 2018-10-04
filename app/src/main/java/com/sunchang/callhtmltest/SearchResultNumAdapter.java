package com.sunchang.callhtmltest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultNumAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<SearchResultNum> mDate = new ArrayList<>();

    public SearchResultNumAdapter(Context context, List<SearchResultNum> mDate) {
        this.mInflater = LayoutInflater.from(context);
        this.mDate = mDate;
    }

    @Override
    public int getCount() {
        return mDate.size();
    }

    @Override
    public Object getItem(int position) {
        return mDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.search_result_item, null);
            holder.mSearchNum = convertView.findViewById(R.id.tv_result_num);
            holder.mBaseUrl = convertView.findViewById(R.id.tv_baseurl);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mSearchNum.setText(mDate.get(position).getResultNum());
        holder.mBaseUrl.setText(mDate.get(position).getBaseUrl());
        return convertView;
    }

    private class ViewHolder {
        private TextView mSearchNum;

        private TextView mBaseUrl;
    }

}
