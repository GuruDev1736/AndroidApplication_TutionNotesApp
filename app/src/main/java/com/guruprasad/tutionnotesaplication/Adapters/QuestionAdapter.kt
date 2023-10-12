package com.guruprasad.tutionnotesaplication.Adapters

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.guruprasad.tutionnotesaplication.Activities.ui.AskMe.AnswerActivity
import com.guruprasad.tutionnotesaplication.Activities.ui.AskMe.SeeQuestionActivity
import com.guruprasad.tutionnotesaplication.Constants
import com.guruprasad.tutionnotesaplication.CustomDialog
import com.guruprasad.tutionnotesaplication.Models.QuestionModel
import com.guruprasad.tutionnotesaplication.Models.ReportQuestionModel
import com.guruprasad.tutionnotesaplication.Models.UserModel
import com.guruprasad.tutionnotesaplication.R

open class QuestionAdapter(options: FirebaseRecyclerOptions<QuestionModel> , context: Context) : FirebaseRecyclerAdapter<QuestionModel, QuestionAdapter.onviewholder>(options) {

        val database:FirebaseDatabase = FirebaseDatabase.getInstance()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): onviewholder {
       val view:View = LayoutInflater.from(parent.context).inflate(R.layout.questionlayout,parent,false)
        return onviewholder(view)
    }

    override fun onBindViewHolder(holder: onviewholder, position: Int, model: QuestionModel) {

        if (model!=null)
        {
            holder.question.text = model.question
            holder.questionId.text = model.questionId
            holder.time.text = model.generationTime
            holder.updatedtime.text = model.updatedTime
        }

        val listener = object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val usermodel:UserModel = snapshot.getValue(UserModel::class.java)!!
                if (usermodel!=null)
                {
                    holder.username.text = usermodel.name
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Constants.error(holder.itemView.context,"Failed to get username"+error.message)
            }
        }
        database.reference.child("Users").child(model.userId).addValueEventListener(listener)

        holder.answer.setOnClickListener {
            holder.itemView.context.startActivity(Intent(holder.itemView.context,AnswerActivity::class.java)
                    .putExtra("questionId",model.questionId))
        }

        holder.see.setOnClickListener {
            holder.itemView.context.startActivity(Intent(holder.itemView.context,SeeQuestionActivity::class.java)
                .putExtra("questionId",model.questionId))

        }

        holder.report.setOnClickListener {

          report(holder.itemView.context ,model )




        }








    }

    private fun report(context: Context , model: QuestionModel){


        val reason:EditText = EditText(context)
        val dialogBuilder:MaterialAlertDialogBuilder = Constants.dialog(context,"Report","Are you sure you want to report the question")
        val yes = object :OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val reason:String = reason.text.toString()

                if (reason.isNullOrEmpty())
                {
                    Constants.error(context,"Please enter the valid reason to report the question")
                    return
                }

                val dialog:CustomDialog = CustomDialog(context)
                dialog.show()

                val report:ReportQuestionModel = ReportQuestionModel(model.userId,model.questionId,model.question,reason)
                database.reference.child("QuestionReport").child(model.questionId).child(model.userId).setValue(report)
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

        val no = object :OnClickListener{
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
    class onviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val question:MaterialTextView = itemView.findViewById(R.id.question)
        val username:MaterialTextView = itemView.findViewById(R.id.username)
        val questionId:MaterialTextView = itemView.findViewById(R.id.questionId)
        val time:MaterialTextView = itemView.findViewById(R.id.time)
        val updatedtime:MaterialTextView = itemView.findViewById(R.id.updatedtime)
        val answer:MaterialButton = itemView.findViewById(R.id.answer)
        val see:MaterialButton = itemView.findViewById(R.id.see)
        val report:MaterialButton = itemView.findViewById(R.id.report)

    }
}
