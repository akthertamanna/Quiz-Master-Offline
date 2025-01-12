package com.skappsstore.quizgame.model;

import java.io.Serializable;

public class QuizList implements Serializable {

    private int questionID;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private byte answer;
    private String hint;
    private byte level;
    private int categoryID;

    public QuizList(int questionID, String question, String optionA, String optionB,
                        String optionC, String optionD, byte answer, String hint,
                        byte level, int categoryID) {
        this.questionID = questionID;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
        this.hint = hint;
        this.level = level;
        this.categoryID = categoryID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public byte getAnswer() {
        return answer;
    }

    public void setAnswer(byte answer) {
        this.answer = answer;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

}
