package com.skappsstore.quizgame.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.multidex.BuildConfig;

import com.skappsstore.quizgame.R;
import com.skappsstore.quizgame.activity.HelpActivity;
import com.skappsstore.quizgame.databinding.FragmentAboutBinding;
import com.skappsstore.quizgame.utilities.Helper;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize View Binding
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        Helper.lastActiveFragment = "About";
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.clickContactID.setOnClickListener(v -> gotoHelp("contact"));
        binding.clickAboutID.setOnClickListener(v -> gotoHelp("about"));
        binding.clickPrivacyID.setOnClickListener(v -> gotoHelp("privacy"));
        binding.clickTermConditionID.setOnClickListener(v -> gotoHelp("term"));
        binding.clickRateID.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id="+ requireContext().getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        binding.clickShareID.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String shareMessage= "\nThis apps is amazing. Play quiz with different categories and learn interesting facts.\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share Quiz Master Apps"));
            } catch(Exception ignored) {

            }
        });

    }

    private void gotoHelp(String data){
        Bundle bundle = new Bundle();
        bundle.putString("data",data);
        Helper.navigateToActivity(getContext(), HelpActivity.class, bundle , false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify the binding to prevent memory leaks
        binding = null;

    }

}