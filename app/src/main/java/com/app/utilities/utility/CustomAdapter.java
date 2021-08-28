package com.app.utilities.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.utilities.R;

import java.util.List;


public class CustomAdapter extends BaseAdapter {

    final Context context;
    final List<RowItem> rowItems;

    public CustomAdapter(Context context, List<RowItem> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.single_row, null);
            holder = new ViewHolder();
            holder.rowImageView = convertView
                    .findViewById(R.id.rowImageView);
            holder.rowTextView = convertView.findViewById(R.id.rowTextView);

            RowItem row_pos = rowItems.get(position);
            holder.rowImageView.setImageResource(row_pos.getRowImageViewId());
            holder.rowTextView.setText(row_pos.getRowTextView());
            convertView.setTag(holder);
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView rowImageView;
        TextView rowTextView;
    }

}
