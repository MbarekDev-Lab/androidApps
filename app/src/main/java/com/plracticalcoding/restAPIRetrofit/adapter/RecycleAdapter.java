package com.plracticalcoding.restAPIRetrofit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plracticalcoding.myapplication.R;
import com.plracticalcoding.restAPIRetrofit.data.model.ModelClass;

import org.w3c.dom.Text;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ModelClass> data;

    public RecycleAdapter(List<ModelClass> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclviewlaoutitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ModelClass modelClass = data.get(position);
        viewHolder.textView1.setText(" " + modelClass.getUserId());
        viewHolder.textView2.setText(" " + modelClass.getId());
        viewHolder.textView3.setText(" " + modelClass.getTitle());
        viewHolder.textView4.setText(" " + modelClass.getSubString());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView6);
            textView2 = itemView.findViewById(R.id.textView7);
            textView3 = itemView.findViewById(R.id.textView8);
            textView4 = itemView.findViewById(R.id.textView9);


        }
    }


}
