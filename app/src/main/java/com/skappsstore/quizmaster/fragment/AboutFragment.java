package com.skappsstore.quizmaster.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skappsstore.quizmaster.BuildConfig;
import com.skappsstore.quizmaster.R;
import com.skappsstore.quizmaster.activity.HelpActivity;

public class AboutFragment extends Fragment {

    private Button clickContactID,clickAboutID,clickPrivacyID,clickTermConditionID,clickRateID,clickShareID;
    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_about, container, false);

        clickContactID = view.findViewById(R.id.clickContactID);
        clickAboutID = view.findViewById(R.id.clickAboutID);
        clickPrivacyID = view.findViewById(R.id.clickPrivacyID);
        clickTermConditionID = view.findViewById(R.id.clickTermConditionID);
        clickRateID = view.findViewById(R.id.clickRateID);
        clickShareID = view.findViewById(R.id.clickShareID);

        BottomNavigationView navBar = getActivity().findViewById(R.id.homeNavBottom);
        navBar.setSelectedItemId(R.id.navigation_about);


        clickContactID.setOnClickListener(v -> {
            gotoHelp("contact");
        });
        clickAboutID.setOnClickListener(v -> {
            gotoHelp("about");
        });
        clickPrivacyID.setOnClickListener(v -> {
            gotoHelp("privacy");
        });
        clickTermConditionID.setOnClickListener(v -> {
            gotoHelp("term");
        });
        clickRateID.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id="+getContext().getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        clickShareID.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String shareMessage= "\nThis apps is amazing. Play quiz with different categories\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share Quiz Master Apps"));
            } catch(Exception e) {
                //e.toString();
            }
        });




        return view;
    }

    private void gotoHelp(String data){

        Bundle bundle = new Bundle();
        Intent intent = new Intent(getContext(), HelpActivity.class);
        bundle.putString("data",data);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}