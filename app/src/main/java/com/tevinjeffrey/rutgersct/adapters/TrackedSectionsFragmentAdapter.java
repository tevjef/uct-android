package com.tevinjeffrey.rutgersct.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.adapters.holders.SectionInfoVH;
import com.tevinjeffrey.rutgersct.customviews.CircleView;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;

import java.util.List;

import butterknife.ButterKnife;

public class TrackedSectionsFragmentAdapter extends RecyclerView.Adapter<TrackedSectionsFragmentAdapter.TrackedSectionViewHolder> {

    private List<Course> courseList;
    private ItemClickListener itemClickListener;

    public TrackedSectionsFragmentAdapter(List<Course> courseList, @NonNull ItemClickListener listener) {
        this.courseList = courseList;
        this.itemClickListener = listener;
        setHasStableIds(true);
    }

    @Override
    public TrackedSectionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final Context context = viewGroup.getContext();
        final View parent = LayoutInflater.from(context).inflate(R.layout.section_layout_with_title, viewGroup, false);

        return TrackedSectionViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(final TrackedSectionViewHolder holder, int position) {

        final Course course = courseList.get(position);
        final Course.Sections section = course.getSections().get(0);

        holder.setCourseTitle(course);
        holder.setOpenStatus(section);
        holder.setSectionNumber(section);
        holder.setInstructors(section);
        holder.setTimes(section);

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClicked(course, v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(courseList.get(position).getSections().get(0).getIndex());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static final class TrackedSectionViewHolder extends SectionInfoVH {
        
        private final TextView mCourseTitleText;

        public static TrackedSectionViewHolder newInstance(View parent) {
            SectionInfoVH sectionInfoVH = SectionInfoVH.newInstance(parent);
            TextView courseTitleText = ButterKnife.findById(parent, R.id.course_title_text);
            TextView instructors = sectionInfoVH.mInstructors;
            CircleView sectionNumberBackground = sectionInfoVH.mSectionNumberBackground;
            ViewGroup sectionTimeContainer = sectionInfoVH.mSectionTimeContainer;
            return new TrackedSectionViewHolder(parent, instructors, courseTitleText, sectionNumberBackground, sectionTimeContainer);
        }

        public TrackedSectionViewHolder(View parent, TextView instructors, TextView courseTitleText, CircleView sectionNumberBackground, ViewGroup mSectionTimeContainer) {
            super(parent, instructors, sectionNumberBackground, mSectionTimeContainer);
            this.mCourseTitleText = courseTitleText;
        }
        
        private void setCourseTitle(Course course) {
            mCourseTitleText.setText(course.getSubject() + " | " + course.getTrueTitle());
        }
    }


    public interface ItemClickListener {
        void itemClicked(Course course, View view, int positon);
    }
}
