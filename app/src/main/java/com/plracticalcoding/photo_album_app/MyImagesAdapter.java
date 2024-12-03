package com.plracticalcoding.photo_album_app;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plracticalcoding.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MyImagesAdapter extends RecyclerView.Adapter<MyImagesAdapter.MyImageHolder> {

    List<MyImages> imagesList = new ArrayList<>();
    private OnImageClickeListener lisntener;


    public void setImagesList(List<MyImages> imagesList) {
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }

    public void setLisntener(OnImageClickeListener lisntener) {
        this.lisntener = lisntener;
    }

    public interface OnImageClickeListener {
        void onImageClick(MyImages myImages);
    }


    public MyImages getItemPosition(int positiion) {
        return imagesList.get(positiion);
    }

    @NonNull
    @Override
    public MyImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card,false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card, parent, false);

        return new MyImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyImageHolder holder, int position) {
        MyImages myImages = imagesList.get(position);
        holder.textViewTitle.setText(myImages.getImage_title());
        holder.textViewDesc.setText(myImages.getImage_description());
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(myImages.getImage(), 0, myImages.getImage().length));
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class MyImageHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle, textViewDesc;

        public MyImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewtitel);
            textViewDesc = itemView.findViewById(R.id.textviewdescriptions);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (lisntener != null && position != RecyclerView.NO_POSITION) {
                        lisntener.onImageClick(imagesList.get(position));
                    }

                }
            });

        }
    }

}
