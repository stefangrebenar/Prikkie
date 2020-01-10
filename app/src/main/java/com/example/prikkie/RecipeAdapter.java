package com.example.prikkie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private ArrayList<ExampleItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTopText;
        public TextView mBottomText;
        public Button mRefreshButton;
        public ProgressBar m_loader;

        public RecipeViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            m_loader = itemView.findViewById(R.id.progressBarWeeklyItem);
            mImageView = itemView.findViewById(R.id.recipeImage);
            mTopText = itemView.findViewById(R.id.topText);
            mBottomText = itemView.findViewById(R.id.bottomText);
            mRefreshButton = itemView.findViewById(R.id.refreshButton);
        }
    }

    public RecipeAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weeklyrecipe_item, parent, false);
        RecipeViewHolder evh = new RecipeViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, final int position) {
        ExampleItem currentItem = mExampleList.get(position);

        Picasso.get()
                .load(currentItem.getImageResource())
                .fit()
                .centerCrop()
                .into(holder.mImageView);
        holder.mTopText.setText(currentItem.getTopText());
        holder.mBottomText.setText("€" + currentItem.getBottomText());
        holder.mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}