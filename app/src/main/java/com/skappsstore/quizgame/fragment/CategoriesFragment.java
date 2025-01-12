package com.skappsstore.quizgame.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.skappsstore.quizgame.R;
import com.skappsstore.quizgame.adapter.CategoryAdapter;
import com.skappsstore.quizgame.database.QuizDataHandler;
import com.skappsstore.quizgame.databinding.FragmentCategoriesBinding;
import com.skappsstore.quizgame.utilities.Helper;
import com.skappsstore.quizgame.model.CategoryList;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    private FragmentCategoriesBinding binding;
    private CategoryAdapter categoryAdapter;
    private ArrayList<CategoryList> categoryList = new ArrayList<>();

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("categoryLists", categoryList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize View Binding
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        Helper.lastActiveFragment = "Category";
        // Ensure that categoryLists is never null
        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            categoryList.addAll((ArrayList<CategoryList>) savedInstanceState.getSerializable("categoryLists"));
        }else{

            // Initialize categoryLists only if it's not initialized (i.e., when there is no savedInstanceState)
            if (categoryList == null) {
                categoryList = new ArrayList<>();
            }

            categoryList.clear(); // Clear the list before adding new data
            categoryList.addAll(QuizDataHandler.getAllCategories());
            if (categoryAdapter != null) {
                categoryAdapter.notifyDataSetChanged();
            }
        }

        setupRecyclerView();
        setupSwipeRefresh();
        displayCategories();

    }

    private void setupRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(
                getContext(),
                getResources().getInteger(R.integer.number_of_grid_items_category)
        );
        binding.recyclerCategoryFrag.setLayoutManager(manager);
        binding.recyclerCategoryFrag.setHasFixedSize(true);

        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        binding.recyclerCategoryFrag.setAdapter(categoryAdapter);
    }

    private void setupSwipeRefresh() {
        binding.swipeCategoryFrag.setOnRefreshListener(() -> {
            binding.recyclerCategoryFrag.getRecycledViewPool().clear();

            // Since data comes from MainActivity, just refresh UI
            displayCategories();

            binding.swipeCategoryFrag.setRefreshing(false);
        });
    }

    @SuppressLint({"DefaultLocale", "NotifyDataSetChanged"})
    private void displayCategories() {

        if (categoryList.isEmpty()) {
            binding.noDataCategoryFragment.setVisibility(View.VISIBLE);
        }else{
            binding.noDataCategoryFragment.setVisibility(View.GONE);
        }

        // Update the total category count and questions (example logic)
        binding.categoryTotal.setText(String.format(
                "Up to %d Categories",
                categoryList.size()
        ));

        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify the binding to prevent memory leaks
        binding = null;
    }
}
