package com.qcit.location.selector.libary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.qcit.location.selector.libary.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KMAdapter extends BaseAdapter implements Filterable {
    private List<PoiItem> mList;
    private Context mContext;
    private MyFilter mFilter;

    public void clear() {
        mList.clear();
    }

    public void addAll(@NotNull List<PoiItem> array) {
        mList.addAll(array);
    }

    class Item {
        TextView tv_title;
        TextView tv_subtitle;
    }

    public KMAdapter(Context context, List<PoiItem> list) {
        mContext = context;
        mList = new ArrayList<PoiItem>();
        mList.addAll(list);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PoiItem getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = new Item();
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.adapter_aotucomp, null);
            item.tv_title = convertView.findViewById(R.id.tv_title);
            item.tv_subtitle = convertView.findViewById(R.id.tv_subtitle);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        item.tv_title.setText(getItem(position).getTitle());
        item.tv_subtitle.setText(getItem(position).getSnippet());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }

//    private class ViewHolder {
//        TextView tv;
//    }

    private class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (mList == null) {
                mList = new ArrayList<PoiItem>();
            }
            results.values = mList;
            results.count = mList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}