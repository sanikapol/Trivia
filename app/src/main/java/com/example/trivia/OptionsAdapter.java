//Assignment Inclass 07
//File Name: Group12_InClass07
//Sanika Pol
//Snehal Kekane
package com.example.trivia;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {

    public static InteractWithTriviaActivity interact;
    private static final String TAG = "demo";
    ArrayList<String> options;


    public OptionsAdapter(ArrayList<String> options, InteractWithTriviaActivity interact) {
        this.options = options;
        this.interact = interact;
    }

    @NonNull
    @Override
    public OptionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item,parent,false);
        OptionsAdapter.ViewHolder viewHolder = new OptionsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsAdapter.ViewHolder holder, int position) {
        String option = options.get(position);
        holder.tv_Options.setText(option);
        holder.pos = position;
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_Options;
        String option;
        int pos;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.option = option;

            tv_Options = (TextView) itemView.findViewById(R.id.tv_Options);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick" + getAdapterPosition());
                    interact.getAnswer(getAdapterPosition());
                }
            });
        }
    }

    public interface InteractWithTriviaActivity{
        public void getAnswer(int position);
    }


}
