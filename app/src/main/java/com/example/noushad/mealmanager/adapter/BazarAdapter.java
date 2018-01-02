package com.example.noushad.mealmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.noushad.mealmanager.R;

import java.util.List;

/**
 * Created by noushad on 1/2/18.
 */

public class BazarAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Integer> mItems;

    public BazarAdapter(Context pContext, List<Integer> pItems) {
        mContext = pContext;
        mItems = pItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.bazar_item, parent, false);
        viewHolder = new BazarVH(viewItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int item = mItems.get(position);
        ((BazarVH) holder).bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class BazarVH extends RecyclerView.ViewHolder {

        private Button bazarButton;

        public BazarVH(View v) {
            super(v);
            bazarButton = v.findViewById(R.id.bazar_button);
        }

        public void bind(int pItem) {
            bazarButton.setText(String.valueOf(pItem));
        }
    }
}
