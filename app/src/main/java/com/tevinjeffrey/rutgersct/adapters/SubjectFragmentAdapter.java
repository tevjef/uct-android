package com.tevinjeffrey.rutgersct.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Subject;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

import butterknife.ButterKnife;

public class SubjectFragmentAdapter extends RecyclerView.Adapter<SubjectFragmentAdapter.SubjectVH>{

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

    public static final class SubjectVH extends RecyclerView.ViewHolder {

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
        
        private void setSubjectTitle(Subject subject) {
            String text = subject.getCode() + " | " + subject.getDescription();
            mSubjectTitle.setText(WordUtils.capitalize(text.toLowerCase()));
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mParent.setOnClickListener(listener);
        }

    }

    public interface ItemClickListener {
        void itemClicked(Subject course, View view, int positon);
    }
}
