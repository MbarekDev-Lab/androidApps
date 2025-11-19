package com.plracticalcoding.fragments.fragment_ope.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.plracticalcoding.fragments.fragment_ope.activities.CountriesActivity;
import com.plracticalcoding.fragments.fragment_ope.activities.InfoBookActivity;
import com.plracticalcoding.fragments.fragment_ope.activities.MuseumsActivity;
import com.plracticalcoding.fragments.fragment_ope.activities.WondersActivity;
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
    public void onBindViewHolder(@NonNull InfoBookViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ModelClass currentItem = modelClasses[position];
        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getText());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    Intent intent = new Intent(context, CountriesActivity.class);
                    context.startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(context, WondersActivity.class);
                    context.startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(context, MuseumsActivity.class);
                    context.startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(context, InfoBookActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelClasses.length;
    }

    public static class InfoBookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        private  CardView cardView;


        public InfoBookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagevfragmentcard);
            textView = itemView.findViewById(R.id.cardTextView);
           cardView = itemView.findViewById(R.id.cardView);
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Handle the click event here
//                }
//            });
        }
    }
}
