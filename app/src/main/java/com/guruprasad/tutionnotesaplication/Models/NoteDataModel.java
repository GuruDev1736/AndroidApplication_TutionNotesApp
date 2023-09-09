package com.guruprasad.tutionnotesaplication.Models;

import java.util.List;

public class NoteDataModel {

    private String title , note , UniqueID , userid , link1 , link2 , link3 , link4 ;

    public NoteDataModel(String title, String note, String uniqueID, String userid, String link1, String link2, String link3, String link4) {
        this.title = title;
        this.note = note;
        UniqueID = uniqueID;
        this.userid = userid;
        this.link1 = link1;
        this.link2 = link2;
        this.link3 = link3;
        this.link4 = link4;
    }

    public NoteDataModel(String title, String note, String uniqueID, String userid) {
        this.title = title;
        this.note = note;
        UniqueID = uniqueID;
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUniqueID() {
        return UniqueID;
    }

    public void setUniqueID(String uniqueID) {
        UniqueID = uniqueID;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLink1() {
        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public String getLink2() {
        return link2;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }

    public String getLink3() {
        return link3;
    }

    public void setLink3(String link3) {
        this.link3 = link3;
    }

    public String getLink4() {
        return link4;
    }

    public void setLink4(String link4) {
        this.link4 = link4;
    }
}
