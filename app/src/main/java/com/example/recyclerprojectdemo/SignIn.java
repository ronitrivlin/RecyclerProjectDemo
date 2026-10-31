package com.example.recyclerprojectdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignIn extends AppCompatActivity {

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPref = getSharedPreferences("MyDemoAdd2627", Context.MODE_PRIVATE);
//        Boolean isChecked = sharedPref.getBoolean("stayConnect",false);
//        // change so if it is ok, move to next activity
//        if (isChecked) {
//            Intent intent = new Intent(SignIn.this,MainActivity.class);
//            startActivity(intent);
//        }

        Button signIn = findViewById(R.id.btnSignin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox ckRemember = findViewById(R.id.cbRemember);
                rememberMe(ckRemember);
                Intent intent = new Intent(SignIn.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button signup = findViewById(R.id.btnSignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = getLayoutInflater().inflate(R.layout.sign_up_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                builder.setView(dialogView);
                final AlertDialog customDialog = builder.create();
                customDialog.show();
                Button reg = customDialog.findViewById(R.id.btnRegister);
                reg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customDialog.findViewById(R.id.etRegName);
                    }
                });
            }
        });

    }

    private void rememberMe(CheckBox ckRemember) {
      //  if (ckRemember.isChecked()) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("stayConnect", ckRemember.isChecked());
            editor.commit();
    //    }
    }
}