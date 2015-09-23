package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.customviews.CircleView;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course.Section.MeetingTimes;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.SectionUtils;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

public class SectionInfoVH extends RecyclerView.ViewHolder {

    private final View mParent;
    public final TextView mInstructors;
    public final CircleView mSectionNumberBackground;
    public final ViewGroup mSectionTimeContainer;

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

    public void setTimes(Course.Section section) {
        TextView mDayText;
        TextView mSectionLocationText;
        TextView mTimeText;

        final List<MeetingTimes> meetingTimes = section.getMeetingTimes();
        //sort times so that Monday > Tuesday and Lecture > Recitation
        Collections.sort(meetingTimes);

        for (int i = 0; i < mSectionTimeContainer.getChildCount(); i++) {
            View timeLayout = mSectionTimeContainer.getChildAt(i);

            MeetingTimes time = null;
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

    public void setSectionNumber(Course.Section section) {
        mSectionNumberBackground.setTitleText(section.getNumber());
        mSectionNumberBackground.setTag(section.getIndex());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mSectionNumberBackground.setTransitionName(section.getIndex());
    }

    public void setInstructors(Course.Section section) {
        mInstructors.setText(section.getToStringInstructors(" | "));
    }

    public void setOpenStatus(Course.Section section) {
        if (section.isOpenStatus()) {
            mSectionNumberBackground.setBackgroundColor(ContextCompat.getColor(mParent.getContext(), R.color.green));
        } else {
            mSectionNumberBackground.setBackgroundColor(ContextCompat.getColor(mParent.getContext(), R.color.red));
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mParent.setOnClickListener(listener);
    }

    public String getIndexNumber() {
        return (String) mSectionNumberBackground.getTag();
    }
}