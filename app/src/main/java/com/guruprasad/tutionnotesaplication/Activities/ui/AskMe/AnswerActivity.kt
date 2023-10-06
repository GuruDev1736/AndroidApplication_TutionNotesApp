package com.guruprasad.tutionnotesaplication.Activities.ui.AskMe

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.guruprasad.tutionnotesaplication.Constants
import com.guruprasad.tutionnotesaplication.CustomDialog
import com.guruprasad.tutionnotesaplication.Models.AnswerModel
import com.guruprasad.tutionnotesaplication.Models.QuestionModel
import com.guruprasad.tutionnotesaplication.Models.UserModel
import com.guruprasad.tutionnotesaplication.R
import com.guruprasad.tutionnotesaplication.databinding.ActivityAnswerBinding
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class AnswerActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAnswerBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth:FirebaseAuth
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.actionbar.activityName.text = "Answer the question"
        binding.actionbar.back.setOnClickListener {
            finish()
        }

        binding.actionbar.files.visibility = View.GONE
        binding.actionbar.options.visibility = View.GONE

        val dialog:CustomDialog = CustomDialog(this@AnswerActivity)
        dialog.show()

        val answerdialog:CustomDialog = CustomDialog(this@AnswerActivity)
        dialog.show()

        database = FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()

        val intent:Intent = intent
        val questionid = intent.getStringExtra("questionId")

        val answerListener = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               val model : AnswerModel? = snapshot.getValue(AnswerModel::class.java)
                if (model!=null)
                {
                    answerdialog.dismiss()
                    binding.answer.setText(model.answer)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Constants.error(this@AnswerActivity,"Failed to get answer"+error.message)
                answerdialog.dismiss()
            }

        }

        database.reference.child("Answers").child(questionid.toString()).child(auth.currentUser!!.uid)
                .addValueEventListener(answerListener)

        val listener = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val model: QuestionModel? = snapshot.getValue(QuestionModel::class.java)
                if (model!=null)
                {
                    dialog.dismiss()
                    binding.question.text = model.question
                    binding.questionid.text = model.questionId

                    val listener = object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val usermodel:UserModel? = snapshot.getValue(UserModel::class.java)

                            if (usermodel!=null)
                            {
                                binding.askedby.text = usermodel.name
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        Constants.error(this@AnswerActivity,"Failed to get user detail "+error.message)
                            dialog.dismiss()
                        }
                    }
                    database.reference.child("Users").child(model.userId).addValueEventListener(listener)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Constants.error(this@AnswerActivity,"Failed to get details"+error.message)
                dialog.dismiss()
            }

        }

        database.reference.child("Questions").child(questionid.toString()).addValueEventListener(listener)

        binding.submit.setOnClickListener {

            val dialog:CustomDialog = CustomDialog(this@AnswerActivity)
            dialog.show()

            val uiid: String = UUID.randomUUID().toString()

            val currentTime: LocalTime? = LocalTime.now()
            val formater = DateTimeFormatter.ofPattern("hh:mm a")
            val formatted = currentTime!!.format(formater)
            val answer:String = binding.answer.text.toString()

            if (answer.isNullOrEmpty())
            {
                Constants.error(this@AnswerActivity,"Answer is Required")
                return@setOnClickListener
            }

            val map:HashMap<String,Any> = HashMap()
            map.put("answer",answer.toString())
            map.put("answerTime",formatted)
            map.put("questionId",questionid.toString())
            map.put("userId",auth.currentUser!!.uid)


//            val model:AnswerModel = AnswerModel(answer,formatted,"",auth.currentUser!!.uid,questionid.toString())

            database.reference.child("Answers").child(questionid.toString()).child(auth.currentUser!!.uid).updateChildren(map)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Constants.success(this@AnswerActivity, "Answer has submitted successfully")
                            dialog.dismiss()
                        } else {
                            Constants.error(this@AnswerActivity, "Failed to submit answer" + task.exception!!.message)
                            dialog.dismiss()
                        }
                    }
        }

    }
}