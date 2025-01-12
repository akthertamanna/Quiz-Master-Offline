package com.skappsstore.quizgame.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.skappsstore.quizgame.R;
import com.skappsstore.quizgame.activity.QuizLevelActivity;
import com.skappsstore.quizgame.database.SaveData;
import com.skappsstore.quizgame.interfaces.DialogCallback;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Helper {
    public static final String DATABASE_NAME = "QuizDatabase";
    public static final String QUIZ_HISTORY_TABLE_NAME = "QuizHistory";
    public static final String SEEN_QUESTION_TABLE_NAME = "SeenQuestion";
    public static String key_clicked_category_title = "clickedCategoryTitle";
    public static String key_CategoryID = "categoryID";
    public static String key_Quiz_Level = "QuizLevel";

    public static boolean isUpdateChecked = false;
    public static String firstTime = "firstTime";
    public static String quizDataUrl = "quizdata.json";

    public static final byte EASY_MODE = 1;
    public static final byte INTERMEDIATE_MODE = 2;
    public static final byte ADVANCE_MODE = 3;

    public static String lastActiveFragment = "Home";


    public static void showScreenSizeToast(Context context) {
        Configuration config = context.getResources().getConfiguration();
        int screenLayout = config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        String screenSize = switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL -> "Small";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL -> "Normal";
            case Configuration.SCREENLAYOUT_SIZE_LARGE -> "Large";
            case Configuration.SCREENLAYOUT_SIZE_XLARGE -> "Extra Large";
            default -> "Undefined";
        };

        Toast.makeText(context, "Screen Size: " + screenSize, Toast.LENGTH_SHORT).show();
    }

    public static String quizLevelConvertToText(byte level){
        if(level == 1){
            return "Beginner";
        }else if(level == 2){
            return "Intermediate";
        }else{
            return "Advance";
        }
    }

    public enum DialogPurpose {
        EXIT,
        PAUSE,
        HOME
    }


    public static boolean isLandscapeMode(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        // The device is in landscape mode
        return orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static String getDialogPurposeText(DialogPurpose purpose){
        return purpose.name();
    }

    public static int getNumberOfQuestionShouldDisplay(byte level){

        if(level == EASY_MODE){
            return 10;
        }else if(level == INTERMEDIATE_MODE){
            return 10;
        }else{
            return 15;
        }

    }

    public static String getJsonData(String fileName, Context context){
        String json = null;
        try {
            InputStream in = context.getAssets().open(fileName);
            int size = in.available();
            byte[] bbuffer = new byte[size];
            in.read(bbuffer);
            in.close();
            json=new String(bbuffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
        return json;
    }

    public static boolean isNetworkConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void quizLevel(int beginnerLvl, int intermediateLvl, int advanceLvl, Context context){

        Dialog dialog;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.quizlevel);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnBeginner= dialog.findViewById(R.id.btnBeginner);
        Button btnIntermediate = dialog.findViewById(R.id.btnIntermediate);
        Button btnAdvance = dialog.findViewById(R.id.btnAdvance);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);

        // need to change the logic of the quiz level here
        // It should be show question size from the setting according to the user selection

        if (beginnerLvl <=9){
            btnBeginner.setVisibility(View.GONE);
        }else{
            btnBeginner.setVisibility(View.VISIBLE);
        }

        if (intermediateLvl<=9){
            btnIntermediate.setVisibility(View.GONE);
        }else{
            btnIntermediate.setVisibility(View.VISIBLE);
        }

        if (advanceLvl <=9){
            btnAdvance.setVisibility(View.GONE);
        }else{
            btnAdvance.setVisibility(View.VISIBLE);
        }

        btnClose.setOnClickListener(view -> dialog.dismiss());

        btnBeginner.setOnClickListener(view -> {
            SaveData.saveQuizLevel(Helper.EASY_MODE,context);
            goToQuiz(context,dialog);

        });

        btnIntermediate.setOnClickListener(view -> {
            SaveData.saveQuizLevel(Helper.INTERMEDIATE_MODE,context);
            goToQuiz(context,dialog);
        });

        btnAdvance.setOnClickListener(view -> {
            SaveData.saveQuizLevel(Helper.ADVANCE_MODE,context);
            goToQuiz(context,dialog);
        });

        Dialog noDialog;
        noDialog = new Dialog(context);
        noDialog.setContentView(R.layout.noquiz);
        Objects.requireNonNull(noDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView btnCloseNo = noDialog.findViewById(R.id.btnCloseno);

        btnCloseNo.setOnClickListener(view -> noDialog.dismiss());

        if (advanceLvl<=9 && beginnerLvl<=9 && intermediateLvl<=9){

            noDialog.show();

        }else{

            dialog.show();

        }

    }

    public static void goToQuiz(Context mContext , Dialog dialog){

        Intent quizLevel = new Intent(mContext, QuizLevelActivity.class);
        dialog.dismiss();
        mContext.startActivity(quizLevel);
        ((Activity) mContext).finish();
    }

    public static SweetAlertDialog displayDialog(Context context, int alertType, String title, String contentText, String confirmText, String cancelText, DialogCallback callback, String purpose){

        // Create and display the exit dialog
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, alertType);
        // Prevent the dialog from being dismissed when clicking outside
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        // Prevent to cancel the dialog by press back button
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setTitleText(title);
        sweetAlertDialog.setContentText(contentText);

        cancelText = cancelText == null ? "No" : cancelText;

        sweetAlertDialog.setCancelText(cancelText);
        sweetAlertDialog.setCancelClickListener(sweetAlertDialogVar -> {
            sweetAlertDialogVar.dismiss();  // Dismiss the dialog
            if (callback != null) {
                // If the callback is not null, invoke onCancel() method
                callback.onCancel(purpose);  // Custom behavior for cancel action
            }
        });
        sweetAlertDialog.setConfirmText(confirmText);
        sweetAlertDialog.setConfirmClickListener(sweetAlertDialogVar -> {
            sweetAlertDialogVar.dismiss();  // Dismiss the dialog
            if (callback != null) {
                // If the callback is not null, invoke onConfirm() method
                callback.onConfirm(purpose);  // Custom behavior for confirm action
            }
        });

        // Return the SweetAlertDialog instance
        return sweetAlertDialog;
    }

    public static void navigateToActivity(Context context, Class<?> targetActivity, Bundle data, boolean shouldFinishCurrentActivity) {
        Intent intent = new Intent(context, targetActivity);

        // If data is provided, add it as extras to the intent
        if (data != null) {
            intent.putExtras(data);
        }

        context.startActivity(intent); // Start the target activity

        // Check if the context is an instance of Activity before calling finish
        if (shouldFinishCurrentActivity && context instanceof Activity) {
            ((Activity) context).finish(); // Finish the current activity if flag is true
        }

    }

}
