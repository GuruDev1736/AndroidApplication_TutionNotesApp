package com.guruprasad.tutionnotesaplication.Activities.ui.CreateNote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.guruprasad.tutionnotesaplication.Activities.NavigationActivity;
import com.guruprasad.tutionnotesaplication.Adapters.NoteAdapter;
import com.guruprasad.tutionnotesaplication.Constants;
import com.guruprasad.tutionnotesaplication.CustomDialog;
import com.guruprasad.tutionnotesaplication.Models.NoteDataModel;
import com.guruprasad.tutionnotesaplication.Models.NoteModel;
import com.guruprasad.tutionnotesaplication.R;
import com.guruprasad.tutionnotesaplication.databinding.ActivityCreateNoteBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateNoteActivity extends AppCompatActivity {

    ActivityCreateNoteBinding binding ;
    List<NoteModel> datalist = new ArrayList<>();
    NoteAdapter adapter ;

    FirebaseDatabase database ;
    FirebaseAuth auth ;
    private String filename ;
    private Uri file ;
    private int count ;
    private String UniqueKey ;
    private String UserId;
    private String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        UniqueKey = UUID.randomUUID().toString();
        UserId = auth.getCurrentUser().getUid();

        binding.actionbar.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNoteActivity.super.onBackPressed();
            }
        });

        binding.actionbar.activityName.setText("Create Note");
        binding.actionbar.files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(CreateNoteActivity.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                                String title = binding.title.getText().toString();
                                String content = binding.note.getText().toString();

                                if (title.isEmpty() || content.isEmpty())
                                {
                                    Constants.error(CreateNoteActivity.this,"Please upload your note first");
                                }
                                else {
                                Intent intent = new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select the File."),101);
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(this,datalist);
        count = adapter.getItemCount();
        binding.recyclerview.setAdapter(adapter);


        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialog dialog = new CustomDialog(CreateNoteActivity.this);
                //dialog.title("Creating New Note");
                dialog.show();


                String title = binding.title.getText().toString();
                String note = binding.note.getText().toString();

                if (TextUtils.isEmpty(title))
                {
                    Constants.error(CreateNoteActivity.this,"Title is necessary before creating the note");
                    return;
                }
                if (TextUtils.isEmpty(note))
                {
                    Constants.error(CreateNoteActivity.this,"Note is null please enter the input");
                    return;
                }

                dialog.show();

                NoteDataModel model  = new NoteDataModel(title,note,UniqueKey,UserId);
                database.getReference().child("Notes").child(UserId).child(UniqueKey).setValue(model)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Constants.success(CreateNoteActivity.this,"Note Created Successfully");
                                    binding.create.setVisibility(View.INVISIBLE);
                                    dialog.dismiss();
                                }
                                else
                                {
                                    Constants.error(CreateNoteActivity.this,"Failed to create Note : "+task.getException().getMessage());
                                    dialog.dismiss();
                                }
                            }
                        });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101  && resultCode==RESULT_OK && data!=null)
        {
            file = data.getData();

           if (file!=null)
           {
               filename = truncateString(getFileName(file),10);
               filepath = file.getPath();

           }
            datalist.add(new NoteModel(filename,binding.title.getText().toString(),binding.note.getText().toString(),UniqueKey,UserId,filepath,file));
            adapter.notifyDataSetChanged();
        }

    }

    public static String truncateString(String input, int maxLength) {
        if (input.length() <= maxLength) {
            return input;
        } else {
            return input.substring(0, maxLength - 1) + "...";
        }
    }

    private String getFileName(Uri uri) {
        String fileName = null;
        String scheme = uri.getScheme();

        if (scheme != null && scheme.equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        fileName = cursor.getString(index);
                    }
                }
            }
        }

        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }

        return fileName;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}