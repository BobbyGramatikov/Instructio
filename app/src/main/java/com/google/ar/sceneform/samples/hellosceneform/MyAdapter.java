package com.google.ar.sceneform.samples.hellosceneform;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

   private ArrayList<Object> mObjectList;
   private OnItemClickListener mListener;
   private MyAdapter myAdapter;

   private View.OnClickListener hs;

    public interface OnItemClickListener{
        void onItemClick(int Position);
        void onRotationClick(int Position);
        void onPositionClick(int Position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView1;
        public ImageView mRotationImageView;
        public ImageView mPositionImageView;



        public ViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewObject);
            mRotationImageView = itemView.findViewById(R.id.imageViewRotation);
            mPositionImageView = itemView.findViewById(R.id.imageViewPosition);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick((position));
                        }
                    }
                }
            });
            mRotationImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            listener.onRotationClick((position));
                        }
                    }
                }
            });
            mPositionImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            listener.onPositionClick((position));
                        }
                    }
                }
            });
           /* mRotationImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            if (listener != null){
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
                listener.onDe
                }
            }
                }
            } */
        }
    }


    public MyAdapter(ArrayList<Object> objectList, View.OnClickListener hs){
        mObjectList = objectList;
        this.hs = hs;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        ViewHolder vh = new ViewHolder(v,mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Object currentObject = mObjectList.get(position);
        String s = "Type: " + currentObject.objectType.toString() + " ID: " + currentObject.objectID + " Current Step: " + currentObject.stepID ;
        holder.mTextView1.setText(s);
    }


    @Override
    public int getItemCount() {
        return mObjectList.size();
    }
}