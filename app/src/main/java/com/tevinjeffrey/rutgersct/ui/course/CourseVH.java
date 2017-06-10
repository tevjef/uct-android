package com.tevinjeffrey.rutgersct.ui.course;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.data.model.Course;
import com.tevinjeffrey.rutgersct.data.model.extensions.Utils;

public final class CourseVH extends RecyclerView.ViewHolder {

  private final View mParent;
  private final TextView mCourseTitle;
  private final TextView mSectionInfo;

  private CourseVH(View parent, TextView courseTitle, TextView sectionInfo) {
    super(parent);
    this.mParent = parent;
    this.mSectionInfo = sectionInfo;
    this.mCourseTitle = courseTitle;
  }

  public static CourseVH newInstance(View parent) {

    TextView courseTitle = ButterKnife.findById(parent, R.id.list_item_title);
    TextView sectionInfo = ButterKnife.findById(parent, R.id.course_list_sections);

    return new CourseVH(parent, courseTitle, sectionInfo);
  }

  public void setCourseTitle(Course course) {
    mCourseTitle.setText(course.number + ":  " + course.name);
  }

  public void setOnClickListener(View.OnClickListener listener) {
    mParent.setOnClickListener(listener);
  }

  public void setSectionsInfo(Course course) {
    mSectionInfo.setText(
        Utils.CourseUtils.getOpenSections(course) + " open sections of " + course.sections.size());
  }
}
