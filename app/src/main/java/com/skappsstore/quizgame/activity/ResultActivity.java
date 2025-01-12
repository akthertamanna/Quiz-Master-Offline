package com.skappsstore.quizgame.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.skappsstore.quizgame.R;
import com.skappsstore.quizgame.database.AppDatabase;
import com.skappsstore.quizgame.database.SaveData;
import com.skappsstore.quizgame.databinding.ActivityResultBinding;
import com.skappsstore.quizgame.utilities.Helper;
import com.skappsstore.quizgame.interfaces.QuizHistoryDao;
import com.skappsstore.quizgame.model.QuizHistory;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding activityBinding;
    private int wrong;
    private Context context;
    private QuizHistoryDao quizHistoryDao;
    private boolean isAlreadySaved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());

        // Initialize the context variable
        context = ResultActivity.this;

        // Restore isAlreadySaved from savedInstanceState
        if (savedInstanceState != null) {
            isAlreadySaved = savedInstanceState.getBoolean("isAlreadySaved", false);
        }


        //The key argument here must match that used in the other activity
        Bundle data = getIntent().getExtras();
        if (data != null) {

            // Initialize the database
            AppDatabase appDatabase = AppDatabase.getDatabase(this);

            // Initialize the DAO
            quizHistoryDao = appDatabase.quizParticipationDao();

            int correct = data.getInt("correct");
            wrong = data.getInt("wrong");
            int total = data.getInt("total");
            long timeTaken = data.getLong("timeTaken");
            calculateWinOrLose(correct, total, timeTaken);

        }


        activityBinding.btnHome.setOnClickListener(v -> goToHomeResult());

        activityBinding.btnPlayAgain.setOnClickListener(v -> playAgain());

        // Register back-press callback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Call the method to show the exit dialog
                goToHomeResult();
            }
        };

        // Add the callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @SuppressLint("DefaultLocale")
    public void calculateWinOrLose(int correct, int total, long timeTaken){
        int percent = correct * 100 / total;
        activityBinding.txtResult.setText(String.format("%d/%d", correct, total));
        int skippedQuestions;

        if (wrong==0){
            skippedQuestions = total-correct;
        }else if(correct==0){
            skippedQuestions = total-wrong;
        }else{
            skippedQuestions = correct + wrong;
            skippedQuestions = total - skippedQuestions;
        }

        // Get the last clicked category id
        int categoryID = SaveData.getCategoryID(context);
        insertQuizParticipation(new QuizHistory(categoryID, SaveData.getQuizLevel(context), correct, wrong, skippedQuestions,total, timeTaken));

        activityBinding.txtSkipped.setText(String.format("%d/%d", skippedQuestions, total));
        activityBinding.txtWrong.setText(String.format("%d/%d", wrong, total));

        if (percent>=60){
            activityBinding.txtWinLoss.setVisibility(View.VISIBLE);
            activityBinding.txtWinLoss.setText(R.string.you_win);
            activityBinding.imgWinLoss.setImageResource(R.drawable.winner);
            activityBinding.txtLossTop.setVisibility(View.GONE);
            activityBinding.extraTxtLoss.setVisibility(View.GONE);

        }else{

            if (Helper.isLandscapeMode(context)){
                activityBinding.txtWinLoss.setVisibility(View.GONE);
            }else{
                activityBinding.txtWinLoss.setVisibility(View.VISIBLE);
                activityBinding.txtWinLoss.setText(R.string.you_lose);
            }

            activityBinding.imgWinLoss.setVisibility(View.GONE);
            activityBinding.extraTxtLoss.setText(String.format("Need 60%% to win. You get %d%%", percent));
            activityBinding.extraTxtLoss.setVisibility(View.VISIBLE);
            activityBinding.txtLossTop.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isAlreadySaved", isAlreadySaved);
    }

    public void playAgain() {
        Helper.navigateToActivity(ResultActivity.this, QuizLevelActivity.class, null , true);
    }
    public void goToHomeResult(){
        Bundle bundle = new Bundle();
        bundle.putString("frm","result");
        Helper.navigateToActivity(ResultActivity.this, MainActivity.class, bundle , true);
    }

    private void insertQuizParticipation(QuizHistory quizHistory) {
        if (!isAlreadySaved){
            isAlreadySaved = true;
            // This should be done on a background thread
            new Thread(() -> quizHistoryDao.insert(quizHistory)).start();
        }
    }
}

