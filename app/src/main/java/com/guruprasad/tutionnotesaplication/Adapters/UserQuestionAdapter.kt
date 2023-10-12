package com.guruprasad.tutionnotesaplication.Adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.guruprasad.tutionnotesaplication.Activities.ui.AskMe.SeeAndEditActivity
import com.guruprasad.tutionnotesaplication.Constants
import com.guruprasad.tutionnotesaplication.CustomDialog
import com.guruprasad.tutionnotesaplication.Models.QuestionModel
import com.guruprasad.tutionnotesaplication.R
import java.util.Objects

open class UserQuestionAdapter(options: FirebaseRecyclerOptions<QuestionModel> , context: Context) : FirebaseRecyclerAdapter<QuestionModel, UserQuestionAdapter.onviewholder>(options) {

    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): onviewholder {
     val view:View = LayoutInflater.from(parent.context).inflate(R.layout.userquestionlayout,parent,false);
        return onviewholder(view)
    }

    override fun onBindViewHolder(holder: onviewholder, position: Int, model: QuestionModel) {

        if (model !=null)
        {

            database = FirebaseDatabase.getInstance()
            auth = FirebaseAuth.getInstance()

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
                    showDeleteConfirmationDialog(holder.itemView.context , model)
                }

        }
        else
        {
            Constants.error(holder.itemView.context,"Data is null")
        }

    }

    private fun showDeleteConfirmationDialog(context: Context , model:QuestionModel) {

        val yes = object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                    deletenode(context,model)
            }
        }

        val No = object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog!!.cancel()
            }
        }

        var dialogbuilder: MaterialAlertDialogBuilder = Constants.dialog(context,"Note","Are you sure you want to delete this note")
        dialogbuilder.setPositiveButton("YES",yes)
        dialogbuilder.setNegativeButton("NO",No)

            val dialog:androidx.appcompat.app.AlertDialog = dialogbuilder.create()
            dialog.show()

    }

    private fun deletenode(context: Context,model : QuestionModel) {

        if (model.questionId.isNullOrEmpty())
        {
            Constants.error(context,"Does not find the question Id")
            return
        }

        val dialog:CustomDialog = CustomDialog(context)
        dialog.show()

        database.reference.child("UserQuestion").child(auth.currentUser!!.uid).child(model.questionId).removeValue()
                .addOnSuccessListener {
                    database.reference.child("Questions").child(model.questionId).removeValue()
                            .addOnSuccessListener {
                                dialog.dismiss()
                                Constants.success(context,"Your Query has deleted successfully")
                            }.addOnFailureListener{e->
                                Constants.error(context,"Failed to delete value : "+e.message)
                                dialog.dismiss()
                            }
                }.addOnFailureListener{e->
                    Constants.error(context,"Failed to delete value : "+e.message)
                    dialog.dismiss()
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