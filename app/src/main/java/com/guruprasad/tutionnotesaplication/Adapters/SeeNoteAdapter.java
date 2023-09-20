package com.guruprasad.tutionnotesaplication.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseError;
import com.guruprasad.tutionnotesaplication.Constants;
import com.guruprasad.tutionnotesaplication.Models.NoteDataModel;
import com.guruprasad.tutionnotesaplication.R;

public class SeeNoteAdapter extends FirebaseRecyclerAdapter<NoteDataModel,SeeNoteAdapter.onviewholder> {


    public SeeNoteAdapter(@NonNull FirebaseRecyclerOptions<NoteDataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull onviewholder holder, int position, @NonNull NoteDataModel model) {

        if (model!=null)
        {
            holder.title.setText(truncateString(model.getFilename(),15));
            holder.edit.setVisibility(View.INVISIBLE);
            holder.delete.setVisibility(View.INVISIBLE);

            holder.see.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(model.getLink()),"application/pdf");
                    try{
                    view.getContext().startActivity(intent);
                    }
                    catch (ActivityNotFoundException e )
                    {
                        Constants.error(view.getContext(),"No application found to display the pdf");
                    }

                }
            });
        }
    }

    public static String truncateString(String input, int maxLength) {
        if (input.length() <= maxLength) {
            return input;
        } else {
            return input.substring(0, maxLength - 1) + "...";
        }
    }


    @NonNull
    @Override
    public onviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notelayout,parent,false);
        return new onviewholder(view);

    }

    public class onviewholder extends RecyclerView.ViewHolder {

        MaterialTextView title ;
        ImageButton see , edit , delete ;

        public onviewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            see = itemView.findViewById(R.id.see);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
