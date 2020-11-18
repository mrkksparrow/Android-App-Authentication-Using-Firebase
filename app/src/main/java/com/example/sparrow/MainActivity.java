package com.example.sparrow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgot;
    private EditText xemail, xpassword;
    private Button login;

    private FirebaseAuth mAuth;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        forgot = (TextView) findViewById(R.id.forgot);
        forgot.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        xemail = (EditText) findViewById(R.id.email);
        xpassword = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        progressbar = (ProgressBar) findViewById(R.id.progressbar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.forgot:
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
                break;

            case R.id.login:
                login();
                break;
        }
    }

    private void login() {
        String email = xemail.getText().toString().trim();
        String password = xpassword.getText().toString().trim();

        if (email.isEmpty()){
            xemail.setError("email required");
            xemail.requestFocus();
            return ;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            xemail.setError("email not valid");
            xemail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            xpassword.setError("password required");
            xpassword.requestFocus();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, Dashboard.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "verify your mail id",Toast.LENGTH_LONG).show();
                        progressbar.setVisibility(View.GONE);
                    }

                }else{
                    Toast.makeText(MainActivity.this, "Invalid email and pasword", Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
    }
}
