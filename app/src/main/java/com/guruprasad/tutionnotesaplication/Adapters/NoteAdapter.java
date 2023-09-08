package com.guruprasad.tutionnotesaplication.Adapters;

import static com.google.android.gms.common.wrappers.Wrappers.packageManager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.guruprasad.tutionnotesaplication.Activities.ui.CreateNote.CreateNoteActivity;
import com.guruprasad.tutionnotesaplication.Constants;
import com.guruprasad.tutionnotesaplication.Models.NoteModel;
import com.guruprasad.tutionnotesaplication.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.onviewholder> {
    private Context context ;
    private List<NoteModel> model ;

    public NoteAdapter(Context context, List<NoteModel> model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public onviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_layout,parent,false);
       return new onviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull onviewholder holder, int position) {

        PackageManager packageManager = context.getPackageManager();

        NoteModel noteModel = model.get(position);

        holder.filename.setText(noteModel.getFilename());

        holder.see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noteModel.getFile()!=null)
                {
                   Uri fileuri = noteModel.getFile();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(fileuri,"application/*");
                    if (intent.resolveActivity(context.getPackageManager()) !=null)
                    {
                        context.startActivity(intent);
                    }
                    else
                    {
                        Constants.error(context,"No application is available to view the file");
                    }
                }
            }
        });

        holder.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position,view.getContext());
            }
        });


    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public void removeItem(int position , Context context) {
        if (position>=0 && position< model.size())
        {
        model.remove(position); // Remove the item from the data source
        notifyItemRemoved(position); // Notify the adapter that an item has been removed
        }
        else
        {
            Constants.error(context, "Postion Out of Bounds Please try again later");
        }
    }


    public class onviewholder extends RecyclerView.ViewHolder {

        MaterialTextView filename ;
        ImageButton see , upload , remove ;


        public onviewholder(@NonNull View itemView) {
            super(itemView);

            filename = itemView.findViewById(R.id.file_name);
            see = itemView.findViewById(R.id.see);
            upload = itemView.findViewById(R.id.upload);
            remove = itemView.findViewById(R.id.remove);
        }
    }
}
