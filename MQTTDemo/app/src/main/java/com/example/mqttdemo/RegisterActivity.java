package com.example.mqttdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {


    EditText editTextEmail,editTextPassword,editTextPasswordAgain,editTextUserName;
    Button registerbut;
    FirebaseAuth auth;
    TextView register;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth=FirebaseAuth.getInstance();
        register=findViewById(R.id.TextViewRegister);
        editTextUserName=(EditText) findViewById(R.id.TextPassword);
        editTextEmail= (EditText) findViewById(R.id.TextEmail);
        editTextPassword=(EditText) findViewById(R.id.editTextPassword);
        editTextPasswordAgain=(EditText) findViewById(R.id.editTextConfirmPassword);
        registerbut=findViewById(R.id.buttonRegister);


        TextView btn = findViewById(R.id.alreadyHaveAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
               startActivity(intent);
               finish();

            }
        });




        registerbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password;
                 email=String.valueOf(editTextEmail.getText());
                 password=String.valueOf(editTextPassword.getText());
                 
                 
                 if(TextUtils.isEmpty(email)){
                     Toast.makeText(RegisterActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                     return;
                 }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Authentication successful ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




            }
        });


    }
}