package com.tevinjeffrey.rutgersct.ui.courseinfo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.data.model.Section;
import com.tevinjeffrey.rutgersct.ui.utils.HeaderVH;
import com.tevinjeffrey.rutgersct.ui.utils.ItemClickListener;
import java.math.BigInteger;
import java.util.List;

public class CourseInfoFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int TYPE_HEADER = 0;
  private static final int TYPE_ITEM = 1;

  private final List<Section> sectionList;

  private final ItemClickListener<Section, View> itemClickListener;
  private final List<View> mHeaders;

  public CourseInfoFragmentAdapter(
      List<View> headers,
      List<Section> sections,
      @NonNull ItemClickListener<Section, View> listener) {
    this.mHeaders = headers;
    this.itemClickListener = listener;
    this.sectionList = sections;
    setHasStableIds(true);
  }

  @Override
  public int getItemCount() {
    return sectionList.size() + 1;
  }

  @Override
  public long getItemId(int position) {
    if (position != 0) {
      return new BigInteger(sectionList.get(position - 1).topic_id).longValue();
    } else {
      return 0;
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (position == 0) {
      return TYPE_HEADER;
    } else {
      return TYPE_ITEM;
    }
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    if (holder.getItemViewType() == TYPE_HEADER && position == 0) {
      HeaderVH headerVH = (HeaderVH) holder;
      headerVH.setHeaders(mHeaders);
    } else if (holder.getItemViewType() == TYPE_ITEM) {
      CourseInfoVH courseInfoVH = (CourseInfoVH) holder;
      final Section section = sectionList.get(position - 1);

      courseInfoVH.setOpenStatus(section);
      courseInfoVH.setSectionNumber(section);
      courseInfoVH.setInstructors(section);
      courseInfoVH.setTimes(section);

      courseInfoVH.setOnClickListener(v -> itemClickListener.onItemClicked(section, v));
    }
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    final Context context = viewGroup.getContext();

    if (viewType == TYPE_HEADER) {
      final LinearLayout parent = (LinearLayout) LayoutInflater
          .from(context)
          .inflate(R.layout.course_info_metadata_container, viewGroup, false);
      return HeaderVH.newInstance(parent);
    } else if (viewType == TYPE_ITEM) {
      final View parent =
          LayoutInflater.from(context).inflate(R.layout.section_layout, viewGroup, false);
      return CourseInfoVH.newInstance(parent);
    }
    return null;
  }
}
