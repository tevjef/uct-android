package com.tevinjeffrey.rutgersct.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.adapters.holders.SubjectVH;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;

import java.util.List;

public class SubjectFragmentAdapter extends RecyclerView.Adapter<SubjectVH> {

    private List<Subject> subjectList;
    private ItemClickListener itemClickListener;

    public SubjectFragmentAdapter(List<Subject> subjectList, @NonNull ItemClickListener listener) {
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
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClicked(subject, v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(subjectList.get(position).getCode());
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public interface ItemClickListener {
        void itemClicked(Subject course, View view, int positon);
    }
}
