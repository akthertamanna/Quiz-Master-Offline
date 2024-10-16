package com.skappsstore.quizmaster.encapsu;

import java.io.Serializable;

public class QuizList implements Serializable {
    private String question;
    private String optiona;
    private String optionb;
    private String optionc;
    private String optiond;
    private int answer;
    private int hasimage;
    private String qimage;
    private String hint;

    public QuizList(String question, String optiona, String optionb, String optionc, String optiond, int answer, int hasimage, String qimage, String hint) {
        this.question = question;
        this.optiona = optiona;
        this.optionb = optionb;
        this.optionc = optionc;
        this.optiond = optiond;
        this.answer = answer;
        this.hasimage = hasimage;
        this.qimage = qimage;
        this.hint = hint;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptiona() {
        return optiona;
    }

    public void setOptiona(String optiona) {
        this.optiona = optiona;
    }

    public String getOptionb() {
        return optionb;
    }

    public void setOptionb(String optionb) {
        this.optionb = optionb;
    }

    public String getOptionc() {
        return optionc;
    }

    public void setOptionc(String optionc) {
        this.optionc = optionc;
    }

    public String getOptiond() {
        return optiond;
    }

    public void setOptiond(String optiond) {
        this.optiond = optiond;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getHasimage() {
        return hasimage;
    }

    public void setHasimage(int hasimage) {
        this.hasimage = hasimage;
    }

    public String getQimage() {
        return qimage;
    }

    public void setQimage(String qimage) {
        this.qimage = qimage;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
