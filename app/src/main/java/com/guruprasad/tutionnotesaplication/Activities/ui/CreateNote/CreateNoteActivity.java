package com.guruprasad.tutionnotesaplication.Activities.ui.CreateNote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;

import com.guruprasad.tutionnotesaplication.Adapters.NoteAdapter;
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

public class CreateNoteActivity extends AppCompatActivity {

    ActivityCreateNoteBinding binding ;
    List<NoteModel> datalist = new ArrayList<>();
    NoteAdapter adapter ;
    private String filename ;
    private Uri file ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                                Intent intent = new Intent();
                                intent.setType("*/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select the File."),101);
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
        binding.recyclerview.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101  && resultCode==RESULT_OK && data!=null)
        {
            file = data.getData();

           if (file!=null)
           {
               filename = getFileName(file);
           }
            datalist.add(new NoteModel(filename,file));
            adapter.notifyDataSetChanged();
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
            // If unable to get the file name from content resolver, try parsing the Uri
            fileName = uri.getLastPathSegment();
        }

        return fileName;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}