package com.skappsstore.quizgame.repository;

import android.app.Application;
import android.os.Build;

import com.skappsstore.quizgame.database.AppDatabase;
import com.skappsstore.quizgame.database.QuizDataHandler;
import com.skappsstore.quizgame.interfaces.SeenQuestionDao;
import com.skappsstore.quizgame.model.QuizList;
import com.skappsstore.quizgame.model.SeenQuestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QuizRepository {

    private final SeenQuestionDao seenQuestionDao;

    public QuizRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        seenQuestionDao = db.seenQuestionDao();
    }

    // Check if the question has been seen
    public boolean hasQuestionBeenSeen(int categoryId, int questionId) {
        return seenQuestionDao.hasQuestionBeenSeen(categoryId, questionId) > 0;
    }

    // Mark a question as seen
    public void markQuestionAsSeen(int categoryId, int questionId, byte level) {
        // Create an instance of SeenQuestion
        SeenQuestion seenQuestion = new SeenQuestion(categoryId, questionId, level);

        // Insert into the database
        new Thread(() -> {
            seenQuestionDao.markQuestionAsSeen(seenQuestion); // This saves the seen question
        }).start();  // Run the insert in a background thread to avoid blocking the UI
    }

    // Get all seen questions for a category
    public List<SeenQuestion> getSeenQuestions(int categoryId) {
        return seenQuestionDao.getSeenQuestionsList(categoryId);
    }

    // Get filtered quiz questions based on category and level
    public static ArrayList<QuizList> getFilteredQuizzes(int mainCategoryID, byte mainLevel, int requiredQuestions, SeenQuestionDao seenQuestionDao) {
        ArrayList<QuizList> filteredQuizzes = new ArrayList<>();
        ArrayList<QuizList> cachedQuizList = QuizDataHandler.getAllQuizzes();

        if (cachedQuizList != null && !cachedQuizList.isEmpty()) {
            // Filter quizzes by category and level (works for all versions)
            List<QuizList> categoryLevelQuizzes = new ArrayList<>();
            for (QuizList quiz : cachedQuizList) {
                if (quiz.getCategoryID() == mainCategoryID && quiz.getLevel() == mainLevel) {
                    categoryLevelQuizzes.add(quiz);
                }
            }

            // Get already seen question IDs (using a manual approach for older versions)
            Set<Integer> seenQuestionIds = new HashSet<>();
            List<SeenQuestion> seenQuestions = seenQuestionDao.getSeenQuestionsList(mainCategoryID);
            for (SeenQuestion seenQuestion : seenQuestions) {
                seenQuestionIds.add(seenQuestion.questionID);
            }

            // Filter out seen questions and select unseen questions
            List<QuizList> unseenQuizzes = new ArrayList<>();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // For API 24 and above (using streams)
                unseenQuizzes = categoryLevelQuizzes.stream()
                        .filter(quiz -> !seenQuestionIds.contains(quiz.getQuestionID()))
                        .collect(Collectors.toList());
            } else {
                // For API 21 to API 23 (using traditional loop)
                for (QuizList quiz : categoryLevelQuizzes) {
                    if (!seenQuestionIds.contains(quiz.getQuestionID())) {
                        unseenQuizzes.add(quiz);
                    }
                }
            }

            // If there are enough unseen questions, return them
            if (unseenQuizzes.size() >= requiredQuestions) {
                filteredQuizzes.addAll(unseenQuizzes.subList(0, requiredQuestions));
            } else {
                // Add all unseen questions
                filteredQuizzes.addAll(unseenQuizzes);

                // Calculate how many more questions are needed
                int remainingQuestions = requiredQuestions - filteredQuizzes.size();

                // Get seen questions to fill up the remaining slots
                List<QuizList> seenQuizzes = new ArrayList<>();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // For API 24 and above (using streams)
                    seenQuizzes = categoryLevelQuizzes.stream()
                            .filter(quiz -> seenQuestionIds.contains(quiz.getQuestionID()))
                            .collect(Collectors.toList());
                } else {
                    // For API 21 to API 23 (using traditional loop)
                    for (QuizList quiz : categoryLevelQuizzes) {
                        if (seenQuestionIds.contains(quiz.getQuestionID())) {
                            seenQuizzes.add(quiz);
                        }
                    }
                }

                // Shuffle the seen questions and pick the remaining ones randomly
                Collections.shuffle(seenQuizzes);
                filteredQuizzes.addAll(seenQuizzes.subList(0, Math.min(remainingQuestions, seenQuizzes.size())));
            }
        }

        return filteredQuizzes;
    }
}
