package com.guruprasad.tutionnotesaplication.Activities.ui.CreateNote;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.ktx.Firebase;
import com.guruprasad.tutionnotesaplication.Adapters.NotesRecyclerViewAdapter;
import com.guruprasad.tutionnotesaplication.Constants;
import com.guruprasad.tutionnotesaplication.Models.NoteDataModel;
import com.guruprasad.tutionnotesaplication.databinding.FragmentHomeBinding;

public class CreateNoteFragment extends Fragment {

    private FragmentHomeBinding binding;
    private NotesRecyclerViewAdapter adapter ;
    FirebaseDatabase database;
    FirebaseAuth auth ;
    private ProgressDialog pd ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        pd = Constants.progress_dialog(getContext() , "Please Wait" , "Fetching Data...");
        pd.show();

        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CreateNoteActivity.class));
            }
        });
        binding.recyclerview.setLayoutManager(new WrapContentLinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        Query query = database.getReference().child("Notes").child(auth.getCurrentUser().getUid());
        FirebaseRecyclerOptions<NoteDataModel> options = new FirebaseRecyclerOptions.Builder<NoteDataModel>().setQuery(query,NoteDataModel.class).build();
        adapter = new NotesRecyclerViewAdapter(options,getContext()){
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                pd.dismiss();

            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
                Constants.error(getContext(),"Error : "+error.getMessage());
                pd.dismiss();
            }
        };
        binding.recyclerview.setAdapter(adapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}