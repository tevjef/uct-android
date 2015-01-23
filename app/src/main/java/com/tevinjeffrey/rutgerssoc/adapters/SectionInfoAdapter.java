package com.tevinjeffrey.rutgerssoc.adapters;

import android.content.Context;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tevinjeffrey.rutgerssoc.R;
import com.tevinjeffrey.rutgerssoc.model.Course;
import com.tevinjeffrey.rutgerssoc.utils.CourseUtils;
import com.tevinjeffrey.rutgerssoc.utils.SectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by Tevin on 1/14/2015.
 */
public class SectionInfoAdapter {

    private Context context;
    private Course course;
    private View rowView;



    private TextView preReqNotes;
    private View subjectNotesContainer;
    private View courseNotesContainer;
    private View preReqNotesContainer;
    private LinearLayout sectionContainer;

    public SectionInfoAdapter(Context context, Course item, View rowView) {
        this.context = context;
        this.course = item;
        this.rowView = rowView;

        init();
    }

    private void init() {


    }


}
