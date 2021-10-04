package com.example.myrecipes.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipes.R;
import com.example.myrecipes.dto.Category;
import com.example.myrecipes.dto.Singleton;

import java.util.ArrayList;

public class Random_RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;
    static Singleton singleton;

    class Random_RvAdapter_ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        boolean isActive;

        public Random_RvAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.random_category_tag);
            isActive = false;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            if (active) {
                activate();
            } else {
                disable();
            }

            isActive = active;
        }

        private void activate() {
            singleton.getCategory(title.getText().toString()).setActive(true);
            title.setTextColor(Color.WHITE);
            title.setBackgroundResource(R.color.design_default_color_primary);
        }

        private void disable() {
            singleton.getCategory(title.getText().toString()).setActive(false);
            title.setTextColor(Color.BLACK);
            title.setBackgroundColor(Color.WHITE);
        }
    }

    public Random_RvAdapter(Activity context) {
        this.context = context;
        singleton = Singleton.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.random_category, parent, false);

        return new Random_RvAdapter_ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Category category = singleton.getCategories().get(position);
        Random_RvAdapter_ViewHolder viewHolder = (Random_RvAdapter_ViewHolder) holder;
        viewHolder.title.setText(category.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.isActive()) {
                    viewHolder.setActive(false);
                } else {
                    viewHolder.setActive(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return singleton.getCategories().size();
    }
}
