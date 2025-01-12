package com.skappsstore.quizgame.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.skappsstore.quizgame.utilities.Helper;

import java.io.Serializable;

@Entity(tableName = Helper.QUIZ_HISTORY_TABLE_NAME)
public class QuizHistory implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "category_id")
    private int categoryID; // Storing the category name

    @ColumnInfo(name = "quiz_level")
    private byte quizLevel;

    @ColumnInfo(name = "correct_answers")
    private int correctAnswers; // Number of correct answers

    @ColumnInfo(name = "wrong_answers")
    private int wrongAnswers;   // Number of wrong answers

    @ColumnInfo(name = "skipped_questions")
    private int skippedQuestions; // Number of skipped questions

    @ColumnInfo(name = "total_questions")
    private int totalQuestions; // Total number of questions in the quiz

    @ColumnInfo(name = "time_taken")
    private long timeTaken; // Time taken for the quiz in milliseconds

    // Constructor
    public QuizHistory(int categoryID, byte quizLevel, int correctAnswers, int wrongAnswers,
                       int skippedQuestions, int totalQuestions, long timeTaken) {
        this.categoryID = categoryID;
        this.quizLevel = quizLevel;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.skippedQuestions = skippedQuestions;
        this.totalQuestions = totalQuestions;
        this.timeTaken = timeTaken;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public byte getQuizLevel() {
        return quizLevel;
    }

    public void setQuizLevel(byte quizLevel) {
        this.quizLevel = quizLevel;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public int getSkippedQuestions() {
        return skippedQuestions;
    }

    public void setSkippedQuestions(int skippedQuestions) {
        this.skippedQuestions = skippedQuestions;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }
}
