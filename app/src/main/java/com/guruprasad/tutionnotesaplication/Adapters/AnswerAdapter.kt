package com.guruprasad.tutionnotesaplication.Adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.guruprasad.tutionnotesaplication.Activities.ui.AskMe.SeeAnswerActivity
import com.guruprasad.tutionnotesaplication.Constants
import com.guruprasad.tutionnotesaplication.CustomDialog
import com.guruprasad.tutionnotesaplication.Models.AnswerModel
import com.guruprasad.tutionnotesaplication.Models.QuestionModel
import com.guruprasad.tutionnotesaplication.Models.ReportAnswerModel
import com.guruprasad.tutionnotesaplication.Models.ReportQuestionModel
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

            holder.see.setOnClickListener {
                holder.itemView.context.startActivity(Intent(holder.itemView.context,SeeAnswerActivity::class.java)
                    .putExtra("questionId",model.questionId)
                    .putExtra("answer",model.answer)
                )
            }

            holder.report.setOnClickListener {
                report(holder.itemView.context,model)
            }
        }
    }

    private fun report(context: Context , model: AnswerModel){


        val reason: EditText = EditText(context)
        val dialogBuilder: MaterialAlertDialogBuilder = Constants.dialog(context,"Report","Are you sure you want to report the question")
        val yes = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val reason:String = reason.text.toString()

                if (reason.isNullOrEmpty())
                {
                    Constants.error(context,"Please enter the valid reason to report the Answer")
                    return
                }

                val dialog: CustomDialog = CustomDialog(context)
                dialog.show()

                val report: ReportAnswerModel = ReportAnswerModel(model.userId,model.questionId,model.answer,reason)
                database.reference.child("AnswerReport").child(model.questionId).child(model.userId).setValue(report)
                    .addOnCompleteListener{task->
                        if (task.isSuccessful)
                        {
                            Constants.success(context,"Your report has submitted , We will take action as soon as possible")
                            dialog.dismiss()
                        }
                        else
                        {
                            Constants.error(context,"Failed to report the question : "+ task.exception!!.message)
                            dialog.dismiss()
                        }
                    }
            }
        }

        val no = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog!!.cancel()
            }
        }
        dialogBuilder.setPositiveButton("YES",yes)
            .setNegativeButton("NO",no)
            .setView(reason)
        val dialog:androidx.appcompat.app.AlertDialog = dialogBuilder.create()
        dialog.show()

    }

    open fun truncateString(input: String, maxLength: Int): String? {
        return if (input.length <= maxLength) {
            input
        } else {
            input.substring(0, maxLength - 1) + "..."
        }
    }

}