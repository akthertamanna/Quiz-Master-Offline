package com.skappsstore.quizgame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skappsstore.quizgame.R;
import com.skappsstore.quizgame.database.SaveData;
import com.skappsstore.quizgame.model.CategoryList;
import com.skappsstore.quizgame.utilities.Helper;
import com.skappsstore.quizgame.utilities.ImageResourceHelper;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ExampleViewHolder> {
    private final Context mContext;
    private final ArrayList<CategoryList> categoryLists;

    public CategoryAdapter(Context context, ArrayList<CategoryList> exampleList) {
        mContext = context;
        categoryLists = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.category_adapter, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CategoryList currentItem = categoryLists.get(position);
        int imageID = ImageResourceHelper.getImageResourceByCategory(currentItem.getCategoryImage());
        holder.lLSubCategoryBg.setBackgroundResource(imageID);

        String creatorName = currentItem.getCategoryTitle();

        holder.categoryTitle.setText(creatorName);

        holder.categoryAdapterRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData.saveClickedCategoryTitle(currentItem.getCategoryTitle(), mContext);
                SaveData.saveCategoryID(currentItem.getCategoryID(),mContext);

                Helper.quizLevel(
                        categoryLists.get(position).getBeginner(),
                        categoryLists.get(position).getIntermediate(),
                        categoryLists.get(position).getAdvanced(),
                        mContext
                );

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryTitle;
        private final RelativeLayout categoryAdapterRel;
        private final LinearLayout lLSubCategoryBg;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            categoryTitle = itemView.findViewById(R.id.category_Title);
            categoryAdapterRel = itemView.findViewById(R.id.categoryAdapterRel);
            lLSubCategoryBg = itemView.findViewById(R.id.lLSubCategoryBg);

        }
    }
}