package com.tevinjeffrey.rutgerssoc.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

public class CourseAdapter extends ArrayAdapter {

    private final Context context;
    private final List<Course> item;

    public CourseAdapter(Context context, List<Course> item) {
        //noinspection unchecked
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

            rowView = mInflater.inflate(R.layout.course_name, null);

            viewHolder.courseTitle = (TextView) rowView.findViewById(R.id.course_list_title);
            viewHolder.courseSectionsOpen = (TextView) rowView.findViewById(R.id.course_list_sections);


            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        Course course = item.get(position);

        viewHolder.courseTitle.setText(WordUtils.capitalize(course.getTrueTitle().toLowerCase()));
        viewHolder.courseSectionsOpen.setText(course.getOpenSections() + " open sections of " + course.getSections().size());

        return rowView;

    }

    static class ViewHolder {
        public TextView courseTitle;
        public TextView courseSectionsOpen;

    }

}
