package com.guruprasad.tutionnotesaplication.Activities.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.guruprasad.tutionnotesaplication.Activities.NavigationActivity;
import com.guruprasad.tutionnotesaplication.Constants;
import com.guruprasad.tutionnotesaplication.R;
import com.guruprasad.tutionnotesaplication.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding ;

    FirebaseAuth auth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();

                    statusCheck(email,password);
            }
        });



    }

    private void statusCheck(String email , String password) {

        if (TextUtils.isEmpty(email))
        {
            binding.etEmail.setError("Email Required");
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            binding.etPassword.setError("Password Required");
            return;
        }

        login(email,password);
    }

    private void login(String email , String password) {

        ProgressDialog pd = Constants.progress_dialog(LoginActivity.this,"Please Wait","Logging User");
        pd.show();

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Constants.success(LoginActivity.this,"Login Successful");
                    startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                    finish();
                    pd.dismiss();
                }
                else
                {
                    Constants.error(LoginActivity.this,"Failed to login user : "+task.getException().getMessage());
                    pd.dismiss();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}