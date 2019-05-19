/*
package com.example.weerapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weerapp.R;
import com.example.weerapp.model.WeerObject;

import java.util.List;

public class WeerObjectAdapter extends RecyclerView.Adapter<WeerObjectAdapter.ViewHolder>{

    private List<WeerObject> mWeerObjecten;

    public WeerObjectAdapter(List<WeerObject> mWeerObjecten) {
        this.mWeerObjecten = mWeerObjecten;
    }

    @NonNull
    @Override
    public WeerObjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_layout, null);
        // Return a new holder instance
        WeerObjectAdapter.ViewHolder viewHolder = new WeerObjectAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeerObjectAdapter.ViewHolder viewHolder, int i) {
        WeerObject weerobject = mWeerObjecten.get(i);

        //alle attributen binden zoals: viewHolder.titel.setText(game.getTitel());

    }

    @Override
    public int getItemCount() {
        return mWeerObjecten.size();
    }

    public void swapList(List<WeerObject> newList) {
        mWeerObjecten = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //textviews declareren zoals: TextView titel;

        public ViewHolder(View itemView) {
            super(itemView);
            //views binden zoals: titel = itemView.findViewById(R.id.gameTitel);
        }
    }
}
*/
