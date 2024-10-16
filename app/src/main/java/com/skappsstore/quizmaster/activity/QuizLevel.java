package com.skappsstore.quizmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skappsstore.quizmaster.R;
import com.skappsstore.quizmaster.helper.*;
import com.skappsstore.quizmaster.encapsu.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class QuizLevel extends AppCompatActivity {

    private TextView txtQuizLevel, txtQuizQuestion,txtQuizLeft,txtQuizTime,txtHindAvailable,hintTxt;
    private ImageView imgQuiz, btnHint;
    private TextView btnOpt1,btnOpt2,btnOpt3,btnOpt4;
    private Button btnNext;
    private LinearLayout btn1Lin,btn2Lin,btn3Lin,btn4Lin;
    private RelativeLayout hintLayer;

    private ArrayList<QuizList> quizLists;
    private int qp = 0;
    private int choosen, answer, btnClicked=0;

    private ProgressBar progreeeID;
    private int currentProgress = 0, currentTimeProgress=0;
    private int correctAnswer=0, wrongAnswer=0;
    private int totalQuiz =0, currentQuiz=0;

    private CountDownTimer countTimerForOne;
    private ProgressBar circlProgreeeID;

    private static final long START_TIME_IN_MILLIS = 120000;

    private long mTimerLeftInMilis = START_TIME_IN_MILLIS;


    private int maxTimeLevel = 15;
    private String levelOfQuiz;
    private int adsCount =0;
    private int hintAvailable=0;
    private boolean countOn =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_level);


        checkLevelForSetTime();

        imgQuiz = findViewById(R.id.imgQuiz);
        imgQuiz.setVisibility(View.GONE);

        txtQuizLevel = findViewById(R.id.txtQuizLevel);


        progreeeID = findViewById(R.id.progreeeID);
        circlProgreeeID = findViewById(R.id.circlProgreeeID);

        hintLayer = findViewById(R.id.hintLayer);
        btnHint = findViewById(R.id.btnHint);

        btnOpt1 = findViewById(R.id.btnOpt1);
        btnOpt2 = findViewById(R.id.btnOpt2);
        btnOpt3 = findViewById(R.id.btnOpt3);
        btnOpt4 = findViewById(R.id.btnOpt4);

        btn1Lin = findViewById(R.id.btn1Lin);
        btn2Lin = findViewById(R.id.btn2Lin);
        btn3Lin = findViewById(R.id.btn3Lin);
        btn4Lin = findViewById(R.id.btn4Lin);

        btnNext = findViewById(R.id.btnNext);


        txtQuizQuestion = findViewById(R.id.txtQuizQuestion);
        txtQuizLeft = findViewById(R.id.txtQuizLeft);
        txtQuizTime = findViewById(R.id.txtQuizTime);
        txtHindAvailable = findViewById(R.id.txtHindAvailable);
        hintTxt = findViewById(R.id.hintTxt);

        txtQuizLeft.setText(currentQuiz+"/"+totalQuiz);

        quizLists = new ArrayList<>();
        TextView txtQuizCategory = findViewById(R.id.txtQuizCategory);
        txtQuizLevel.setText(SaveData.getQuizLevel(this));
        txtQuizCategory.setText(SaveData.getClickedCategoryTitle(QuizLevel.this));




        if (levelOfQuiz.equals(Helper.easy)){
            hintAvailable = 3;
        }else if(levelOfQuiz.equals(Helper.intermediate)){
            hintAvailable = 5;
        }else if(levelOfQuiz.equals(Helper.advance)){
            hintAvailable = 5;
        }

        txtHindAvailable.setText(String.valueOf(hintAvailable));

        btnNext.setOnClickListener(v -> {
            checkCanGo();

        });

        btn1Lin.setOnClickListener(v -> {
            resetTimeForCounterButton();
            choosen = 1;
            btnClicked = 1;
            answer_check(choosen);

        });

        btn2Lin.setOnClickListener(v -> {
            resetTimeForCounterButton();
            choosen = 2;
            btnClicked = 2;
            answer_check(choosen);

        });

        btn3Lin.setOnClickListener(v -> {
            resetTimeForCounterButton();
            choosen = 3;
            btnClicked = 3;
            answer_check(choosen);

        });

        btn4Lin.setOnClickListener(v -> {
            resetTimeForCounterButton();
            choosen = 4;
            btnClicked = 4;
            answer_check(choosen);

        });
        btnHint.setOnClickListener(v -> {


            String st = quizLists.get(qp).getHint();


            if (hintAvailable<=0){

                hintTxt.setText("All hint are already used");
                hintTxt.setVisibility(View.VISIBLE);
//                hintLayer.setBackgroundDrawable(ContextCompat.getDrawable(quiz_level.this,R.drawable.hintbgdis));

            }else {
                if (st.equals("0")){
                    hintTxt.setText("No hint available for this question");
                    hintTxt.setVisibility(View.VISIBLE);
//                    hintLayer.setBackgroundDrawable(ContextCompat.getDrawable(quiz_level.this,R.drawable.hintbgdis));

                }else{
                    hintAvailable = hintAvailable-1;
                    txtHindAvailable.setText(String.valueOf(hintAvailable));
                    hintTxt.setText(quizLists.get(qp).getHint());
                    hintTxt.setVisibility(View.VISIBLE);
                }
//                Toast.makeText(this, quizLists.get(qp).getHint(), Toast.LENGTH_SHORT).show();
            }

        });


        check_Inter();

    }



    public void check_Inter(){
        if(Helper.connectioncheck(getApplicationContext())){
            getURLs();
        }
        else {
            Helper.show_no_internet(getApplicationContext());
        }
    }
    private void checkLevelForSetTime(){
        levelOfQuiz = SaveData.getQuizLevel(QuizLevel.this);
        if (levelOfQuiz.equals(Helper.easy)){
            mTimerLeftInMilis = 30000;
            maxTimeLevel = 30;

        }else if(levelOfQuiz.equals(Helper.intermediate)){
            mTimerLeftInMilis = 20000;
            maxTimeLevel = 20;

        }else if(levelOfQuiz.equals(Helper.advance)){
            mTimerLeftInMilis = 20000;
            maxTimeLevel = 20;
        }
    }
    private void resetTimeForCounterButton(){


        if (countOn=true){

            countTimerForOne.cancel();
        }

        hintTxt.setVisibility(View.GONE);
        checkLevelForSetTime();

    }


    private void updateQuizNum(){
        currentQuiz = currentQuiz+1;
        currentProgress = currentProgress+1;
        txtQuizLeft.setText(currentQuiz+"/"+totalQuiz);
        progreeeID.setProgress(currentProgress);






    }
    private void answer_check(int choosen){


        btn1Lin.setEnabled(false);
        btn2Lin.setEnabled(false);
        btn3Lin.setEnabled(false);
        btn4Lin.setEnabled(false);




        if (choosen==answer){
            correctAnswer = correctAnswer +1;

//            Toast.makeText(this, "You choose the right answer", Toast.LENGTH_SHORT).show();

            if (btnClicked==1){
                btn1Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
            }else if (btnClicked==2){
                btn2Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
            }else if (btnClicked==3){
                btn3Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
            }else{
                btn4Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
            }

        }else{
            wrongAnswer++;
//            Toast.makeText(this, "You choose the wrong answer", Toast.LENGTH_SHORT).show();
            if (btnClicked==1){
                btn1Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.wronganswer));

                if (answer==2){
                    btn2Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
                }else if (answer==3){
                    btn3Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
                }else if (answer==4){
                    btn4Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
                }

            }else if (btnClicked==2){
                btn2Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.wronganswer));

                if (answer==1){
                    btn1Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
                }else if (answer==3){
                    btn3Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
                }else if (answer==4){
                    btn4Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
                }

            }else if (btnClicked==3){
                btn3Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.wronganswer));

                if (answer==1){
                    btn1Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
                }else if (answer==2){
                    btn2Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
                }else if (answer==4){
                    btn4Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
                }

            }else{
                btn4Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.wronganswer));

                if (answer==1){
                    btn1Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
                }else if (answer==2){
                    btn2Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
                }else if (answer==3){
                    btn3Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.rightanswer));
                }

            }
        }
        updateQuizNum();

        timeFor();

    }



    private void getURLs() {
        final String u = Helper.kiuu+ Helper.questions;
        class GetURLs_hi extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(QuizLevel.this, "Loading...", "Please Wait or if take longer, exit from the apps and open again", true, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject ja = new JSONObject(s);
                    JSONArray jsonArray = ja.getJSONArray("result");
                    for(int i =0; i <jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String question = jsonObject.getString("question");
                        String optiona = jsonObject.getString("optiona");
                        String optionb = jsonObject.getString("optionb");
                        String optionc = jsonObject.getString("optionc");
                        String optiond = jsonObject.getString("optiond");

                        String hint = jsonObject.getString("hint");

                        int answer = jsonObject.getInt("answer");

                        String image = jsonObject.getString("imageurl");

                        int hasImage = jsonObject.getInt("hasimage");
                        quizLists.add(new QuizList(question,optiona,optionb,optionc,optiond,answer,hasImage,image,hint));

                    }

                    nextQuestion();
                    currentProgress = 0;
                    totalQuiz = quizLists.size();
                    progreeeID.setProgress(currentProgress);
                    progreeeID.setMax(totalQuiz);
                    setCountTimerForOne();

                    circlProgreeeID.setMax(maxTimeLevel);
                    txtQuizLeft.setText(currentQuiz+"/"+totalQuiz);
                    hintImg();



                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
            @Override
            protected String doInBackground(String... strings) {


                try {

                    URL url = new URL(u);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("scatid", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(SaveData.getCategoryID(QuizLevel.this)), "UTF-8")+ "&" +
                            URLEncoder.encode("level", "UTF-8") + "=" + URLEncoder.encode(SaveData.getQuizLevel(QuizLevel.this), "UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS));
                    String Response = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        Response += line;
                    }
                    bufferedReader.close();

                    IS.close();
                    //httpURLConnection.connect();
                    httpURLConnection.disconnect();
                    return Response;
                }
                catch (Exception e) {
                    return null;
                }
            }
        }
        GetURLs_hi gu = new GetURLs_hi();
        gu.execute(u);
    }

    public void checkCanGo(){

        if (qp == (quizLists.size() - 1)){

            gotoNext();
            countTimerForOne.cancel();

        }else{
            qp++;
            btn1Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.quizoption));
            btn2Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.quizoption));
            btn3Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.quizoption));
            btn4Lin.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.quizoption));


            btn1Lin.setEnabled(true);
            btn2Lin.setEnabled(true);
            btn3Lin.setEnabled(true);
            btn4Lin.setEnabled(true);

            nextQuestion();
            resetProgressTime();
            setCountTimerForOne();
            hintImg();

        }
    }

    public void hintImg(){

        String st = quizLists.get(qp).getHint();

        if (st.equals("0")){
            hintLayer.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.hintbgdis));

        }else {

            hintLayer.setBackgroundDrawable(ContextCompat.getDrawable(QuizLevel.this,R.drawable.hintbg));

        }
    }
    public void nextQuestion(){

        txtQuizQuestion.setText(quizLists.get(qp).getQuestion());
        btnOpt1.setText(quizLists.get(qp).getOptiona());
        btnOpt2.setText(quizLists.get(qp).getOptionb());
        btnOpt3.setText(quizLists.get(qp).getOptionc());
        btnOpt4.setText(quizLists.get(qp).getOptiond());

        if (quizLists.get(qp).getHasimage()==1){
            imgQuiz.setVisibility(View.VISIBLE);
//            Picasso.with(quiz_level.this).load(quizLists.get(qp).getQimage()).fit().centerInside().into(imgQuiz);
            Glide.with(QuizLevel.this).load(quizLists.get(qp).getQimage()).centerCrop().placeholder(R.drawable.defaultimage).into(imgQuiz);
        }else{
            imgQuiz.setVisibility(View.GONE);
        }

        answer = quizLists.get(qp).getAnswer();

    }

    public void timeFor() {

        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                if (adsCount >=5){
                    adsCalling();
                }else{
                    checkCanGo();
                }
//                checkCanGo();

            }
        }.start();

    }

    private void adsCalling() {

        //ads fail to show
        adsCount=0;
        checkCanGo();

    }

    public void setCountTimerForOne(){
        countTimerForOne = new CountDownTimer(mTimerLeftInMilis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                mTimerLeftInMilis = millisUntilFinished;
                currentTimeProgress = currentTimeProgress+1;
                txtQuizTime.setText("" + mTimerLeftInMilis / 1000);
                circlProgreeeID.setProgress(currentTimeProgress);

            }

            @Override
            public void onFinish() {
                countOn =false;

//                if (btnClicked == 0) {

                updateQuizNum();
//                }


                resetProgressTime();

                checkCanGo();

            }
        }.start();
        countOn = true;

    }

    public void resetProgressTime(){
        currentTimeProgress = 0;
        circlProgreeeID.setProgress(currentTimeProgress);
        circlProgreeeID.setMax(maxTimeLevel);
        checkLevelForSetTime();

    }
    @Override
    public void onBackPressed() {


        showExitFromQuiz();

    }

    public void showExitFromQuiz() {

        SweetAlertDialog sweetAlertDialog2 = new SweetAlertDialog(QuizLevel.this ,SweetAlertDialog.ERROR_TYPE );
        sweetAlertDialog2 = sweetAlertDialog2.setTitleText("Exit from Quiz!!!");
        sweetAlertDialog2 = sweetAlertDialog2.setContentText("Are you sure you want to exit from quiz?");
        sweetAlertDialog2.setCancelable(false);
        sweetAlertDialog2.setCancelText("No");
        sweetAlertDialog2.setCancelClickListener(sweetAlertDialog -> sweetAlertDialog.dismiss());
        sweetAlertDialog2.setConfirmText("Yes");
        sweetAlertDialog2.setConfirmClickListener(sweetAlertDialog -> {

            if (countOn==true){
                countTimerForOne.cancel();
            }

            sweetAlertDialog.dismiss();
            finish();

        });
        sweetAlertDialog2.show();

    }
    @Override
    protected void onPause() {
        if (countOn=true){

            countTimerForOne.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onStart() {
        if (currentTimeProgress >0){
            resetProgressTime();
            setCountTimerForOne();
        }
        super.onStart();

    }

    public void gotoNext(){
        Intent intent = new Intent(QuizLevel.this, ResultActivity.class);
        intent.putExtra("correct", correctAnswer);
        intent.putExtra("wrong", wrongAnswer);
        intent.putExtra("total", totalQuiz);
        startActivity(intent);
        finish();
    }

}