package com.example.prikkie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {
    private ArrayList<ExampleItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTopText;
        public TextView mBottomText;
        public CheckBox mCheckBox;

        public ShoppingListViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTopText = itemView.findViewById(R.id.topText);
            mBottomText = itemView.findViewById(R.id.bottomText);
            mCheckBox = itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ShoppingListAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppinglist_item, parent, false);
        ShoppingListViewHolder evh = new ShoppingListViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ShoppingListViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);

        Picasso.get().load(currentItem.getImageResource()).resize(50,50).into(holder.mImageView);
        holder.mTopText.setText(currentItem.getTopText());
        holder.mBottomText.setText(currentItem.getBottomText());
        holder.mCheckBox.setChecked(currentItem.getChecked());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}