package com.example.noushad.mealmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.noushad.mealmanager.R;
import com.example.noushad.mealmanager.model.Meal;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by noushad on 1/2/18.
 */

public class MealAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Meal> mItems;

    public MealAdapter(Context pContext, List<Meal> pItems) {
        mContext = pContext;
        mItems = pItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.meal_item, parent, false);
        viewHolder = new MealVH(viewItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Meal item = mItems.get(position);
        ((MealVH) holder).bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class MealVH extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView meal;

        public MealVH(View v) {
            super(v);
            date = v.findViewById(R.id.mi_date);
            meal = v.findViewById(R.id.mi_count);

        }

        public void bind(Meal pItem) {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);

            date.setText(dateFormat.format(pItem.getDate()));
            meal.setText(String.valueOf(pItem.getMeal()));
        }
    }
}
