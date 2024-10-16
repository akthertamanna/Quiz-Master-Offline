package com.skappsstore.quizmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.skappsstore.quizmaster.MainActivity;
import com.skappsstore.quizmaster.R;

public class ResultActivity extends AppCompatActivity {

    private TextView txtResult, txtWinLoss,txtSkipped,txtWrong,extraTxtLoss,txtLossTop;
    private ImageView imgWinLoss;
    private int correct, total, wrong, notAttempt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txtResult = findViewById(R.id.txtResult);
        txtWinLoss = findViewById(R.id.txtWinLoss);
        txtSkipped = findViewById(R.id.txtSkipped);
        txtWrong = findViewById(R.id.txtWrong);
        extraTxtLoss = findViewById(R.id.extraTxtLoss);

        imgWinLoss = findViewById(R.id.imgWinLoss);
        txtLossTop = findViewById(R.id.txtLossTop);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            correct = extras.getInt("correct");
            wrong = extras.getInt("wrong");
            total = extras.getInt("total");

            calculateWinorLose(correct,total);

            //The key argument here must match that used in the other activity

        }



    }

    public void calculateWinorLose(int correct, int total){
        int percent = correct * 100 / total;
        txtResult.setText(correct+"/"+total);
        int skipped =0;
        if (wrong==0){

            skipped = total-correct;
        }else if(correct==0){
            skipped = total-wrong;
        }else{
            skipped = correct+wrong;
            skipped = total- skipped;
        }


        txtSkipped.setText(skipped+"/"+total);
        txtWrong.setText(wrong+"/"+total);

        if (percent>=60){
            txtWinLoss.setText("You Win");
            imgWinLoss.setImageResource(R.drawable.winner);
            txtLossTop.setVisibility(View.GONE);
            extraTxtLoss.setVisibility(View.GONE);
        }else{
            txtWinLoss.setText("You Lose");
//            imgWinLoss.setImageResource(R.drawable.lose);
            imgWinLoss.setVisibility(View.GONE);
            extraTxtLoss.setText("Need 60% to win. You get "+percent+"%");
            extraTxtLoss.setVisibility(View.VISIBLE);
            txtLossTop.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        go();

    }

    public void gotohome(View view) {
        go();
    }

    public void playAgin(View view) {
        Intent intent = new Intent(ResultActivity.this, QuizLevel.class);
        startActivity(intent);
        finish();
    }
    public void go(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        bundle.putString("frm","result");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}

