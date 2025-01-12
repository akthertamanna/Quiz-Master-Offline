package com.skappsstore.quizgame.database;

import android.content.Context;
import com.skappsstore.quizgame.utilities.Helper;
import com.skappsstore.quizgame.interfaces.QuizHistoryDao;
import com.skappsstore.quizgame.model.CategoryList;
import com.skappsstore.quizgame.model.QuizHistory;
import com.skappsstore.quizgame.model.QuizList;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class QuizDataHandler {
    // Static cache to store quiz data once fetched
    private static ArrayList<QuizList> cachedQuizList = null;

    // Static cache to store category data once fetched
    private static ArrayList<CategoryList> cachedCategoryList = null;

    // Static HashMap to store category names by ID globally
    private static HashMap<Integer, String> categoryNameMap = new HashMap<>();

    // Fetch Categories (this will only happen once)
    public static void fetchCategories(String categoryUrl, Context context) {
        // Avoid re-fetching if data is already cached
        if (cachedCategoryList == null) {
            cachedCategoryList = new ArrayList<>();
            try {
                // Get the JSON data from the helper method
                String jsonData = Helper.getJsonData(categoryUrl, context);
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject resultObject = jsonObject.getJSONObject("result");


                // Check if the "categories" key exists and handle both object and array cases
                if (resultObject.has("categories")) {
                    Object categories = resultObject.get("categories");


                    if (categories instanceof JSONObject) {
                        // If it's a JSONObject (like "1": {...}, "2": {...})
                        JSONObject categoriesObject = (JSONObject) categories;
                        Iterator<String> keys = categoriesObject.keys();
                        while (keys.hasNext()) {
                            String key = keys.next(); // Key is like "1", "2", etc.
                            JSONObject jsonCategory = categoriesObject.getJSONObject(key);


                            // Dynamically parse each field using opt* methods for safety
                            String postTitle = jsonCategory.optString("categoryTitle", "Unknown Category");
                            String imageUrl = jsonCategory.optString("categoryImage", "No Image");
                            int categoryID = jsonCategory.optInt("categoryID", -1);
                            int totalQuestionPerCategory = jsonCategory.optInt("totalQuiz", 0);
                            int beginner = jsonCategory.optInt("beginner", 0);
                            int intermediate = jsonCategory.optInt("intermediate", 0);
                            int advanced = jsonCategory.optInt("advanced", 0);


                            // Add CategoryList object to the cache
                            cachedCategoryList.add(new CategoryList(
                                    postTitle,
                                    imageUrl,
                                    categoryID,
                                    totalQuestionPerCategory,
                                    beginner,
                                    intermediate,
                                    advanced
                            ));


                            // Add the category ID and name to the global HashMap
                            categoryNameMap.put(categoryID, postTitle);
                        }


                    } else if (categories instanceof JSONArray) {
                        // If it's a JSONArray (like [{...}, {...}])
                        JSONArray categoryArray = (JSONArray) categories;


                        // Loop through the array and parse each category
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject jsonCategory = categoryArray.getJSONObject(i);


                            // Dynamically parse each field using opt* methods for safety
                            String postTitle = jsonCategory.optString("categoryTitle", "Unknown Category");
                            String imageUrl = jsonCategory.optString("categoryImage", "No Image");
                            int categoryID = jsonCategory.optInt("categoryID", -1);
                            int totalQuestionPerCategory = jsonCategory.optInt("totalQuiz", 0);
                            int beginner = jsonCategory.optInt("beginner", 0);
                            int intermediate = jsonCategory.optInt("intermediate", 0);
                            int advanced = jsonCategory.optInt("advanced", 0);


                            // Add CategoryList object to the cache
                            cachedCategoryList.add(new CategoryList(
                                    postTitle,
                                    imageUrl,
                                    categoryID,
                                    totalQuestionPerCategory,
                                    beginner,
                                    intermediate,
                                    advanced
                            ));

                            // Add the category ID and name to the global HashMap
                            categoryNameMap.put(categoryID, postTitle);
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    // Fetch quizzes from the file (this will only happen once)
    public static void fetchAndCacheQuizzes(String quizUrl, Context context) {
        // Avoid re-fetching if data is already cached
        if (cachedQuizList != null) {
            return; // Data is already cached
        }

        cachedQuizList = new ArrayList<>();
        try {
            // Get the JSON data from the helper method
            String jsonData = Helper.getJsonData(quizUrl, context);
            JSONObject jsonObjectGetQuestions = new JSONObject(jsonData);
            JSONObject resultObject = jsonObjectGetQuestions.getJSONObject("result");

            // Check if the "quizzes" key exists and handle both object and array cases
            if (resultObject.has("quizzes")) {
                Object quizzes = resultObject.get("quizzes");

                if (quizzes instanceof JSONObject) {
                    // If it's a JSONObject (e.g., "393": {...}, "394": {...})
                    JSONObject quizzesObject = (JSONObject) quizzes;
                    Iterator<String> keys = quizzesObject.keys();
                    while (keys.hasNext()) {
                        String key = keys.next(); // Key is like "393"
                        JSONObject jsonObject = quizzesObject.getJSONObject(key);

                        // Dynamically parse the fields using opt* methods for safety
                        int questionID = jsonObject.optInt("questionID");
                        int categoryID = jsonObject.optInt("categoryID");
                        byte level = (byte) jsonObject.optInt("level");

                        String question = jsonObject.optString("question");
                        String optionA = jsonObject.optString("optionA");
                        String optionB = jsonObject.optString("optionB");
                        String optionC = jsonObject.optString("optionC");
                        String optionD = jsonObject.optString("optionD");
                        String hintTxt = jsonObject.optString("hint");

                        byte answer = (byte) jsonObject.optInt("answer");

                        // Create a new QuizList object and add it to the cache
                        cachedQuizList.add(new QuizList(questionID,question,optionA,optionB,
                                optionC,
                                optionD,
                                answer,
                                hintTxt,
                                level,
                                categoryID
                        ));
                    }

                } else if (quizzes instanceof JSONArray) {
                    // If it's a JSONArray (e.g., [{...}, {...}])
                    JSONArray quizzesArray = (JSONArray) quizzes;


                    // Loop through the array and parse each quiz
                    for (int i = 0; i < quizzesArray.length(); i++) {
                        JSONObject jsonObject = quizzesArray.getJSONObject(i);

                        // Dynamically parse the fields using opt* methods for safety
                        int questionID = jsonObject.optInt("questionID");
                        int categoryID = jsonObject.optInt("categoryID");
                        byte level = (byte) jsonObject.optInt("level");

                        String question = jsonObject.optString("question");
                        String optionA = jsonObject.optString("optionA");
                        String optionB = jsonObject.optString("optionB");
                        String optionC = jsonObject.optString("optionC");
                        String optionD = jsonObject.optString("optionD");
                        String hintTxt = jsonObject.optString("hint");

                        byte answer = (byte) jsonObject.optInt("answer");

                        // Create a new QuizList object and add it to the cache
                        cachedQuizList.add(new QuizList(
                                questionID,
                                question,
                                optionA,
                                optionB,
                                optionC,
                                optionD,
                                answer,
                                hintTxt,
                                level,
                                categoryID
                        ));
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public static ArrayList<QuizList> getAllQuizzes() {
        // Return cached quiz list, ensure it's not null before returning
        if (!cachedQuizList.isEmpty()) {
            return cachedQuizList;
        }
        return new ArrayList<>(); // Return an empty list if cachedQuizList is null
    }

    public static ArrayList<CategoryList> getAllCategories() {
        return cachedCategoryList;
    }

    // Get a category name by ID from the global static map
    public static String getCategoryNameByID(int categoryID) {
        return categoryNameMap.get(categoryID);
    }

    public static ArrayList<CategoryList> getCategoriesSlider() {
        ArrayList<CategoryList> sliderList = new ArrayList<>();

        // Ensure cachedCategoryList is not null and contains data
        if (cachedCategoryList != null && !cachedCategoryList.isEmpty()) {
            // Loop through the first 3 items in the cachedCategoryList

            // Shuffle the cachedCategoryList to randomize the order
            Collections.shuffle(cachedCategoryList);

            for (int i = 0; i < Math.min(3, cachedCategoryList.size()); i++) {
                CategoryList category = cachedCategoryList.get(i); // Get each category item

                // Create a SliderList object from the CategoryList object
                CategoryList slider = new CategoryList(
                        category.getCategoryTitle(),
                        category.getCategoryImage(),
                        category.getCategoryID(),
                        category.getTotalQuiz(),
                        category.getBeginner(),
                        category.getIntermediate(),
                        category.getAdvanced()
                );

                // Add the SliderList item to the sliderList
                sliderList.add(slider);
            }
        }

        return sliderList;  // Return the list containing the first 3 items mapped to SliderList
    }


    public static ArrayList<QuizHistory> getAllQuizHistory(Context context) {
        List<QuizHistory> quizHistoryList;
        AppDatabase appDatabase = AppDatabase.getDatabase(context);

        // Initialize the DAO
        QuizHistoryDao quizHistoryDao = appDatabase.quizParticipationDao();

        // Fetch data from the DAO and convert it to an ArrayList
        quizHistoryList = quizHistoryDao.getAllQuizHistory();
        // Return as an ArrayList
        return new ArrayList<>(quizHistoryList);
    }
}