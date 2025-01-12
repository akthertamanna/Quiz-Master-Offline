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
import com.skappsstore.quizgame.adapter.QuizHistoryAdapter;
import com.skappsstore.quizgame.database.QuizDataHandler;
import com.skappsstore.quizgame.databinding.FragmentQuizHistoryBinding;
import com.skappsstore.quizgame.utilities.Helper;
import com.skappsstore.quizgame.model.QuizHistory;
import java.util.ArrayList;

public class QuizHistoryFragment extends Fragment {
    private FragmentQuizHistoryBinding binding;
    private QuizHistoryAdapter quizHistoryAdapter;
    private ArrayList<QuizHistory> quizHistoryList = null;
    private static final String KEY_QUIZ_HISTORY = "quiz_history";

    public QuizHistoryFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize View Binding
        binding = FragmentQuizHistoryBinding.inflate(inflater, container, false);
        Helper.lastActiveFragment = "History";
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //quizHistoryList = QuizDataHandler.getAllQuizHistory(getContext());

        if (savedInstanceState != null) {
            quizHistoryList = (ArrayList<QuizHistory>) savedInstanceState.getSerializable(KEY_QUIZ_HISTORY);
        } else {
            // If no saved state, get the list from your data source
            quizHistoryList = QuizDataHandler.getAllQuizHistory(getContext());
        }



        setupRecyclerView();
        setupSwipeRefresh();
        displayCategories();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_QUIZ_HISTORY, quizHistoryList);
    }

    private void setupRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(
                getContext(),
                getResources().getInteger(R.integer.number_of_grid_items_history)
        );
        binding.recyclerQuizHistoryFrag.setLayoutManager(manager);
        binding.recyclerQuizHistoryFrag.setHasFixedSize(true);

        quizHistoryAdapter = new QuizHistoryAdapter(getContext(), quizHistoryList);
        binding.recyclerQuizHistoryFrag.setAdapter(quizHistoryAdapter);
    }

    private void setupSwipeRefresh() {
        binding.swipeCategoryFrag.setOnRefreshListener(() -> {
            binding.recyclerQuizHistoryFrag.getRecycledViewPool().clear();

            displayCategories();

            binding.swipeCategoryFrag.setRefreshing(false);
        });
    }

    @SuppressLint({"DefaultLocale", "NotifyDataSetChanged"})
    private void displayCategories() {
        if (quizHistoryList.isEmpty()) {
            binding.noDataQuizHistoryFragment.setVisibility(View.VISIBLE);
        }else{
            binding.noDataQuizHistoryFragment.setVisibility(View.GONE);
        }

        // Update the total category count and questions
        binding.txtTotalQuizParticipate.setText(String.format("%s %d quiz", getResources().getString(R.string.you_have_participated_in_X_quiz), quizHistoryList.size()));

        quizHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify the binding to prevent memory leaks
        binding = null;
    }
}