package com.skappsstore.quizgame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.skappsstore.quizgame.R;
import com.skappsstore.quizgame.database.QuizDataHandler;
import com.skappsstore.quizgame.utilities.Helper;
import com.skappsstore.quizgame.utilities.TimeUtils;
import com.skappsstore.quizgame.model.QuizHistory;
import java.util.ArrayList;

public class QuizHistoryAdapter extends RecyclerView.Adapter<QuizHistoryAdapter.QuizHistoryViewHolder> {
    private final Context mContext;
    private final ArrayList<QuizHistory> quizHistoryLists;

    public QuizHistoryAdapter(Context context, ArrayList<QuizHistory> quizHistoryLists) {
        mContext = context;
        this.quizHistoryLists = quizHistoryLists;
    }

    @NonNull
    @Override
    public QuizHistoryAdapter.QuizHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_quiz_history, parent, false);
        return new QuizHistoryViewHolder(v);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull QuizHistoryAdapter.QuizHistoryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        QuizHistory singleItem = quizHistoryLists.get(position);

        if (QuizDataHandler.getCategoryNameByID(singleItem.getCategoryID()) != null){
            holder.txtCategoryName.setText(String.format(QuizDataHandler.getCategoryNameByID(singleItem.getCategoryID())));
        }
        else{
            holder.txtCategoryName.setText(String.format("Category ID: %d", singleItem.getCategoryID()));
        }


        holder.txtQuizLevel.setText(String.format("Quiz Level: %s", Helper.quizLevelConvertToText(singleItem.getQuizLevel())));
        holder.txtCorrectAnswer.setText(String.format("Correct answer: %d", singleItem.getCorrectAnswers()));
        holder.txtWrongAnswer.setText(String.format("Wrong answer: %d", singleItem.getWrongAnswers()));
        holder.txtSkippedQuestion.setText(String.format("Skipped question: %d", singleItem.getSkippedQuestions()));
        holder.txtTotalQuestions.setText(String.format("Total questions: %d", singleItem.getTotalQuestions()));
        holder.txtTimeTaken.setText(TimeUtils.convertMillisecondsToTime(singleItem.getTimeTaken()));

        // Set alternating background color based on position (even or odd)
        if (position % 2 == 0) {
            // Set white background for even positions
            holder.linearLayoutQizHistoryItemID.setBackgroundColor(ContextCompat.getColor(mContext, R.color.PapayaWhip));
            holder.cardviewQuizHistoryItemID.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.PapayaWhip));
        } else {
            // Set grey background for odd positions
            holder.linearLayoutQizHistoryItemID.setBackgroundColor(ContextCompat.getColor(mContext, R.color.LavenderBlush));
            holder.cardviewQuizHistoryItemID.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.LavenderBlush));
        }

    }

    @Override
    public int getItemCount() {
        return quizHistoryLists.size();
    }

    public static class QuizHistoryViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView cardviewQuizHistoryItemID;
        private final LinearLayout linearLayoutQizHistoryItemID;
        private final TextView txtQuizLevel;
        private final TextView txtCategoryName;
        private final TextView txtCorrectAnswer;
        private final TextView txtWrongAnswer;
        private final TextView txtSkippedQuestion;
        private final TextView txtTotalQuestions;
        private final TextView txtTimeTaken;

        public QuizHistoryViewHolder(View itemView) {
            super(itemView);
            linearLayoutQizHistoryItemID = itemView.findViewById(R.id.linear_layout_quiz_history_item_ID);
            cardviewQuizHistoryItemID = itemView.findViewById(R.id.cardview_quiz_history_item_ID);
            txtCategoryName = itemView.findViewById(R.id.txt_category_name);
            txtQuizLevel = itemView.findViewById(R.id.txt_quiz_level);
            txtCorrectAnswer = itemView.findViewById(R.id.txt_correct_answer);
            txtWrongAnswer = itemView.findViewById(R.id.txt_wrong_answer);
            txtSkippedQuestion = itemView.findViewById(R.id.txt_skipped_question);
            txtTotalQuestions = itemView.findViewById(R.id.txt_total_questions);
            txtTimeTaken = itemView.findViewById(R.id.txt_time_taken);
        }
    }
}