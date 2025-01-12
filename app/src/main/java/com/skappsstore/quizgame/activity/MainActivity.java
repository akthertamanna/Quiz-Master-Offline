package com.skappsstore.quizgame.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.skappsstore.quizgame.R;
import com.skappsstore.quizgame.databinding.ActivityHomeBinding;
import com.skappsstore.quizgame.fragment.AboutFragment;
import com.skappsstore.quizgame.fragment.CategoriesFragment;
import com.skappsstore.quizgame.fragment.HomeFragment;
import com.skappsstore.quizgame.fragment.QuizHistoryFragment;
import com.skappsstore.quizgame.interfaces.DialogCallback;
import com.skappsstore.quizgame.utilities.Helper;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    public ActivityHomeBinding activityBinding;
    private Fragment activeFragment = null;
    private final HomeFragment homeFragment = new HomeFragment();
    private final CategoriesFragment categoriesFragment = new CategoriesFragment();
    private final QuizHistoryFragment quizHistoryFragment = new QuizHistoryFragment();
    private final AboutFragment aboutFragment = new AboutFragment();
    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;

    private static final String HomeFragmentTag = "HomeFragment";
    private static final String CategoriesFragmentTag = "CategoriesFragment";
    private static final String QuizHistoryFragmentTag = "QuizHistoryFragment";
    private static final String AboutFragmentTag = "AboutFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());


        if (savedInstanceState == null) {
            activeFragment = new HomeFragment();
            switchFragment(new HomeFragment(), HomeFragmentTag);

        }else{

            // Get last clicked fragment
            String lastActiveFragment = Helper.lastActiveFragment;

            // Display fragment according to the lastActiveFragment variable
            switch (lastActiveFragment) {
                case "Home":
                    activeFragment = new HomeFragment();
                    switchFragment(new HomeFragment(), HomeFragmentTag);
                    break;
                case "Category":
                    activeFragment = new CategoriesFragment();
                    switchFragment(new CategoriesFragment(), CategoriesFragmentTag);
                    break;
                case "History":
                    activeFragment = new QuizHistoryFragment();
                    switchFragment(new QuizHistoryFragment(), QuizHistoryFragmentTag);
                    break;
                default:
                    activeFragment = new AboutFragment();
                    switchFragment(new AboutFragment(), AboutFragmentTag);
                    break;
            }
        }

        handleIntentData();


        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    // handle callback
                    if (result.getResultCode() != RESULT_OK) {
                        // Update is canceled or fails,
                        // Request to start the update again.
                        Toast.makeText(getApplicationContext(), "Update failed! Try again", Toast.LENGTH_SHORT).show();
                        checkInternetAndAppUpdate();

                    }else{
                        Toast.makeText(getApplicationContext(), "Update successfully done", Toast.LENGTH_SHORT).show();
                        // Update successfully done
                        Helper.isUpdateChecked = true;
                    }

                });

        // Handle bottom navigation
        activityBinding.homeNavBottom.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home){
                switchFragment(homeFragment, HomeFragmentTag);
                return true;
            }else if (item.getItemId() == R.id.navigation_category){
                switchFragment(categoriesFragment, CategoriesFragmentTag);
                return true;
            }else if (item.getItemId() == R.id.navigation_quiz_result){
                switchFragment(quizHistoryFragment, QuizHistoryFragmentTag);
                return true;
            }else if (item.getItemId() == R.id.navigation_about){
                switchFragment(aboutFragment, AboutFragmentTag);
                return true;
            }
            return false;
        });

        registerBackPressedCallback();

        checkInternetAndAppUpdate();

    }

    private void checkInternetAndAppUpdate(){
        // Internet is not connected
        if (!Helper.isNetworkConnected(this)) {
            // Internet is not connected, don't check the app update is available or not
            Helper.isUpdateChecked = true;
        }else{
            // Internet is connected, check the update now
            // If the update already checked, don't check again
            if (!Helper.isUpdateChecked){
                // Check the app update is available or not as it's not checked already
                checkAppUpdate();
            }
        }
    }

    private void checkAppUpdate() {

        // Initialize the AppUpdateManager
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update
                appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activityResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());
            } else {
                // Update is not available, load data for MaiaActivity
                Helper.isUpdateChecked = true;
            }
        });
    }

    private void showProgressBar(boolean show) {
        if (show) {
            activityBinding.progressBarOverlay.setVisibility(View.VISIBLE);
        } else {
            activityBinding.progressBarOverlay.setVisibility(View.GONE);
        }
    }

    private void handleIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String dataKey = bundle.getString("frm", "none");
            if ("result".equals(dataKey) || "quiz".equals(dataKey)) {
                switchFragment(categoriesFragment, "CategoriesFragment");
                activityBinding.homeNavBottom.setSelectedItemId(R.id.navigation_category);
            } else if ("help".equals(dataKey)) {
                switchFragment(new AboutFragment() , "AboutFragment");
                activityBinding.homeNavBottom.setSelectedItemId(R.id.navigation_about);
            }
            // Clear the extras from the Intent
            // It will not replacing fragment to any mentioned above fragment if key is found
            getIntent().replaceExtras((Bundle) null); // Clears all extras
        }
    }

    public void switchFragment(Fragment fragment, String tag) {
        if (activeFragment != fragment) {
            // Show the progress bar while switching fragments
            showProgressBar(fragment instanceof HomeFragment || fragment instanceof CategoriesFragment || fragment instanceof QuizHistoryFragment);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.homeFrag, fragment, tag)
                    .commit();

            if (fragment instanceof HomeFragment || fragment instanceof CategoriesFragment || fragment instanceof QuizHistoryFragment){
                new Handler().postDelayed(() -> showProgressBar(false), 600);
            }
            // After a short delay, hide the progress bar

            activeFragment = fragment;
       }
    }

    private void registerBackPressedCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SweetAlertDialog dialog = Helper.displayDialog(
                        MainActivity.this,
                        SweetAlertDialog.WARNING_TYPE,
                        "Exit?",
                        "Are you sure you want to exit?",
                        "Exit",
                        "No",
                        new DialogCallback() {
                            @Override
                            public void onConfirm(String purpose) {
                                finishAffinity(); // Exit the app
                            }

                            @Override
                            public void onCancel(String purpose) {
                                // Do nothing on cancel
                            }
                        },
                        Helper.getDialogPurposeText(Helper.DialogPurpose.PAUSE)
                );
                dialog.show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showProgressBar(false);
    }
}
