package com.tevinjeffrey.rutgersct.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.adapters.holders.HeaderVH;
import com.tevinjeffrey.rutgersct.adapters.holders.SectionInfoVH;
import com.tevinjeffrey.rutgersct.customviews.CircleView;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.SectionUtils;

import java.util.List;

public class CourseInfoFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private Course course;
    final List<Course.Sections> sectionList;
    final private ItemClickListener itemClickListener;
    final private List<View> mHeaders;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public CourseInfoFragmentAdapter(List<View> headers, Course course, @NonNull ItemClickListener listener) {
        this.mHeaders = headers;
        this.course = course;
        this.itemClickListener = listener;
        this.sectionList = course.getSections();
        SectionUtils.scrubSectionList(sectionList);
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final Context context = viewGroup.getContext();

        if (viewType == TYPE_HEADER) {
            final LinearLayout parent = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.course_info_metadata_container, viewGroup, false);
            return HeaderVH.newInstance(parent);
        } else if(viewType == TYPE_ITEM) {
            final View parent = LayoutInflater.from(context).inflate(R.layout.section_layout, viewGroup, false);
            return CourseInfoVH.newInstance(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_HEADER && position == 0) {
            HeaderVH headerVH = (HeaderVH)holder;
            headerVH.setHeaders(mHeaders);
        } else if (holder.getItemViewType() == TYPE_ITEM) {
            CourseInfoVH courseInfoVH = (CourseInfoVH) holder;
            final Course.Sections section = sectionList.get(position - 1);

            courseInfoVH.setOpenStatus(section);
            courseInfoVH.setSectionNumber(section);
            courseInfoVH.setInstructors(section);
            courseInfoVH.setTimes(section);

            courseInfoVH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.itemClicked(course, section, v, holder.getAdapterPosition());
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public long getItemId(int position) {
        if (position != 0)
            return Long.valueOf(sectionList.get(position - 1).getIndex());
        else
            return 0;
    }

    @Override
    public int getItemCount() {
        return sectionList.size() + 1;
    }

    public interface ItemClickListener {
        void itemClicked(Course course, Course.Sections sections, View view, int positon);
    }

    public static final class CourseInfoVH extends SectionInfoVH {

        public static CourseInfoVH newInstance(View parent) {
            SectionInfoVH sectionInfoVH = SectionInfoVH.newInstance(parent);
            TextView instructors = sectionInfoVH.mInstructors;
            CircleView sectionNumberBackground = sectionInfoVH.mSectionNumberBackground;
            ViewGroup sectionTimeContainer = sectionInfoVH.mSectionTimeContainer;

            return new CourseInfoVH(parent, instructors, sectionNumberBackground, sectionTimeContainer);
        }

        public CourseInfoVH(View parent, TextView instructors, CircleView sectionNumberBackground, ViewGroup mSectionTimeContainer) {
            super(parent, instructors, sectionNumberBackground, mSectionTimeContainer);
        }


    }
}
