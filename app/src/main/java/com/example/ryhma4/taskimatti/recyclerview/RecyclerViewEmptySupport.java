package com.example.ryhma4.taskimatti.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * This class is to get setEmptyView method to RecyclerView, which allows to create a view if there is no content available.
 */
public class RecyclerViewEmptySupport extends RecyclerView {
    private View emptyView;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            TaskRecyclerViewAdapter adapter = (TaskRecyclerViewAdapter) getAdapter();
            if (adapter != null && emptyView != null) {
                if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    RecyclerViewEmptySupport.this.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public RecyclerViewEmptySupport(Context context) {
        super(context);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}
