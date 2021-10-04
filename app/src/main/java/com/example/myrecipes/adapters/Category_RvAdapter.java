package com.example.myrecipes.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipes.R;
import com.example.myrecipes.dto.Category;
import com.example.myrecipes.dto.Singleton;

import java.util.ArrayList;

public class Category_RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity context;
    static Singleton singleton = Singleton.getInstance();

    class Category_RvAdapter_ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public Category_RvAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
//            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.category_title);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        }

//        @Override
//        public void onClick(View v) {
//            System.out.println(getAdapterPosition());
//            System.out.println(categories.get(getAdapterPosition()).getTitle());
//            Navigation.createNavigateOnClickListener(R.id.action_bottom_nav_bar_home_to_recipesFragment).onClick(v);
//        }
    }

    public Category_RvAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.category, parent, false);

        return new Category_RvAdapter_ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Category category = singleton.getCategories().get(position);
        Category_RvAdapter_ViewHolder viewHolder = (Category_RvAdapter_ViewHolder) holder;
        viewHolder.title.setText(category.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(category.getTitle());
                Bundle bundle = new Bundle();
                bundle.putString("categoryTitle", category.getTitle());
                Navigation.findNavController(v).navigate(R.id.action_bottom_nav_bar_home_to_recipesFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return singleton.getCategories().size();
    }


}
