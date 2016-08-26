package com.tevinjeffrey.rutgersct.ui.trackedsections;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.data.uctapi.search.UCTSubscription;
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener;

import java.util.List;

public class TrackedSectionsFragmentAdapter extends RecyclerView.Adapter<TrackedSectionVH> {

    private final List<UCTSubscription> sectionList;
    private final ItemClickListener<UCTSubscription, View> itemClickListener;

    public TrackedSectionsFragmentAdapter(List<UCTSubscription> sectionList, @NonNull ItemClickListener<UCTSubscription, View> listener) {
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

        final UCTSubscription subscription = sectionList.get(position);

        holder.setCourseTitle(subscription.getSubject().number, subscription.getCourse().name);
        holder.setOpenStatus(subscription.getSection());
        holder.setSectionNumber(subscription.getSection());
        holder.setInstructors(subscription.getSection());
        holder.setTimes(subscription.getSection());

        holder.setOnClickListener(v -> itemClickListener.onItemClicked(subscription, v));
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(sectionList.get(position).getSection().topic_name);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }
}
