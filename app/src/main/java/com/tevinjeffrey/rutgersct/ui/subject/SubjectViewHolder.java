package com.tevinjeffrey.rutgersct.ui.subject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.data.model.Subject;

public final class SubjectViewHolder extends RecyclerView.ViewHolder {

  private final View mParent;
  private final TextView mSubjectTitle;

  public SubjectViewHolder(View parent, TextView subjectTitle) {
    super(parent);
    this.mParent = parent;
    this.mSubjectTitle = subjectTitle;
  }

  public static SubjectViewHolder newInstance(View parent) {

    TextView subjectTitle = parent.findViewById(R.id.list_item_title);

    return new SubjectViewHolder(parent, subjectTitle);
  }

  public void setOnClickListener(View.OnClickListener listener) {
    mParent.setOnClickListener(listener);
  }

  public void setSubjectTitle(Subject subject) {
    String text = subject.number + ": " + subject.name;
    mSubjectTitle.setText(text);
  }
}
