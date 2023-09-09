package com.guruprasad.tutionnotesaplication.Adapters;

import static com.google.android.gms.common.wrappers.Wrappers.packageManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.guruprasad.tutionnotesaplication.Activities.ui.CreateNote.CreateNoteActivity;
import com.guruprasad.tutionnotesaplication.Constants;
import com.guruprasad.tutionnotesaplication.Models.NoteModel;
import com.guruprasad.tutionnotesaplication.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.onviewholder> {
    private Context context ;
    private List<NoteModel> model ;

    FirebaseDatabase database = FirebaseDatabase.getInstance() ;
    FirebaseStorage storage =  FirebaseStorage.getInstance();

    FirebaseAuth auth = FirebaseAuth.getInstance();

    private String linkNo ;


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

                    if (noteModel.getFile()==null)
                    {
                        Constants.error(context,"Please select the file");
                        return;
                    }
                    if (model.size()>4)
                    {
                        Constants.error(context,"Please select the files less than 4");
                        return;
                    }
                    if (noteModel.getTitle()==null)
                    {
                        Constants.error(context,"Title is required");
                        return;
                    }
                    if (noteModel.getUserId()==null)
                    {
                        Constants.error(context,"UserId is null");
                        return;
                    }
                    if (noteModel.getUniqueKey()==null)
                    {
                        Constants.error(context,"Reference key is null");
                    }

                    uploaddata(noteModel.getFile() , getItemCount(), noteModel.getUserId(), noteModel.getUniqueKey() , holder);
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position,view.getContext());
            }
        });


    }

    private void uploaddata(Uri file , int size , String userid , String referenceId , onviewholder holder) {


        ProgressDialog pd = Constants.progress_dialog(context,"Please Wait","Uploading your attachment");
        pd.show();

        final StorageReference reference= storage.getReference().child("Attachments").child(auth.getCurrentUser().getUid());
        reference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        if (size==1)
                        {
                            linkNo = "link1";
                        }
                        if (size==2)
                        {
                            linkNo = "link2";
                        }
                        if (size==3)
                        {
                            linkNo = "link3";
                        }
                        if (size==4)
                        {
                            linkNo="link4";
                        }

                        database.getReference().child("Notes").child(userid).child(referenceId).child(linkNo).setValue(uri.toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Constants.success(context,"File Uploaded Successfully");
                                        pd.dismiss();
                                        holder.upload.setVisibility(View.INVISIBLE);
                                        holder.remove.setVisibility(View.INVISIBLE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Constants.error(context,"Unable to upload file : "+e.getMessage());
                                        pd.dismiss();
                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Constants.error(context,"Unable to upload file : "+e.getMessage());
                        pd.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Constants.error(context,"Unable to upload file : "+e.getMessage());
                pd.dismiss();
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
