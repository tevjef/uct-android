package com.tevinjeffrey.rutgersct.ui.sectioninfo;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Meeting;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Section;
import com.tevinjeffrey.rutgersct.data.uctapi.model.extensions.Utils;
import com.tevinjeffrey.rutgersct.ui.utils.CircleView;

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

    public void setTimes(Section section) {
        TextView mDayText;
        TextView mSectionLocationText;
        TextView mTimeText;

        final List<Meeting> meetingTimes = section.meetings;
        //sort times so that Monday > Tuesday and Lecture > Recitation

        for (int i = 0; i < mSectionTimeContainer.getChildCount(); i++) {
            View timeLayout = mSectionTimeContainer.getChildAt(i);

            Meeting time = null;
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

                if (TextUtils.isEmpty(time.day)) {
                    mDayText.setText(time.class_type);
                } else {
                    mDayText.setText(time.day);
                }

                if (!TextUtils.isEmpty(time.start_time)) {
                    mTimeText.setText(time.start_time + " - " + time.end_time);
                }

                if (!TextUtils.isEmpty(time.room)) {
                    String locationText = time.room;

                    if (!TextUtils.isEmpty(time.class_type)) {
                        locationText = locationText + "  " + time.class_type;
                    }

                    mSectionLocationText.setText(locationText);
                }

            }
        }
    }

    public void setSectionNumber(Section section) {
        mSectionNumberBackground.setTitleText(section.number);
        mSectionNumberBackground.setTag(section.topic_name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mSectionNumberBackground.setTransitionName(section.topic_name);
    }

    public void setInstructors(Section section) {
        mInstructors.setText(Utils.InstructorUtils.toString(section.instructors));
    }

    public void setOpenStatus(Section section) {
        if (section.status.equals("Open")) {
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