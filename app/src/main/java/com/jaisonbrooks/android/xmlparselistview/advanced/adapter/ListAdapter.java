package com.jaisonbrooks.android.xmlparselistview.advanced.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jaisonbrooks.android.xmlparselistview.advanced.R;
import com.jaisonbrooks.android.xmlparselistview.advanced.model.DataFeed;

import java.util.ArrayList;

/**
 * Created by jbrooks on 7/25/2014
 */

public class ListAdapter extends ArrayAdapter<DataFeed> {

    Context context;

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView subtitle;
        TextView description;
        TextView id;
    }

    public ListAdapter(Context context, ArrayList<DataFeed> data) {
        super(context, R.layout.list_item, data);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataFeed data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.subtitle = (TextView) convertView.findViewById(R.id.subtitle);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.id = (TextView) convertView.findViewById(R.id.id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.title.setText(data.getTitle());
        viewHolder.subtitle.setText(data.getSubtitle());
        viewHolder.description.setText(data.getDescription());
        viewHolder.id.setText(Integer.toString(data.getId()));

        // Return the completed view to render on screen
        return convertView;
    }
}