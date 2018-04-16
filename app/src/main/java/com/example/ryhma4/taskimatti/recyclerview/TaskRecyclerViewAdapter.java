package com.example.ryhma4.taskimatti.recyclerview;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.model.Task;
import com.example.ryhma4.taskimatti.recyclerview.TaskFragment.OnListFragmentInteractionListener;

import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerViewEmptySupport.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

    private final List<Task> mValues;
    private final OnListFragmentInteractionListener mListener;
    private int lastPosition = -1;

    public TaskRecyclerViewAdapter(List<Task> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTime());
        holder.mContentView.setText(mValues.get(position).getName());
        holder.mDescriptionView.setText(mValues.get(position).getDescription());
        setAnimation(holder.itemView, position); // Animate the appearance of the card

        // This is unnecessary because it's handled with long click
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public class ViewHolder extends RecyclerViewEmptySupport.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mDescriptionView;
        public final CardView taskCardView;
        public final AppCompatImageView arrowIconDown, arrowIconUp;
        public Task mItem;

        public ViewHolder(final View view) {
            super(view);

            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
            mDescriptionView = view.findViewById(R.id.description);
            taskCardView = view.findViewById(R.id.taskCardView);
            mDescriptionView.setVisibility(View.GONE);

            arrowIconDown = view.findViewById(R.id.arrowIconDown);
            arrowIconUp = view.findViewById(R.id.arrowIconUp);
            arrowIconDown.setVisibility(View.VISIBLE);

            taskCardView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (mDescriptionView.getVisibility() == View.GONE) {
//                        TransitionManager.beginDelayedTransition(taskCardView);
                        mDescriptionView.setVisibility(View.VISIBLE);
                        arrowIconDown.setVisibility(View.GONE);
                        arrowIconUp.setVisibility(View.VISIBLE);
                    } else {
                        mDescriptionView.setVisibility(View.GONE);
                        arrowIconDown.setVisibility(View.VISIBLE);
                        arrowIconUp.setVisibility(View.GONE);
                    }
                }
            });

            taskCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(mItem);
                    }
                    return false;
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
