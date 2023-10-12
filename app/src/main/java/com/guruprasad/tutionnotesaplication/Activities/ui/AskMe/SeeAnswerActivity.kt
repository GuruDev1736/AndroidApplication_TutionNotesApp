package com.guruprasad.tutionnotesaplication.Activities.ui.AskMe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.guruprasad.tutionnotesaplication.Constants
import com.guruprasad.tutionnotesaplication.CustomDialog
import com.guruprasad.tutionnotesaplication.Models.QuestionModel
import com.guruprasad.tutionnotesaplication.R
import com.guruprasad.tutionnotesaplication.databinding.ActivitySeeAnswerBinding

class SeeAnswerActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySeeAnswerBinding
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dialog:CustomDialog = CustomDialog(this@SeeAnswerActivity)
        dialog.show()

        database = FirebaseDatabase.getInstance()

        binding.actionbar.activityName.text = "See Answer"
        binding.actionbar.back.setOnClickListener {
            finish()
        }
        binding.actionbar.files.visibility = View.GONE
        binding.actionbar.options.visibility = View.GONE

        val intent:Intent = intent
        val questionId:String? = intent.getStringExtra("questionId")
        val answer: String? = intent.getStringExtra("answer")

        val listener = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val model: QuestionModel? = snapshot.getValue(QuestionModel::class.java)
                if (model!=null)
                {
                    dialog.dismiss()
                    binding.question.text = model.question
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Constants.error(this@SeeAnswerActivity,"Failed to ge the question")
                dialog.dismiss()
            }
        }

        database.reference.child("Questions").child(questionId.toString()).addValueEventListener(listener)
        binding.answer.text = answer



    }
}