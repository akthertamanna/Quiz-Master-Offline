package com.skappsstore.quizgame.fragment;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.skappsstore.quizgame.R;
import com.skappsstore.quizgame.activity.MainActivity;
import com.skappsstore.quizgame.adapter.HomeCategoryAdapter;
import com.skappsstore.quizgame.database.QuizDataHandler;
import com.skappsstore.quizgame.database.SaveData;
import com.skappsstore.quizgame.databinding.FragmentHomeBinding;
import com.skappsstore.quizgame.utilities.Helper;
import com.skappsstore.quizgame.model.*;
import com.skappsstore.quizgame.utilities.ImageResourceHelper;

import java.util.ArrayList;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeCategoryAdapter homeCategoryAdapter;
    private ArrayList<CategoryList> categoryLists = new ArrayList<>();
    private ArrayList<CategoryList> imageListsCategory = new ArrayList<>();
    private static final String KEY_HOME_CATEGORY = "categoryListsHome";


    public HomeFragment() {
        // Required empty public constructor
    }

    // Save when it will change the orientation
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_HOME_CATEGORY, categoryLists);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize View Binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        Helper.lastActiveFragment = "Home";
        // Ensure that categoryLists is never null
        if (categoryLists == null) {
            categoryLists = new ArrayList<>();
        }
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            categoryLists.addAll((ArrayList<CategoryList>) savedInstanceState.getSerializable(KEY_HOME_CATEGORY));
        }else{

            // Initialize categoryLists only if it's not initialized (i.e., when there is no savedInstanceState)
            if (categoryLists == null) {
                categoryLists = new ArrayList<>();
            }

            categoryLists.clear(); // Clear the list before adding new data
            categoryLists.addAll(QuizDataHandler.getAllCategories());
            if (homeCategoryAdapter != null) {
                homeCategoryAdapter.notifyDataSetChanged();
            }
        }

        setupViewAllCategoryListener();
        setupRecyclerView();
        setupSwipeRefresh();
        getCategories();
    }

    private void setupViewAllCategoryListener() {
        binding.txtViewAllCategory.setOnClickListener(v -> {
            CategoriesFragment categoriesFragment = new CategoriesFragment();
            // Call switchFragment from MainActivity
            if (getActivity() instanceof MainActivity activity) {
                activity.switchFragment(categoriesFragment, "CategoriesFragment");

                // Update BottomNavigationView to change active nav
                activity.activityBinding.homeNavBottom.setSelectedItemId(R.id.navigation_category);
            }
        });
    }


    private void setupRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(
                getContext(),
                getResources().getInteger(R.integer.number_of_grid_items_home_category)
        );
        binding.recyclerViewHomeCategoryFrag.setLayoutManager(manager);
        binding.recyclerViewHomeCategoryFrag.setHasFixedSize(true);
        binding.recyclerViewHomeCategoryFrag.setNestedScrollingEnabled(false);

        homeCategoryAdapter = new HomeCategoryAdapter(getActivity(), categoryLists);
        binding.recyclerViewHomeCategoryFrag.setAdapter(homeCategoryAdapter);
    }


    private void setupSwipeRefresh() {
        binding.swipeHomeFrag.setOnRefreshListener(() -> {
            //binding.progressBarHome.setVisibility(View.VISIBLE);
            clearData();
            getCategories();
            binding.swipeHomeFrag.setRefreshing(false);
        });

    }

    private void clearData() {
        // imageListsCategory list need to clear as we want to display the category randomly
        imageListsCategory.clear();
        binding.imageSliderCategory.clearAnimation();
        binding.imageSliderCategory.removeAllSliders();
        binding.recyclerViewHomeCategoryFrag.getRecycledViewPool().clear();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getCategories() {

        imageListsCategory.addAll(QuizDataHandler.getCategoriesSlider());
        setupSlider();

        if (categoryLists.isEmpty()) {
            // Here display no category available display
            Toast.makeText(getContext(), "No categories available", Toast.LENGTH_SHORT).show();
        }

        homeCategoryAdapter.notifyDataSetChanged();
    }

    private void setupSlider() {
        if (imageListsCategory.isEmpty()) {
            binding.imageSliderCategory.setVisibility(GONE);
            binding.noDataHomeFragmentSlider.setVisibility(View.VISIBLE);
            return;
        }

        for (int i = 0; i < Math.min(imageListsCategory.size(), 3); i++) {
            CategoryList slider = imageListsCategory.get(i);
            TextSliderView textSliderView = new TextSliderView(getActivity());

            textSliderView
                    .description(slider.getCategoryTitle().toUpperCase(Locale.ROOT))
                    .image(ImageResourceHelper.getImageResourceByCategory(slider.getCategoryImage()))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(sl -> handleSliderClick(slider));

            binding.imageSliderCategory.addSlider(textSliderView);
        }

        configureSliderLayout();

    }

    private void handleSliderClick(CategoryList slider) {
        SaveData.saveClickedCategoryTitle(slider.getCategoryTitle(), getContext());
        SaveData.saveCategoryID(slider.getCategoryID(), getContext());
        Helper.quizLevel(slider.getBeginner(), slider.getIntermediate(), slider.getAdvanced(), getContext());
    }

    private void configureSliderLayout() {
        try {
            binding.imageSliderCategory.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
            binding.imageSliderCategory.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            binding.imageSliderCategory.setCustomAnimation(new DescriptionAnimation());
            binding.imageSliderCategory.setDuration(6000);
            binding.imageSliderCategory.getPagerIndicator().setDefaultIndicatorColor(
                    requireContext().getResources().getColor(R.color.main),
                    requireContext().getResources().getColor(R.color.white)
            );
        } catch (Resources.NotFoundException ignored) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify the binding to prevent memory leaks
        binding = null;
    }
}