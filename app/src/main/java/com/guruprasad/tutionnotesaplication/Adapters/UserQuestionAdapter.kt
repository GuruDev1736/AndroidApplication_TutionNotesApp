package com.guruprasad.tutionnotesaplication.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.textview.MaterialTextView
import com.guruprasad.tutionnotesaplication.Activities.ui.AskMe.SeeAndEditActivity
import com.guruprasad.tutionnotesaplication.Constants
import com.guruprasad.tutionnotesaplication.Models.QuestionModel
import com.guruprasad.tutionnotesaplication.R

open class UserQuestionAdapter(options: FirebaseRecyclerOptions<QuestionModel> , context: Context) : FirebaseRecyclerAdapter<QuestionModel, UserQuestionAdapter.onviewholder>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): onviewholder {
     val view:View = LayoutInflater.from(parent.context).inflate(R.layout.userquestionlayout,parent,false);
        return onviewholder(view)
    }

    override fun onBindViewHolder(holder: onviewholder, position: Int, model: QuestionModel) {

        if (model !=null)
        {
            holder.question.setText(truncateString(model.question,15))
            holder.see.setOnClickListener {
                holder.itemView.context.
                startActivity(Intent(holder.itemView.context,SeeAndEditActivity::class.java)
                        .putExtra("operation","1")
                        .putExtra("questionId",model.questionId))
            }
            holder.edit.setOnClickListener {
                holder.itemView.context.
                startActivity(Intent(holder.itemView.context,SeeAndEditActivity::class.java)
                        .putExtra("operation","2")
                        .putExtra("questionId",model.questionId))
            }

            holder.delete.setOnClickListener {

            }

        }
        else
        {
            Constants.error(holder.itemView.context,"Data is null")
        }

    }

    open fun truncateString(input: String, maxLength: Int): String? {
        return if (input.length <= maxLength) {
            input
        } else {
            input.substring(0, maxLength - 1) + "..."
        }
    }

    public class onviewholder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val question:MaterialTextView = itemView.findViewById(R.id.question)
        val see:ImageButton = itemView.findViewById(R.id.see)
        val edit:ImageButton = itemView.findViewById(R.id.edit)
        val delete:ImageButton = itemView.findViewById(R.id.delete)

    }


}