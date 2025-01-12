package com.skappsstore.quizgame.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.skappsstore.quizgame.utilities.Helper;

public class SaveData {

    // Generic method to save data
    public static <T> boolean saveData(String key, T value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Byte) {
            editor.putInt(key, (Byte) value);
        }else {
            throw new IllegalArgumentException("Unsupported data type");
        }

        editor.apply();
        return true;
    }

    // Generic method to retrieve data
    @SuppressWarnings("unchecked")
    public static <T> T getData(String key, T defaultValue, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);

        if (defaultValue instanceof String) {
            return (T) sharedPreferences.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return (T) (Integer) sharedPreferences.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return (T) (Boolean) sharedPreferences.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            return (T) (Float) sharedPreferences.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Long) {
            return (T) (Long) sharedPreferences.getLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Byte) {
            // Retrieve byte as an int and cast back to byte
            return (T) (Byte) (byte) sharedPreferences.getInt(key, (Byte) defaultValue);
        }else {
            throw new IllegalArgumentException("Unsupported data type");
        }
    }

    // Specific methods for saving and retrieving your data
    public static void saveClickedCategoryTitle(String category, Context context) {
        saveData(Helper.key_clicked_category_title, category, context);
    }

    public static String getClickedCategoryTitle(Context context) {
        return getData(Helper.key_clicked_category_title, "No Category Title", context);
    }

    public static void saveQuizLevel(byte level, Context context) {
        saveData(Helper.key_Quiz_Level, level, context);
    }

    public static byte getQuizLevel(Context context) {
        return getData(Helper.key_Quiz_Level, Helper.EASY_MODE, context);
    }

    public static void saveCategoryID(int level, Context context) {
        saveData(Helper.key_CategoryID, level, context);
    }

    public static int getCategoryID(Context context) {
        return getData(Helper.key_CategoryID, 0, context);
    }

}

