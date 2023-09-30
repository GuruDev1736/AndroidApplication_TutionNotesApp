package com.guruprasad.tutionnotesaplication.Activities.ui.AskMe

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.guruprasad.tutionnotesaplication.R
import com.guruprasad.tutionnotesaplication.databinding.FragmentNotificationsBinding

class AskMeFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentNotificationsBinding.inflate(layoutInflater)
        val view:View = binding.root

        binding.ask.setOnClickListener{
            startActivity(Intent(context,PostQuestionActivity::class.java))
        }

        return view

    }

}