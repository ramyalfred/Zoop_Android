package com.example.stockup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stockup.R;
import com.example.stockup.model.Habit;

import java.util.ArrayList;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<Habit> mHabits;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTextView;
        public TextView streakTextView;

        public ViewHolder(View itemView){
            super(itemView);

            nameTextView = itemView.findViewById(R.id.habit_name);
            streakTextView = itemView.findViewById(R.id.habit_streak);
        }

    }

    public RecyclerAdapter(ArrayList<Habit> habitList){
        this.mHabits = habitList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Obtaining the LayoutInflater from the running activity
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Retuning a new ViewHolder after handing it a recycler item view
        View v = inflater.inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Getting the habit at that position
        Habit habit = mHabits.get(position);

        //Setting the actual values of the views
        holder.nameTextView.setText(habit.getHabitName());
        holder.streakTextView.setText(Integer.toString(habit.getHabitStreak()));
    }

    @Override
    public int getItemCount() {
        return mHabits.size();
    }
}
