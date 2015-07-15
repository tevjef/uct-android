package com.tevinjeffrey.rutgersct.adapters.holders;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.customviews.CircleView;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.SectionUtils;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

public class SectionInfoVH extends RecyclerView.ViewHolder {

    final public View mParent;
    final public TextView mInstructors;
    final public CircleView mSectionNumberBackground;
    final public ViewGroup mSectionTimeContainer;

    public static SectionInfoVH newInstance(View parent) {
        TextView instructors = ButterKnife.findById(parent, R.id.prof_text);
        CircleView sectionNumberBackground = ButterKnife.findById(parent, R.id.section_number_background);
        ViewGroup sectionTimeContainer = ButterKnife.findById(parent, R.id.section_item_time_container);

        return new SectionInfoVH(parent, instructors, sectionNumberBackground, sectionTimeContainer);
    }

    public SectionInfoVH(View parent, TextView instructors, CircleView sectionNumberBackground, ViewGroup mSectionTimeContainer) {
        super(parent);
        this.mParent = parent;
        this.mInstructors = instructors;
        this.mSectionNumberBackground = sectionNumberBackground;
        this.mSectionTimeContainer = mSectionTimeContainer;
    }

    public void setTimes(Course.Sections section) {
        TextView mDayText;
        TextView mSectionLocationText;
        TextView mTimeText;

        final List<Course.Sections.MeetingTimes> meetingTimes = section.getMeetingTimes();
        //sort times so that Monday > Tuesday and Lecture > Recitation
        Collections.sort(meetingTimes);

        for(int i = 0; i < mSectionTimeContainer.getChildCount(); i++) {
            View timeLayout = mSectionTimeContainer.getChildAt(i);

            Course.Sections.MeetingTimes time = null;
            if (meetingTimes.size() > 0 && meetingTimes.size() - 1 >= i) {
                time = meetingTimes.get(i);
            }

            if (time == null) {
                timeLayout.setVisibility(View.GONE);
            } else {
                mDayText = (TextView) timeLayout.findViewById(R.id.section_item_time_info_day_text);
                mSectionLocationText = (TextView) timeLayout.findViewById(R.id.section_item_time_info_location_text);
                mTimeText = (TextView) timeLayout.findViewById(R.id.section_item_time_info_meeting_time_text);


                timeLayout.setVisibility(View.VISIBLE);
                //This is a reused layout that contains inforation not to be shown at this time.
                // The class location is not to be shown in the Tracked Section Fragment
                //timeLayout.findViewById(R.id.section_item_time_info_meeting_type).setVisibility(View.GONE);
                if (time.isByArrangement()) {
                    mDayText.setText(SectionUtils.getMeetingDayName(time));
                    mTimeText.setText(SectionUtils.getMeetingHours(time));
                    mSectionLocationText.setText("");
                } else {
                    mDayText.setText(SectionUtils.getMeetingDayName(time));
                    mTimeText.setText(SectionUtils.getMeetingHours(time));
                    mSectionLocationText.setText(SectionUtils.getClassLocation(time));
                }
            }

        }

    }

    public void setSectionNumber(Course.Sections section) {
        mSectionNumberBackground.setTitleText(section.getNumber());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        mSectionNumberBackground.setTransitionName(section.getNumber());
    }

    public void setInstructors(Course.Sections section) {
        mInstructors.setText(section.getToStringInstructors(" | "));
    }

    public void setOpenStatus(Course.Sections section) {
        if (section.isOpenStatus()) {
            mSectionNumberBackground.setBackgroundColor(mParent.getResources().getColor(R.color.green));
        } else {
            mSectionNumberBackground.setBackgroundColor(mParent.getResources().getColor(R.color.red));
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mParent.setOnClickListener(listener);
    }

}