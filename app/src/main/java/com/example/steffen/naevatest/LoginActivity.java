package com.example.steffen.naevatest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText name;
    private EditText workplace;
    private EditText password;
    private EditText retypePassword;
    private EditText sign_email;
    private EditText sign_password;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    //Method triggered by button, attempt to login if the inputs are non empty
    public void signIn(View v) {
        refreshData();
        String temp_email = sign_email.getText().toString();
        String temp_password = sign_password.getText().toString();
        if (!isEmpty(temp_email, temp_password)) {
            fireSignIn(temp_email, temp_password);
        }
    }

    //Checks if the input fields are empty and throws response to user
    public boolean isEmpty(String email, String password) {
        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill in email and password",
                    Toast.LENGTH_LONG).show();
            return true;
        }
        if (email.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill in email",
                    Toast.LENGTH_LONG).show();
            return true;
        }
        if (password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill in password",
                    Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    // Firebase auth login method
    public void fireSignIn(String emails, String password) {
        mAuth.signInWithEmailAndPassword(emails, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            ;
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // ...
                    }
                });
    }

    //Redirects user if login successful
    public void updateUI(FirebaseUser user) {
        if (user == null) {
            setContentView(R.layout.activity_login);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //Creates a firebase user if the input fields are filled in properly
    public void createUser(View v) {
        refreshData();
        if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill all the fields",
                    Toast.LENGTH_LONG).show();
        } else if (password.getText().toString().equals(retypePassword.getText().toString())) {
            createFireUser();
        } else {
            Toast.makeText(LoginActivity.this, "Passwords does not match",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void clickSignUp(View v) {
        setContentView(R.layout.activity_signup);
    }

    //Method to create firbase user
    private void createFireUser() {
        final String temp_email = email.getText().toString();
        String temp_password = password.getText().toString();
        mAuth.createUserWithEmailAndPassword(temp_email, temp_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User userToDb=new User(name.getText().toString(),workplace.getText().toString(),"123456789",randomHexString(10),randomHexString(6));
                            db.collection("steffen").document(temp_email)
                                    .set(userToDb)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    //Refreshes link between code and elements in layout
    private void refreshData() {
        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        retypePassword = (EditText) findViewById(R.id.et_passwordretype);
        name=(EditText) findViewById(R.id.et_name);
        workplace=(EditText)findViewById(R.id.et_workplace);
        sign_email = (EditText) findViewById(R.id.signin_email);
        sign_password = (EditText) findViewById(R.id.signin_password);
    }
    private String randomHexString(int length) {
        byte[] buf = new byte[length];
        new Random().nextBytes(buf);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(String.format("%02X", buf[i]));
        }
        return stringBuilder.toString();
    }
}
