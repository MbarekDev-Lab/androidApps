package com.plracticalcoding.Tack_Note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plracticalcoding.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    public List<Note> notes = new ArrayList<>();
    private OnItemClickedLisner listener;

    /*public NoteAdapter(List<Note> notes) {
        this.notes = notes;
    }*/


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note = notes.get(position);

        holder.title.setText(note.getTitel());
        holder.desc.setText(note.getDescription());

    }

    @Override
    public int getItemCount() {
       return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public Note getNotes(int position){
        return notes.get(position);
    }

    public  class NoteHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc ;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewtitle);
            desc = itemView.findViewById(R.id.textViewDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                     if (listener != null && position != RecyclerView.NO_POSITION){
                         listener.onItemClicked(notes.get(position));
                     }

                }
            });
        }
    }

    public interface OnItemClickedLisner{
        void onItemClicked(Note note);
    }

    public void setOnItemClickedListener(OnItemClickedLisner listener){
        this.listener = listener;
    }

}
