package com.example.richardantonios.testapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.richardantonios.testapp.R;
import com.example.richardantonios.testapp.models.ItemModel;

import java.util.ArrayList;
import java.util.Random;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<ItemModel> data;
    private Random random;
    private Context context;
    private LayoutInflater layoutInflater;

    private static class ViewHolder {
        private TextView titleTextView;
    }

    public ListViewAdapter(ArrayList<ItemModel> data, Context context) {
        super();
        this.data = data;
        this.context = context;
        random = new Random();
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public ItemModel getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        // generate random color
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = convertView.findViewById(R.id.itemtxt);
            convertView.setTag(viewHolder);

            // set random background color
            convertView.setBackgroundColor(color);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ItemModel model = getItem(position);
        viewHolder.titleTextView.setText(model.getTitle());

        return convertView;
    }

}