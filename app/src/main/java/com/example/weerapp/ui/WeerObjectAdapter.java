package com.example.weerapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weerapp.R;
import com.example.weerapp.model.WeerObject;

import java.util.List;

public class WeerObjectAdapter extends RecyclerView.Adapter<WeerObjectAdapter.ViewHolder>{

    private List<WeerObject> mWeerObjects;

    public WeerObjectAdapter(List<WeerObject> mWeerObjects) {
        this.mWeerObjects = mWeerObjects;
    }

    @NonNull
    @Override
    public WeerObjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_layout, viewGroup, false);
        // Return a new holder instance
        WeerObjectAdapter.ViewHolder viewHolder = new WeerObjectAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeerObjectAdapter.ViewHolder viewHolder, int i) {
        WeerObject weerobject = mWeerObjects.get(i);
        viewHolder.naamStad.setText(weerobject.getNaamStad());
        viewHolder.aantalgraden.setText(weerobject.getMain().getTemp() + "°C");
        viewHolder.datum.setText(weerobject.getDatum());

    }

    @Override
    public int getItemCount() {
        return mWeerObjects.size();
    }

    public void swapList(List<WeerObject> newList) {
        mWeerObjects = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView naamStad;
        TextView aantalgraden;
        TextView datum;

        public ViewHolder(View itemView) {
            super(itemView);
            naamStad = itemView.findViewById(R.id.naamStad);
            aantalgraden = itemView.findViewById(R.id.aantalGraden);
            datum = itemView.findViewById(R.id.datum);
        }
    }
}
