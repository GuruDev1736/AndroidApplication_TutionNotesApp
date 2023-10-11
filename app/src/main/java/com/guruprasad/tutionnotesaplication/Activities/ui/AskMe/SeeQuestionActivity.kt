package com.guruprasad.tutionnotesaplication.Activities.ui.AskMe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.guruprasad.tutionnotesaplication.Activities.ui.CreateNote.WrapContentLinearLayoutManager
import com.guruprasad.tutionnotesaplication.Adapters.AnswerAdapter
import com.guruprasad.tutionnotesaplication.Adapters.QuestionAdapter
import com.guruprasad.tutionnotesaplication.Constants
import com.guruprasad.tutionnotesaplication.CustomDialog
import com.guruprasad.tutionnotesaplication.Models.AnswerModel
import com.guruprasad.tutionnotesaplication.Models.QuestionModel
import com.guruprasad.tutionnotesaplication.Models.UserModel
import com.guruprasad.tutionnotesaplication.R
import com.guruprasad.tutionnotesaplication.databinding.ActivitySeeQuestionBinding

class SeeQuestionActivity : AppCompatActivity() {


    private lateinit var binding:ActivitySeeQuestionBinding
    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth
    private lateinit var adapter: AnswerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.actionbar.activityName.text = "See Question"
        binding.actionbar.back.setOnClickListener {
            finish()
        }
        binding.actionbar.files.visibility = View.GONE
        binding.actionbar.options.visibility = View.GONE


        val dialog:CustomDialog = CustomDialog(this@SeeQuestionActivity)
        dialog.show()

        val intent:Intent = intent
        val questionid: String? = intent.getStringExtra("questionId")

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        val listener = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val model: QuestionModel? = snapshot.getValue(QuestionModel::class.java)
                if (model!=null)
                {
                    dialog.dismiss()
                    binding.question.text = model.question
                    binding.questionid.text = model.questionId

                    val userListener = object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val usermodel: UserModel? = snapshot.getValue(UserModel::class.java)
                            if (usermodel!=null)
                            {
                                binding.askedby.text = usermodel.name
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Constants.error(this@SeeQuestionActivity,"Failed to get the username : "+error.message)
                        }
                    }
                    database.reference.child("Users").child(auth.currentUser!!.uid).addValueEventListener(userListener)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Constants.error(this@SeeQuestionActivity,"Failed to get the question : "+error.message)
                dialog.dismiss()
            }
        }

        database.reference.child("Questions").child(questionid.toString()).addValueEventListener(listener)

        val answerdialog:CustomDialog = CustomDialog(this@SeeQuestionActivity)
        answerdialog.show()

        binding.recyclerview.layoutManager = WrapContentLinearLayoutManager(this@SeeQuestionActivity,RecyclerView.VERTICAL,false)
        val query:Query = database.reference.child("Answers").child(questionid.toString())
        val option: FirebaseRecyclerOptions<AnswerModel> = FirebaseRecyclerOptions.Builder<AnswerModel>().setQuery(query,AnswerModel::class.java).build()
        adapter = object :AnswerAdapter(option,this@SeeQuestionActivity)
        {
            override fun onDataChanged() {
                super.onDataChanged()
                answerdialog.dismiss()
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Constants.error(this@SeeQuestionActivity,"Failed to load answers : "+error.message)
                answerdialog.dismiss()
            }
        }

        binding.recyclerview.adapter = adapter



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