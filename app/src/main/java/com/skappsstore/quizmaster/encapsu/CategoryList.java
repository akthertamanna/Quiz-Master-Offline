package com.skappsstore.quizmaster.encapsu;

import java.io.Serializable;

public class CategoryList implements Serializable {
    private String categoryTitle;
    private String categoryImage;
    private int cagetoryID;
    private int totalQuiz;

    private int beginnerT;
    private int intermediateT;
    private int advanceT;

    public CategoryList(String categoryTitle, String categoryImage, int cagetoryID, int totalQuiz, int beginnerT, int intermediateT, int advanceT) {
        this.categoryTitle = categoryTitle;
        this.categoryImage = categoryImage;
        this.cagetoryID = cagetoryID;
        this.totalQuiz = totalQuiz;
        this.beginnerT = beginnerT;
        this.intermediateT = intermediateT;
        this.advanceT = advanceT;
    }

    public CategoryList() {

    }


    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategory(String category) {
        this.categoryTitle = category;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public int getCagetoryID() {
        return cagetoryID;
    }

    public void setCagetoryID(int cagetoryID) {
        this.cagetoryID = cagetoryID;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
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

    public int getTotalQuiz() {
        return totalQuiz;
    }

    public void setTotalQuiz(int totalQuiz) {
        this.totalQuiz = totalQuiz;
    }
}

