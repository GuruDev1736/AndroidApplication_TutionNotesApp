package com.guruprasad.tutionnotesaplication.Activities.ui.AskMe;

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Contacts.Intents.UI
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.guruprasad.tutionnotesaplication.Activities.ui.CreateNote.WrapContentLinearLayoutManager
import com.guruprasad.tutionnotesaplication.Adapters.UserQuestionAdapter
import com.guruprasad.tutionnotesaplication.Constants
import com.guruprasad.tutionnotesaplication.CustomDialog
import com.guruprasad.tutionnotesaplication.Models.QuestionModel
import com.guruprasad.tutionnotesaplication.databinding.ActivityPostQuestionBinding
import es.dmoral.toasty.Toasty
import java.time.LocalTime
import java.util.UUID

class PostQuestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostQuestionBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: FirebaseAuth
    private lateinit var adapter:UserQuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.actionbar.back.setOnClickListener {
          onBackPressed()
        }

        binding.actionbar.activityName.setText("Ask Question")
        binding.actionbar.files.visibility = View.GONE
        binding.actionbar.options.visibility = View.GONE

        val dialog :CustomDialog = CustomDialog(this@PostQuestionActivity)
        dialog.show()

        FirebaseApp.initializeApp(this@PostQuestionActivity)


        database = FirebaseDatabase.getInstance()
        userId = FirebaseAuth.getInstance()


        binding.submit.setOnClickListener {

            if (binding.query.text.toString().isEmpty()) {
                Constants.error(this@PostQuestionActivity, "Please enter the queery")
                return@setOnClickListener
            }
            else
            {
            Upload(binding.query.text.toString())
            }

        }

        binding.recyclerView.layoutManager = WrapContentLinearLayoutManager(this@PostQuestionActivity, VERTICAL,false)
        val query:Query = database.reference.child("UserQuestion").child(userId.currentUser!!.uid)
        val option:FirebaseRecyclerOptions<QuestionModel> = FirebaseRecyclerOptions.Builder<QuestionModel>().setQuery(query,QuestionModel::class.java).build()
        adapter = object : UserQuestionAdapter(option , this@PostQuestionActivity)
        {
            override fun onDataChanged() {
                super.onDataChanged()
                dialog.dismiss()
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Constants.error(this@PostQuestionActivity,"Failed to load data "+error.message)
                dialog.dismiss()
            }
        }

        binding.recyclerView.adapter = adapter
    }


    @SuppressLint("NewApi")
    private fun Upload(question: String) {

        val UID: String = UUID.randomUUID().toString()
        val currentTime: String = LocalTime.now().toString()
        val dialog :CustomDialog = CustomDialog(this@PostQuestionActivity)
        dialog.show()
        //dialog.title("Uploading Question")


                val model: QuestionModel = QuestionModel(userId.currentUser!!.uid, question, UID, currentTime)
                database.reference.child("Questions").child(UID).setValue(model)
                        .addOnSuccessListener {

                            database.reference.child("UserQuestion").child(userId.currentUser!!.uid).child(UID).setValue(model)
                                    .addOnSuccessListener {
                                        Constants.success(this@PostQuestionActivity, "Question has successfully uploaded")
                                        dialog.dismiss()
                                        binding.query.text!!.clear()
                                    }.addOnFailureListener{e->
                                        Constants.error(this@PostQuestionActivity,"Failed to upload question in user database"+e.message)
                                        dialog.dismiss()
                                    }
                        }.addOnFailureListener { e ->
                            Constants.error(this@PostQuestionActivity, "Failed to upload the question to global database : " + e.message)
                            dialog.dismiss()
                        }
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