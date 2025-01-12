package com.skappsstore.quizgame.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.skappsstore.quizgame.repository.QuizRepository;
import com.skappsstore.quizgame.model.SeenQuestion;

import java.util.List;

public class QuizViewModel extends AndroidViewModel {

    private final QuizRepository repository;

    public QuizViewModel(Application application) {
        super(application);
        repository = new QuizRepository(application);
    }

    // Check if the question has been seen
    public boolean hasQuestionBeenSeen(int categoryId, int questionId) {
        return repository.hasQuestionBeenSeen(categoryId, questionId);
    }

    // Mark a question as seen
    public void markQuestionAsSeen(int categoryId, int questionId, byte level) {
        repository.markQuestionAsSeen(categoryId, questionId, level);
    }

    // Get all seen questions for a category
    public List<SeenQuestion> getSeenQuestions(int categoryId) {
        return repository.getSeenQuestions(categoryId);
    }

}
