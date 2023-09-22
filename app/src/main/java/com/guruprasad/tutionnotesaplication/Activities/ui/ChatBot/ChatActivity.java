package com.guruprasad.tutionnotesaplication.Activities.ui.ChatBot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.guruprasad.tutionnotesaplication.R;
import com.guruprasad.tutionnotesaplication.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}