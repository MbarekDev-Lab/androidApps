package com.plracticalcoding.fragments.fragment_ope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plracticalcoding.fragments.model.ModelClass;
import com.plracticalcoding.myapplication.R;

public class InfoBookAdapter extends RecyclerView.Adapter<InfoBookAdapter.InfoBookViewHolder> {

    private final ModelClass[] modelClasses;
    private Context context;

    public InfoBookAdapter(ModelClass[] modelClasses, Context context) {
        this.modelClasses = modelClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public InfoBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design, parent, false);
        return new InfoBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoBookViewHolder holder, int position) {
        ModelClass currentItem = modelClasses[position];
        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getText());
    }

    @Override
    public int getItemCount() {
        return modelClasses.length;
    }

    public static class InfoBookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public InfoBookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagevfragmentcard);
            textView = itemView.findViewById(R.id.cardTextView);
        }
    }
}
