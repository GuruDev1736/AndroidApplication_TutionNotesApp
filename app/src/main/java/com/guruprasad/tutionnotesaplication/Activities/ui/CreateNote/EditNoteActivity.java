package com.guruprasad.tutionnotesaplication.Activities.ui.CreateNote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.guruprasad.tutionnotesaplication.R;
import com.guruprasad.tutionnotesaplication.databinding.ActivityEditNoteBinding;

public class EditNoteActivity extends AppCompatActivity {


    ActivityEditNoteBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}