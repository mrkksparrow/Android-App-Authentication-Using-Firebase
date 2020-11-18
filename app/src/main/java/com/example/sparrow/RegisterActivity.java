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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView gologin;
    private Button register;
    private EditText xname, xphone, xemail, xpassword;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        gologin = (TextView) findViewById(R.id.gologin);
        gologin.setOnClickListener(this);

        xname = (EditText) findViewById(R.id.name);
        xphone = (EditText) findViewById(R.id.phone);
        xemail = (EditText) findViewById(R.id.email);
        xpassword = (EditText) findViewById(R.id.password);

        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gologin:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register:
                register();
                break;
        }

    }

    private void register() {
        final String email = xemail.getText().toString().trim();
        final String phone = xphone.getText().toString().trim();
        final String name = xname.getText().toString().trim();
        String password = xpassword.getText().toString().trim();

        if(name.isEmpty()){
            xname.setError("name required");
            xname.requestFocus();
            return ;
        }

        if(phone.isEmpty()){
            xphone.setError("phone required");
            xphone.requestFocus();
            return ;
        }

        if(email.isEmpty()){
            xemail.setError("email required");
            xemail.requestFocus();
            return ;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            xemail.setError("please provide valid email");
            xemail.requestFocus();
            return ;
        }

        if(password.isEmpty()){
            xpassword.setError("password required");
            xpassword.requestFocus();
            return ;
        }

        if (password.length() < 8){
            xpassword.setError("weak password");
            xpassword.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name, phone, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "User has been registered successfullt!", Toast.LENGTH_LONG).show();
                                        progressbar.setVisibility(View.GONE);
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, "Failed to register! Try again", Toast.LENGTH_LONG).show();
                                        progressbar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this, "Failed to register! Try again", Toast.LENGTH_LONG).show();
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });

    }
    @Override
    public void onBackPressed() {
    }
}
