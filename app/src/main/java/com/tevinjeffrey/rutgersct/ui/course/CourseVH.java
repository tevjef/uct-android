package com.tevinjeffrey.rutgersct.ui.course;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Course;

import butterknife.ButterKnife;

public final class CourseVH extends RecyclerView.ViewHolder {

    private final View mParent;
    private final TextView mCourseTitle;
    private final TextView mSectionInfo;

    public static CourseVH newInstance(View parent) {

        TextView courseTitle = ButterKnife.findById(parent, R.id.list_item_title);
        TextView sectionInfo = ButterKnife.findById(parent, R.id.course_list_sections);

        return new CourseVH(parent, courseTitle, sectionInfo);
    }

    private CourseVH(View parent, TextView courseTitle, TextView sectionInfo) {
        super(parent);
        this.mParent = parent;
        this.mSectionInfo = sectionInfo;
        this.mCourseTitle = courseTitle;
    }

    public void setCourseTitle(Course course) {
        mCourseTitle.setText(course.getCourseNumber() + " | " + course.getTrueTitle());
    }

    public void setSectionsInfo(Course course) {
        mSectionInfo.setText(course.getOpenSections() + " open sections of " + course.getSectionsTotal());
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mParent.setOnClickListener(listener);
    }

}
