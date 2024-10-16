package com.skappsstore.quizmaster.adapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.skappsstore.quizmaster.R;
import com.skappsstore.quizmaster.activity.QuizLevel;
import com.skappsstore.quizmaster.encapsu.*;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skappsstore.quizmaster.helper.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<CategoryList> mExampleList;
    private OnItemClickListener mListener;
    private Dialog dialog;;



    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CategoryAdapter(Context context, ArrayList<CategoryList> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.category_adapter, parent, false);


        return new ExampleViewHolder(v);


    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, @SuppressLint("RecyclerView") int position) {



        CategoryList currentItem = mExampleList.get(position);

        String imageUrl = currentItem.getCategoryImage();

        Glide.with(mContext)
                .load(imageUrl)
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.lLSubCategoryBg.setBackground(resource);

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {


                    }
                });

        String creatorName = currentItem.getCategoryTitle();

        holder.catagory_Title.setText(creatorName);

        holder.categoryAdapterRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData.saveClickedCategoryTitle(currentItem.getCategoryTitle(), mContext);
                SaveData.saveCategoryID(currentItem.getCagetoryID(),mContext);

                int bgn , in ,ad;
                bgn = mExampleList.get(position).getBeginnerT();
                in = mExampleList.get(position).getIntermediateT();
                ad = mExampleList.get(position).getAdvanceT();






//                    openDialog(bgn,in,ad);
                    Helper.quizLevel(bgn,in,ad,mContext);



            }
        });


    }

    public void openDialog(int bgn, int in, int ad){

        Button btnBeginner= dialog.findViewById(R.id.btnBeginner);
        Button btnIntermediate = dialog.findViewById(R.id.btnIntermediate);
        Button btnAdvance = dialog.findViewById(R.id.btnAdvance);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        if (bgn==0){
            btnBeginner.setVisibility(View.GONE);
        }else{
            btnBeginner.setVisibility(View.VISIBLE);
        }

        if (in==0){
            btnIntermediate.setVisibility(View.GONE);
        }else{
            btnIntermediate.setVisibility(View.VISIBLE);
        }

        if (ad==0){
            btnAdvance.setVisibility(View.GONE);
        }else{
            btnAdvance.setVisibility(View.VISIBLE);
        }

        btnClose.setOnClickListener(view -> {
            dialog.dismiss();
        });
        btnBeginner.setOnClickListener(view -> {
            SaveData.saveQuizLevel(Helper.easy,mContext);
            goToQuiz();

        });

        btnIntermediate.setOnClickListener(view -> {
            SaveData.saveQuizLevel(Helper.intermediate,mContext);
            goToQuiz();
        });

        btnAdvance.setOnClickListener(view -> {
            SaveData.saveQuizLevel(Helper.advance,mContext);
            goToQuiz();
        });




//        Toast.makeText(this, "You clicked "+savedata.getQuizLevel(subcategory_activity.this), Toast.LENGTH_SHORT).show();

        dialog.show();
    }


    public void goToQuiz(){
        Intent quizlevel = new Intent(mContext, QuizLevel.class);
        dialog.dismiss();
        mContext.startActivity(quizlevel);

    }


    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {

        private TextView catagory_Title,catagoryID;
        //        public CircleImageView mImageView;
        private RelativeLayout categoryAdapterRel;
        private LinearLayout lLSubCategoryBg;

        public ExampleViewHolder(View itemView) {
            super(itemView);
//            mImageView = itemView.findViewById(R.id.image_view_category);
            catagory_Title = itemView.findViewById(R.id.category_Title);
            categoryAdapterRel = itemView.findViewById(R.id.categoryAdapterRel);
            lLSubCategoryBg = itemView.findViewById(R.id.lLSubCategoryBg);

            // mTextViewLikes = itemView.findViewById(R.id.text_view_likes);

            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position);
                    }
                }
            });
        }
    }
}