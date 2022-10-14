package com.example.quizbox3;

public class CategoryModel {
   String catid , catname , catimg;

    public CategoryModel() {
    }

    public CategoryModel(String catid, String catname, String catimg) {
        this.catid = catid;
        this.catname = catname;
        this.catimg = catimg;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getCatimg() {
        return catimg;
    }

    public void setCatimg(String catimg) {
        this.catimg = catimg;
    }
}
