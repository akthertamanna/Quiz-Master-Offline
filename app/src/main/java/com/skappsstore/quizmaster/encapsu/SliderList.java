package com.skappsstore.quizmaster.encapsu;

import java.io.Serializable;

public class SliderList implements Serializable {
    private String categoryTitle;
    private String categoryImage;
    private int categoryID;
    private int totalQuiz;

    private int beginnerT;
    private int intermediateT;
    private int advanceT;

    public SliderList(String categoryTitle, String categoryImage, int categoryID, int totalQuiz, int beginnerT, int intermediateT, int advanceT) {
        this.categoryTitle = categoryTitle;
        this.categoryImage = categoryImage;
        this.categoryID = categoryID;
        this.totalQuiz = totalQuiz;
        this.beginnerT = beginnerT;
        this.intermediateT = intermediateT;
        this.advanceT = advanceT;
    }

    public SliderList() {
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getTotalQuiz() {
        return totalQuiz;
    }

    public void setTotalQuiz(int totalQuiz) {
        this.totalQuiz = totalQuiz;
    }

    public int getBeginnerT() {
        return beginnerT;
    }

    public void setBeginnerT(int beginnerT) {
        this.beginnerT = beginnerT;
    }

    public int getIntermediateT() {
        return intermediateT;
    }

    public void setIntermediateT(int intermediateT) {
        this.intermediateT = intermediateT;
    }

    public int getAdvanceT() {
        return advanceT;
    }

    public void setAdvanceT(int advanceT) {
        this.advanceT = advanceT;
    }
}
