package com.guruprasad.tutionnotesaplication.Activities.ui.AskMe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.guruprasad.tutionnotesaplication.Constants
import com.guruprasad.tutionnotesaplication.CustomDialog
import com.guruprasad.tutionnotesaplication.Models.QuestionModel
import com.guruprasad.tutionnotesaplication.R
import com.guruprasad.tutionnotesaplication.databinding.ActivitySeeAndEditBinding
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Objects

private lateinit var binding:ActivitySeeAndEditBinding
private lateinit var database:FirebaseDatabase
private lateinit var auth:FirebaseAuth
private lateinit var reference:DatabaseReference


class SeeAndEditActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeAndEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()



        binding.actionbar.back.setOnClickListener {
            finish()
        }
        binding.actionbar.files.visibility = View.GONE
        binding.actionbar.options.visibility = View.GONE

        val intent : Intent = getIntent()
        val operation : String? = intent.getStringExtra("operation")
        val questionId : String? = intent.getStringExtra("questionId")

         reference = database.reference.child("UserQuestion").child(auth.currentUser!!.uid)
                .child(questionId.toString())

        if (operation.equals("1"))
        {
            if (questionId != null) {
                seeQuestion(reference)
            }
        }
        if (operation.equals("2"))
        {
            if (questionId != null) {
                editQuestion(reference , questionId)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun editQuestion(reference: DatabaseReference , questionId:String) {
        val dialog :CustomDialog = CustomDialog(this@SeeAndEditActivity)
        dialog.show()

       binding.question.visibility = View.VISIBLE
        binding.emailLayout.visibility = View.VISIBLE
        binding.update.visibility = View.VISIBLE

        binding.actionbar.activityName.setText("Edit Question")
        val currentTime: LocalTime? = LocalTime.now()
        val formater = DateTimeFormatter.ofPattern("hh:mm a")
        val formatted = currentTime!!.format(formater)


        val listener = object :ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val model: QuestionModel? = snapshot.getValue(QuestionModel::class.java)
                if (model!=null)
                {
                    dialog.dismiss()
                    binding.query.setText(model.question)
                    binding.question.setText("Question Id : "+model.questionId)

                    binding.update.setOnClickListener {

                        if (binding.query.text.toString().isEmpty()) {
                            Constants.error(this@SeeAndEditActivity,"Question is Empty")
                            return@setOnClickListener
                        }

                        if (binding.query.text.toString().equals(model.question)){
                            Constants.warning(this@SeeAndEditActivity,"There are no changes")
                            return@setOnClickListener
                        }

                        val dialog:CustomDialog = CustomDialog(this@SeeAndEditActivity)
                        dialog.show()

                        val data:HashMap<String,Any> = HashMap()
                        data.put("question", binding.query.text.toString())
                        data.put("updatedTime",formatted)

                        reference.updateChildren(data).addOnSuccessListener {
                            database.reference.child("Questions").child(questionId).updateChildren(data)
                                    .addOnSuccessListener {
                                        Constants.success(this@SeeAndEditActivity,"Your Question Has Edited")
                                        startActivity(Intent(this@SeeAndEditActivity,PostQuestionActivity::class.java))
                                        finish()
                                        dialog.dismiss()
                                    }.addOnFailureListener{e->
                                        Constants.error(this@SeeAndEditActivity,"Failed to edit question : "+e.message)
                                        dialog.dismiss()
                                    }
                        }.addOnFailureListener{e->
                            Constants.error(this@SeeAndEditActivity,"Failed to edit question : "+e.message)
                            dialog.dismiss()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Constants.error(this@SeeAndEditActivity,"Failed to load data : "+error.message)
                dialog.dismiss()
            }
        }
        reference.addValueEventListener(listener)
    }


    private fun seeQuestion(reference: DatabaseReference) {

        val dialog :CustomDialog = CustomDialog(this@SeeAndEditActivity)
        dialog.show()

        binding.actionbar.activityName.setText("See Question")

        val listener = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val model: QuestionModel? = snapshot.getValue(QuestionModel::class.java)
                if (model!=null)
                {
                    dialog.dismiss()
                    binding.question.setText(model.question)
                    binding.emailLayout.visibility = View.GONE
                    binding.update.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Constants.error(this@SeeAndEditActivity,"Failed to load data : "+error.message)
                dialog.dismiss()
            }
        }
        reference.addValueEventListener(listener)
    }



}


