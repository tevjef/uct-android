package com.tevinjeffrey.rutgersct.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;

import java.util.List;

import butterknife.ButterKnife;

public class CourseFragmentAdapter extends RecyclerView.Adapter<CourseFragmentAdapter.CourseVH>{

    private List<Course> courseList;
    private ItemClickListener itemClickListener;

    public CourseFragmentAdapter(List<Course> courseList, @NonNull ItemClickListener listener) {
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
                itemClickListener.itemClicked(course, v, holder.getAdapterPosition());
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

    public static final class CourseVH extends RecyclerView.ViewHolder {

        private final View mParent;
        private final TextView mCourseTitle;
        private TextView mSectionInfo;

        public static CourseVH newInstance(View parent) {

            TextView courseTitle = ButterKnife.findById(parent, R.id.list_item_title);
            TextView sectionInfo = ButterKnife.findById(parent, R.id.course_list_sections);

            return new CourseVH(parent, courseTitle, sectionInfo);
        }

        public CourseVH(View parent, TextView courseTitle, TextView sectionInfo) {
            super(parent);
            this.mParent = parent;
            this.mSectionInfo = sectionInfo;
            this.mCourseTitle = courseTitle;
        }

        private void setCourseTitle(Course course) {
            mCourseTitle.setText(course.getCourseNumber() + " | " + course.getTrueTitle());
        }

        private void setSectionsInfo(Course course) {
            mSectionInfo.setText(course.getOpenSections() + " open sections of " + course.getSectionsTotal());
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mParent.setOnClickListener(listener);
        }

    }

    public interface ItemClickListener {
        void itemClicked(Course course, View view, int positon);
    }
}
