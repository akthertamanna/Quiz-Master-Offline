package com.skappsstore.quizgame.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.skappsstore.quizgame.utilities.Helper;
import com.skappsstore.quizgame.model.SeenQuestion;

import java.util.List;

@Dao
public interface SeenQuestionDao {

    // Single item
    // Check if the question has been seen for a given category
    @Query("SELECT COUNT(*) FROM "+ Helper.SEEN_QUESTION_TABLE_NAME +" WHERE category_id = :categoryId AND question_id = :questionId")
    int hasQuestionBeenSeen(int categoryId, int questionId);

    // Mark a question as seen
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void markQuestionAsSeen(SeenQuestion seenQuestion);

    // Get all seen questions for a single category
    @Query("SELECT * FROM "+ Helper.SEEN_QUESTION_TABLE_NAME +" WHERE category_id = :categoryId")
    List<SeenQuestion> getSeenQuestionsList(int categoryId);
}
