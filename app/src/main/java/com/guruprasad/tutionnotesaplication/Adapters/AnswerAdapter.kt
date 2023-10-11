package com.guruprasad.tutionnotesaplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.guruprasad.tutionnotesaplication.Constants
import com.guruprasad.tutionnotesaplication.Models.AnswerModel
import com.guruprasad.tutionnotesaplication.Models.UserModel
import com.guruprasad.tutionnotesaplication.R

open class AnswerAdapter(options: FirebaseRecyclerOptions<AnswerModel> , context: Context) : FirebaseRecyclerAdapter<AnswerModel, AnswerAdapter.onviewholder>(options) {
            val  auth: FirebaseAuth = FirebaseAuth.getInstance()
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    class onviewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val answer:MaterialTextView = itemView.findViewById(R.id.answer)
        val see:MaterialButton= itemView.findViewById(R.id.see)
        val report:MaterialButton = itemView.findViewById(R.id.report)
        val username:MaterialTextView = itemView.findViewById(R.id.username)
        val questionId:MaterialTextView = itemView.findViewById(R.id.questionId)
        val time:MaterialTextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): onviewholder {
       val view:View = LayoutInflater.from(parent.context).inflate(R.layout.answerlayout,parent,false)
        return onviewholder(view)
    }

    override fun onBindViewHolder(holder: onviewholder, position: Int, model: AnswerModel) {

        if (model!=null)
        {
            val listner = object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userModel: UserModel? = snapshot.getValue(UserModel::class.java)
                    if (userModel!=null)
                    {
                        holder.username.text = userModel.name
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Constants.error(holder.itemView.context,"Failed to get username : "+error.message)
                }

            }
            database.reference.child("Users").child(model.userId).addValueEventListener(listner)

            holder.answer.text = truncateString(model.answer,10)
            holder.time.text = model.answerTime
            holder.questionId.text = model.questionId

        }
    }

    open fun truncateString(input: String, maxLength: Int): String? {
        return if (input.length <= maxLength) {
            input
        } else {
            input.substring(0, maxLength - 1) + "..."
        }
    }

}