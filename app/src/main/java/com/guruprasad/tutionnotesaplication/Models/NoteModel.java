package com.guruprasad.tutionnotesaplication.Models;

import android.net.Uri;

public class NoteModel {

    private String filename;
    private Uri file;

    public NoteModel(String filename, Uri file) {
        this.filename = filename;
        this.file = file;
    }

    public NoteModel() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Uri getFile() {
        return file;
    }

    public void setFile(Uri file) {
        this.file = file;
    }
}
