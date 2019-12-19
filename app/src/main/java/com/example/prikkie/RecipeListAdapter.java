package com.example.prikkie;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prikkie.Api.recipe_api.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {
    private ArrayList<Recipe> m_recipes;
    private OnItemClickListener m_Listener;
    private MainActivity mainActivity;

    public void setRecipes(ArrayList<Recipe> recipes){
        m_recipes = recipes;
        this.notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        m_Listener = listener;
    }

    public class RecipeListViewHolder extends RecyclerView.ViewHolder {
        public ImageView m_imageView;
        public TextView m_title;
        public TextView m_ingredients;

        public RecipeListViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            m_imageView = itemView.findViewById(R.id.imageView);
            m_title = itemView.findViewById(R.id.recipeTitle);
            m_ingredients = itemView.findViewById(R.id.recipeIngredients);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                            RecipeDetails.setRecipe(m_recipes.get(position));
                            setFragment(RecipeDetails.getFragment());
                        }
                    }
                }
            });
        }
    }

    public void setFragment(Fragment fragment) {
        mainActivity.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.frame_container, fragment).commit();
    }

    public RecipeListAdapter(ArrayList<Recipe> recipes, Activity activity) {
        mainActivity = (MainActivity) activity;
        m_recipes = recipes;
    }

    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        RecipeListViewHolder viewHolder = new RecipeListViewHolder(v, m_Listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeListViewHolder holder, int position) {
        Recipe currentItem = m_recipes.get(position);

        Picasso.get().load(currentItem.imagePath).into(holder.m_imageView);
        holder.m_title.setText(currentItem.title);
        holder.m_ingredients.setText(currentItem.ingredientsToString());
    }

    @Override
    public int getItemCount() {
        if(m_recipes != null) {
            return m_recipes.size();
        }
        return 0;
    }
}