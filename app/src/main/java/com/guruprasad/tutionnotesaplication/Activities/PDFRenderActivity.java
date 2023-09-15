package com.guruprasad.tutionnotesaplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.guruprasad.tutionnotesaplication.R;
import com.guruprasad.tutionnotesaplication.databinding.ActivityPdfrenderBinding;

public class PDFRenderActivity extends AppCompatActivity {

    ActivityPdfrenderBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfrenderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}