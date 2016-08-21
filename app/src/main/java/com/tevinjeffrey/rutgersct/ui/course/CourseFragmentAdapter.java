package com.tevinjeffrey.rutgersct.ui.course;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Course;

import java.util.List;

public class CourseFragmentAdapter extends RecyclerView.Adapter<CourseVH> {

    private List<Course> courseList;
    private ItemClickListener<Course, View> itemClickListener;

    public CourseFragmentAdapter(List<Course> courseList, @NonNull ItemClickListener<Course, View> listener) {
        this.courseList = courseList;
        this.itemClickListener = listener;
        setHasStableIds(true);
    }

    @Override
    public CourseVH onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final Context context = viewGroup.getContext();
        final View parent = LayoutInflater.from(context).inflate(R.layout.course_list_item, viewGroup, false);

        return CourseVH.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(final CourseVH holder, int position) {

        final Course course = courseList.get(position);

        holder.setCourseTitle(course);
        holder.setSectionsInfo(course);
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(course, v);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(courseList.get(position).getCourseNumber());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

}
