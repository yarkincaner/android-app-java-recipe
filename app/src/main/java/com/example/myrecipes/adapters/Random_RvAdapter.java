package com.example.myrecipes.adapters;

import android.app.Activity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipes.R;
import com.example.myrecipes.dto.Category;

import java.util.ArrayList;

public class Random_RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;
    ArrayList<Category> categories;

    class Random_RvAdapter_ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public Random_RvAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.random_category_tag);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        }
    }

    public Random_RvAdapter(Activity context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
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
        Category category = categories.get(position);
        Random_RvAdapter_ViewHolder viewHolder = (Random_RvAdapter_ViewHolder) holder;
        viewHolder.title.setText(category.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
