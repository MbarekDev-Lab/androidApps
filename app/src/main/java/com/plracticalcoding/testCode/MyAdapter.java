package com.plracticalcoding.testCode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plracticalcoding.myapplication.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<YourDataModel> dataList;

    public MyAdapter(List<YourDataModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        YourDataModel data = dataList.get(position);
        holder.column1.setText(data.getColumn1());
        holder.column2.setText(data.getColumn2());
        holder.column3.setText(data.getColumn3());
        holder.column4.setText(data.getColumn4());
        holder.column5.setText(data.getColumn5());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView column1, column2, column3, column4, column5;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            column1 = itemView.findViewById(R.id.column1);
            column2 = itemView.findViewById(R.id.column2);
            column3 = itemView.findViewById(R.id.column3);
            column4 = itemView.findViewById(R.id.column4);
            column5 = itemView.findViewById(R.id.column5);
        }
    }
}
