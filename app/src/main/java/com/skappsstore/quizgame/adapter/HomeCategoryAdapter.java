package com.skappsstore.quizgame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.skappsstore.quizgame.R;
import com.skappsstore.quizgame.model.CategoryList;
import com.skappsstore.quizgame.utilities.Helper;
import com.skappsstore.quizgame.database.SaveData;
import com.skappsstore.quizgame.utilities.ImageResourceHelper;
import java.util.ArrayList;
import java.util.Locale;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ExampleViewHolder> {
    private final Context mContext;
    private final ArrayList<CategoryList> categoryLists;

    public HomeCategoryAdapter(Context context, ArrayList<CategoryList> categoryLists) {
        mContext = context;
        this.categoryLists = categoryLists;
    }

    @NonNull
    @Override
    public HomeCategoryAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.home_category_adapter, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HomeCategoryAdapter.ExampleViewHolder holder, int position) {
        CategoryList currentItem = categoryLists.get(position);

        String creatorName = currentItem.getCategoryTitle().toUpperCase(Locale.ROOT);

        int imageID = ImageResourceHelper.getImageResourceByCategory(currentItem.getCategoryImage());

        holder.textView_home_category_adapter.setText(creatorName);

        holder.imageView_home_category_adapter.setImageResource(imageID);

        holder.clickCategoryID.setOnClickListener(v -> {

            SaveData.saveClickedCategoryTitle(currentItem.getCategoryTitle(), mContext);
            SaveData.saveCategoryID(currentItem.getCategoryID(),mContext);

            if (categoryLists.get(position).getBeginner() != 0 ||
                    categoryLists.get(position).getIntermediate() != 0 ||
                    categoryLists.get(position).getAdvanced() != 0) {

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

        public TextView textView_home_category_adapter;
        public RoundedImageView imageView_home_category_adapter;
        private final RelativeLayout clickCategoryID;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            imageView_home_category_adapter = itemView.findViewById(R.id.imageView_home_category_adapter);
            textView_home_category_adapter = itemView.findViewById(R.id.textView_home_category_adapter);
            clickCategoryID = itemView.findViewById(R.id.clickCategoryID);
        }
    }
}
