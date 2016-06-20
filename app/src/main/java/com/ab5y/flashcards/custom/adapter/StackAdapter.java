package com.ab5y.flashcards.custom.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ab5y.flashcards.R;
import com.ab5y.flashcards.model.Stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Abhay on 9/6/2016.
 */
public class StackAdapter extends ArrayAdapter<Stack> {

    private final Context mContext;
    private final List<Stack> mStacks;
    private final List<Stack> mStacks_All;
    private final List<Stack> mStacks_Suggestions;
    private final int mLayoutResrouceId;

    public StackAdapter(Context context, int resource, List<Stack> stackList) {
        super(context, resource, stackList);
        this.mContext = context;
        this.mLayoutResrouceId = resource;
        this.mStacks = new ArrayList<>(stackList);
        this.mStacks_All = new ArrayList<>(stackList);
        this.mStacks_Suggestions = new ArrayList<>();
    }

    public int getCount() {
        return mStacks.size();
    }

    public Stack getItem(int position) {
        return mStacks.get(position);
    }

    public long getItemID(int position) {
        return mStacks.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResrouceId, parent, false);
            }
            Stack stack = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.textView);
            name.setText(stack.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((Stack) resultValue).getName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    mStacks_Suggestions.clear();
                    for (Stack stack : mStacks_All) {
                        if(stack.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            mStacks_Suggestions.add(stack);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mStacks_Suggestions;
                    filterResults.count = mStacks_Suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mStacks.clear();
                if (results != null && results.count > 0) {
                    // avoids warning
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof Stack) {
                            mStacks.add((Stack) object);
                        }
                    }
                } else if (constraint == null) {
                    mStacks.addAll(mStacks_All);
                }
                notifyDataSetChanged();
            }
        };
    }
}
