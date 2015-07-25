package com.tevinjeffrey.rutgersct.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.adapters.holders.TrackedSectionVH;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;

import java.util.List;

public class TrackedSectionsFragmentAdapter extends RecyclerView.Adapter<TrackedSectionVH> {

    private final List<Course.Section> sectionList;
    private final ItemClickListener itemClickListener;

    public TrackedSectionsFragmentAdapter(List<Course.Section> sectionList, @NonNull ItemClickListener listener) {
        this.sectionList = sectionList;
        this.itemClickListener = listener;
        setHasStableIds(true);
    }

    @Override
    public TrackedSectionVH onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final Context context = viewGroup.getContext();
        final View parent = LayoutInflater.from(context).inflate(R.layout.section_layout_with_title, viewGroup, false);

        return TrackedSectionVH.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(final TrackedSectionVH holder, int position) {

        final Course.Section section = sectionList.get(position);

        holder.setCourseTitle(section.getCourse());
        holder.setOpenStatus(section);
        holder.setSectionNumber(section);
        holder.setInstructors(section);
        holder.setTimes(section);

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(section, v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(sectionList.get(position).getIndex());
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }


    public interface ItemClickListener {
        void onItemClicked(Course.Section section, View view, int positon);
    }
}
