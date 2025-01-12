package com.skappsstore.quizgame.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.skappsstore.quizgame.utilities.Helper;
import com.skappsstore.quizgame.interfaces.QuizHistoryDao;
import com.skappsstore.quizgame.interfaces.SeenQuestionDao;
import com.skappsstore.quizgame.model.QuizHistory;
import com.skappsstore.quizgame.model.SeenQuestion;

@Database(entities = {QuizHistory.class, SeenQuestion.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract QuizHistoryDao quizParticipationDao();

    public abstract SeenQuestionDao seenQuestionDao();

    public static AppDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (AppDatabase.class){
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, Helper.DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }

        }
        return INSTANCE;
    }
}