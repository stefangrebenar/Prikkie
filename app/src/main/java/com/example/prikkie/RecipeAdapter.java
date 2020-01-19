package com.example.prikkie;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prikkie.Api.recipe_api.Recipe;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private ArrayList<Recipe> m_recipes;
    private OnItemClickListener mListener;
    private MainActivity mainActivity;
    private WeeklyPlannerFragment weeklyPlannerFragment;
    public EditText budgetHolder;
    public EditText daysHolder;
    DecimalFormat df = new DecimalFormat("#.##");

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        public EditText budgetHolder;
        public EditText daysHolder;
        public ImageView mImageView;
        public TextView mTopText;
        public TextView mBottomText;
        public Button mRefreshButton;
        public ProgressBar m_loader;

        public RecipeViewHolder(View itemView, final OnItemClickListener listener, EditText budgetText, EditText daysText) {
            super(itemView);
            budgetHolder = budgetText;
            daysHolder = daysText;
            m_loader = itemView.findViewById(R.id.progressBarWeeklyItem);
            m_loader.setVisibility(View.INVISIBLE);
            mImageView = itemView.findViewById(R.id.recipeImage);
            mTopText = itemView.findViewById(R.id.topText);
            mBottomText = itemView.findViewById(R.id.bottomText);
            mRefreshButton = itemView.findViewById(R.id.refreshButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                            RecipeDetails rd = RecipeDetails.getFragment();
                            rd.setRecipe(m_recipes.get(position));
                            rd.PreviousFragment = RecipeDetails.Fragment.WEEKLYPLANNER;
                            setFragment(rd);
                        }
                    }
                }
            });
        }

        public void setFragment(Fragment fragment) {
            try{
                mainActivity.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container, fragment).commit();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public RecipeAdapter(ArrayList<Recipe> exampleList, MainActivity mainActivity, WeeklyPlannerFragment weeklyPlannerFragment, EditText budgetHolder, EditText daysHolder) {
        this.mainActivity = mainActivity;
        this.weeklyPlannerFragment = weeklyPlannerFragment;
        this.budgetHolder = budgetHolder;
        this.daysHolder = daysHolder;
        m_recipes = exampleList;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weeklyrecipe_item, parent, false);
        RecipeViewHolder evh = new RecipeViewHolder(v, mListener, budgetHolder, daysHolder);
        return evh;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, final int position) {
        final Recipe currentItem = m_recipes.get(position);

        Log.d("TEST", currentItem.title + " : " + currentItem.imagePath);
        Picasso.get()
                .load(currentItem.imagePath)
                .fit()
                .centerCrop()
                .into(holder.mImageView);
        holder.mTopText.setText(currentItem.title);
        holder.mBottomText.setText("€" + df.format(currentItem.price));
        holder.mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = currentItem.title;
                Toast msg = Toast.makeText(mainActivity, title + " wordt vervangen voor een ander recept", Toast.LENGTH_SHORT);
                msg.show();
                weeklyPlannerFragment.Refresh(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return m_recipes.size();
    }
}