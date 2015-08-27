package com.tevinjeffrey.rutgersct.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public final class HeaderVH extends RecyclerView.ViewHolder {

    private final ViewGroup parent;

    public static HeaderVH newInstance(ViewGroup parent) {
        return new HeaderVH(parent);
    }

    private HeaderVH(ViewGroup itemView) {
        super(itemView);
        this.parent = itemView;
    }

    public void setHeaders(Iterable<View> headers) {
        for (View v : headers) {
            if (v.getParent() == null)
                parent.addView(v);
        }
    }
}
