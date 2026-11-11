package com.example.recyclerprojectdemo;

import static android.app.ProgressDialog.show;
import static com.example.recyclerprojectdemo.FirebaseRef.refAuth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

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
        Boolean isChecked = sharedPref.getBoolean("stayConnect",false);
        // change so if it is ok, move to next activity
        if (isChecked && refAuth.getCurrentUser()!=null) {
            Intent intent = new Intent(SignIn.this,MainActivity.class);
            startActivity(intent);
        }

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
                        EditText etEmail = customDialog.findViewById(R.id.etRegEmail);
                        EditText etPass = customDialog.findViewById(R.id.etRegPass);
                        String email = etEmail.getText().toString();
                        String pass = etPass.getText().toString();
                        refAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {     // נוציא הודעה שהצלחנו
                                            FirebaseUser user = refAuth.getCurrentUser();
                                            Toast.makeText(SignIn.this, user.getUid(),
                                                    Toast.LENGTH_LONG).show();
                                            customDialog.dismiss();
                                        } else {
                                            Toast.makeText(SignIn.this, "Error. Please try again later.",
                                            Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

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