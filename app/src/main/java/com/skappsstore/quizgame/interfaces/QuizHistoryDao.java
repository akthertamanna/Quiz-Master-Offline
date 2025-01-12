package com.skappsstore.quizgame.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.skappsstore.quizgame.model.QuizHistory;

import java.util.List;

@Dao
public interface QuizHistoryDao {

    // Insert a new quiz participation record
    @Insert
    void insert(QuizHistory quizHistory);

    // Get all quiz participation's for the single user (though only one will exist)
    @Query("SELECT * FROM QuizHistory ORDER BY id DESC")
    List<QuizHistory> getAllQuizHistory();

    // Get quiz participation by its ID
    @Query("SELECT * FROM QuizHistory WHERE id = :id")
    QuizHistory getQuizParticipationById(int id);

    // Get the latest quiz participation (assuming the user only participates in one set of quizzes at a time)
    @Query("SELECT * FROM QuizHistory ORDER BY id DESC")
    QuizHistory getLatestQuizParticipation();
}
