package com.guruprasad.tutionnotesaplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.guruprasad.tutionnotesaplication.Activities.Authentication.LoginActivity;
import com.guruprasad.tutionnotesaplication.Constants;
import com.guruprasad.tutionnotesaplication.databinding.ActivityMainBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            @Override
            public void run() {

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();

                } else {
                    String[] permissions = {
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.POST_NOTIFICATIONS
                    };
                    ActivityCompat.requestPermissions(MainActivity.this, permissions, 101);

                }
            }
        }, 1000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }
    }
}

//
// Manifest.permission.CAMERA,
//         Manifest.permission.INTERNET,
//         Manifest.permission.READ_EXTERNAL_STORAGE,
//         Manifest.permission.WRITE_EXTERNAL_STORAGE,
//         Manifest.permission.POST_NOTIFICATIONS,
//         Manifest.permission.CALL_PHONE