package com.tevinjeffrey.rutgersct.ui.subject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;

import butterknife.ButterKnife;

public final class SubjectVH extends RecyclerView.ViewHolder {

    private final View mParent;
    private final TextView mSubjectTitle;

    public static SubjectVH newInstance(View parent) {

        TextView subjectTitle = ButterKnife.findById(parent, R.id.list_item_title);

        return new SubjectVH(parent, subjectTitle);
    }

    public SubjectVH(View parent, TextView subjectTitle) {
        super(parent);
        this.mParent = parent;
        this.mSubjectTitle = subjectTitle;
    }

    public void setSubjectTitle(Subject subject) {
        String text = subject.number + ": " + subject.name;
        mSubjectTitle.setText(text);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mParent.setOnClickListener(listener);
    }

}
