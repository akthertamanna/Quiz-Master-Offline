package com.skappsstore.quizmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.skappsstore.quizmaster.R;
import com.skappsstore.quizmaster.encapsu.CategoryList;
import com.skappsstore.quizmaster.helper.Helper;
import com.skappsstore.quizmaster.helper.SaveData;

import java.util.ArrayList;
import java.util.Locale;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<CategoryList> mExampleList;
    private HomeCategoryAdapter.OnItemClickListener mListener;



    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(HomeCategoryAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public HomeCategoryAdapter(Context context, ArrayList<CategoryList> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }

    @Override
    public HomeCategoryAdapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.home_category_adapter, parent, false);
        return new HomeCategoryAdapter.ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HomeCategoryAdapter.ExampleViewHolder holder, int position) {
        CategoryList currentItem = mExampleList.get(position);

        String imageUrl = currentItem.getCategoryImage();

        String creatorName = currentItem.getCategoryTitle().toUpperCase(Locale.ROOT);

        holder.textView_home_category_adapter.setText(creatorName);
//        holder.catagoryID.setText(""+currentItem.getCagetoryID());

        if (!imageUrl.equals("no")){
            try{
//                Picasso.with(mContext).load(imageUrl).fit().centerInside().into(holder.imageView_home_category_adapter);
                Glide.with(mContext).load(imageUrl).centerCrop().placeholder(R.drawable.defaultimage).into(holder.imageView_home_category_adapter);
            } catch (Exception e) {
                e.printStackTrace();
                holder.imageView_home_category_adapter.setImageResource(R.drawable.defaultimage);
            }
        }else{
            holder.imageView_home_category_adapter.setImageResource(R.drawable.defaultimage);
        }




        holder.clickCategoryID.setOnClickListener(v -> {
            SaveData.saveClickedCategoryTitle(currentItem.getCategoryTitle(), mContext);
            SaveData.saveCategoryID(currentItem.getCagetoryID(),mContext);
//            Toast.makeText(mContext, "Current id"+currentItem.getCagetoryID(), Toast.LENGTH_SHORT).show();

//            AppCompatActivity activity = (AppCompatActivity) v.getContext();
//            CategoriesFragment ca = new CategoriesFragment();
//            activity.getSupportFragmentManager().beginTransaction().replace(R.id.homeFrag,ca).addToBackStack(null).commit();

            int bgn , in ,ad;
            bgn = mExampleList.get(position).getBeginnerT();
            in = mExampleList.get(position).getIntermediateT();
            ad = mExampleList.get(position).getAdvanceT();



            if (bgn==0  && in==0 && ad==0){

            }else{
//                    openDialog(bgn,in,ad);
                Helper.quizLevel(bgn,in,ad,mContext);

            }

        });

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView textView_home_category_adapter;
        public RoundedImageView imageView_home_category_adapter;
        private RelativeLayout clickCategoryID;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            imageView_home_category_adapter = itemView.findViewById(R.id.imageView_home_category_adapter);
            textView_home_category_adapter = itemView.findViewById(R.id.textView_home_category_adapter);
            clickCategoryID = itemView.findViewById(R.id.clickCategoryID);

            // mTextViewLikes = itemView.findViewById(R.id.text_view_likes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
