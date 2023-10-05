package com.guruprasad.tutionnotesaplication.Activities.ui.AskMe

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.guruprasad.tutionnotesaplication.Activities.ui.CreateNote.WrapContentLinearLayoutManager
import com.guruprasad.tutionnotesaplication.Adapters.QuestionAdapter
import com.guruprasad.tutionnotesaplication.Adapters.UserQuestionAdapter
import com.guruprasad.tutionnotesaplication.Constants
import com.guruprasad.tutionnotesaplication.CustomDialog
import com.guruprasad.tutionnotesaplication.Models.QuestionModel
import com.guruprasad.tutionnotesaplication.R
import com.guruprasad.tutionnotesaplication.databinding.FragmentNotificationsBinding

class AskMeFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: FirebaseAuth
    private lateinit var adapter: QuestionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentNotificationsBinding.inflate(layoutInflater)
        val view:View = binding.root

        val dialog : CustomDialog = CustomDialog(requireContext())
        dialog.show()

        binding.ask.setOnClickListener{
            startActivity(Intent(context,PostQuestionActivity::class.java))
        }

        database = FirebaseDatabase.getInstance()
        userId = FirebaseAuth.getInstance()

        binding.recyclerView.layoutManager = WrapContentLinearLayoutManager(context, RecyclerView.VERTICAL,false)
        val query: Query = database.reference.child("Questions")
        val option: FirebaseRecyclerOptions<QuestionModel> = FirebaseRecyclerOptions.Builder<QuestionModel>().setQuery(query, QuestionModel::class.java).build()
        adapter = object : QuestionAdapter(option , requireContext())
        {
            override fun onDataChanged() {
                super.onDataChanged()
                dialog.dismiss()
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Constants.error(context,"Failed to load data "+error.message)
                dialog.dismiss()
            }
        }

        binding.recyclerView.adapter = adapter

    return view

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}