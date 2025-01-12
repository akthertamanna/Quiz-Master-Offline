package com.skappsstore.quizgame.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.skappsstore.quizgame.utilities.Helper;

// Composite primary key ("category_id", "question_id")
@Entity(tableName = Helper.SEEN_QUESTION_TABLE_NAME, primaryKeys = {"category_id", "question_id"})
public class SeenQuestion {

    @ColumnInfo(name = "category_id")
    public int categoryID;

    @ColumnInfo(name = "question_id")
    public int questionID;

    @ColumnInfo(name = "level")
    public byte level;

    public SeenQuestion(int categoryID, int questionID, byte level) {
        this.categoryID = categoryID;
        this.questionID = questionID;
        this.level = level;
    }
}
