package com.tevinjeffrey.rutgerssoc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Subject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;

/**
 * Created by Tevin on 1/14/2015.
 */
public class SubjectAdapter extends ArrayAdapter {

    private Context context;
    private String[] navTitles;
    private TypedArray navIcons;
    private int type;
    private ImageView imgIcon = null;
    private TextView txtTitle = null;
    private ArrayList<Subject> item;



    static class ViewHolder {
        public TextView text;
        public ImageView image;
    }


    public SubjectAdapter(Context context, ArrayList<Subject> item){
        super(context, -1, item);
        this.context = context;
        this.item = item;
    }


    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        ViewHolder viewHolder;

        if (rowView == null) {

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            viewHolder = new ViewHolder();

            rowView = mInflater.inflate(R.layout.subject_name, null);
            viewHolder.text = (TextView) rowView.findViewById(R.id.textView);


            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        String text = item.get(position).getDescription().toLowerCase();
        viewHolder.text.setText(WordUtils.capitalize(text));

        return rowView;

    }

}
