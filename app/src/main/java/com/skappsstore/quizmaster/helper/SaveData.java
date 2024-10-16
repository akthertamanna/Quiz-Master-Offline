package com.skappsstore.quizmaster.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveData {

    public static boolean saveClickedCategoryTitle(String category, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Helper.key_clicked_category_title, category);
        editor.apply();
        return true;
    }

    public static String getClickedCategoryTitle(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(Helper.key_clicked_category_title, "1");
    }

    public static boolean saveQuizLevel(String level, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Helper.key_Quiz_Level, level);
        editor.apply();
        return true;
    }

    public static String getQuizLevel(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(Helper.key_Quiz_Level, "Beginner");
    }

    public static boolean saveCategoryID(int level, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Helper.key_CategoryID, level);
        editor.apply();
        return true;
    }

    public static int getCategoryID(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(Helper.key_CategoryID, 0);
    }




    public static boolean saveFirstTime(String level, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString(Helper.firstTime, level);
        editor.apply();
        return true;
    }



    public static String getFirstTime(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(Helper.firstTime, "no");
    }





}
