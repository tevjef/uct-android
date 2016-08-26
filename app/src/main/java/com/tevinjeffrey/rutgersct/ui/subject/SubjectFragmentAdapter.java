package com.tevinjeffrey.rutgersct.ui.subject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Subject;
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener;

import java.util.List;

public class SubjectFragmentAdapter extends RecyclerView.Adapter<SubjectVH> {

    private final List<Subject> subjectList;
    private final ItemClickListener<Subject, View> itemClickListener;

    public SubjectFragmentAdapter(List<Subject> subjectList, @NonNull ItemClickListener<Subject, View> listener) {
        this.subjectList = subjectList;
        this.itemClickListener = listener;
        setHasStableIds(true);
    }

    @Override
    public SubjectVH onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final Context context = viewGroup.getContext();
        final View parent = LayoutInflater.from(context).inflate(R.layout.subject_list_item, viewGroup, false);

        return SubjectVH.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(final SubjectVH holder, int position) {

        final Subject subject = subjectList.get(position);

        holder.setSubjectTitle(subject);
        holder.setOnClickListener(v -> itemClickListener.onItemClicked(subject, v));
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(subjectList.get(position).topic_name);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

}
