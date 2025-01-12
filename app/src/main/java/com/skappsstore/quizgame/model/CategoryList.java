package com.skappsstore.quizgame.model;

import java.io.Serializable;

public class CategoryList implements Serializable {
    private String categoryTitle;
    private String categoryImage;
    private int categoryID;
    private int totalQuiz;
    private int beginner;
    private int intermediate;
    private int advanced;

    public CategoryList(String categoryTitle, String categoryImage, int categoryID, int totalQuiz, int beginner, int intermediate, int advanced) {
        this.categoryTitle = categoryTitle;
        this.categoryImage = categoryImage;
        this.categoryID = categoryID;
        this.totalQuiz = totalQuiz;
        this.beginner = beginner;
        this.intermediate = intermediate;
        this.advanced = advanced;
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

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public int getBeginner() {
        return beginner;
    }

    public void setBeginner(int beginner) {
        this.beginner = beginner;
    }

    public int getIntermediate() {
        return intermediate;
    }

    public void setIntermediate(int intermediate) {
        this.intermediate = intermediate;
    }

    public int getAdvanced() {
        return advanced;
    }

    public void setAdvanced(int advanced) {
        this.advanced = advanced;
    }

    public int getTotalQuiz() {
        return totalQuiz;
    }

    public void setTotalQuiz(int totalQuiz) {
        this.totalQuiz = totalQuiz;
    }
}

