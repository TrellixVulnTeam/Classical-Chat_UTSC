package com.epicodus.classicalchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.register_toolbar) Toolbar mToolbar;

    @Bind(R.id.regDisplayName) TextInputLayout mDisplayName;
    @Bind(R.id.regEmail) TextInputLayout mEmail;
    @Bind(R.id.regPassword) TextInputLayout mPassword;
    @Bind(R.id.regConfirmPassword) TextInputLayout mConfirmPassword;

    @Bind(R.id.regCreateBtn) Button mCreateBtn;

    private ProgressDialog mRegProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mCreateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String display_name = mDisplayName.getEditText().getText().toString().trim();
        String email = mEmail.getEditText().getText().toString().trim();
        String password = mPassword.getEditText().getText().toString().trim();
        String confirm_password = mConfirmPassword.getEditText().getText().toString().trim();

        boolean validName = isValidName(display_name);
        boolean validEmail = isValidEmail(email);
        boolean validPassword = isValidPassword(password, confirm_password);
        if (!validEmail || !validName || !validPassword) return;

        if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {

            mRegProgress.setTitle("Registering User");
            mRegProgress.setMessage("Please wait while we create your account.");
            mRegProgress.setCanceledOnTouchOutside(false);
            mRegProgress.show();

            register_user(display_name, email, password, confirm_password);
        }


    }

    private void register_user(String display_name, String email, String password, String confirm_password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    mRegProgress.dismiss();

                    Intent main_intent = new Intent (RegisterActivity.this, MainActivity.class);
                    main_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(main_intent);
                    finish();
                } else {

                    mRegProgress.hide();
                    Toast.makeText(RegisterActivity.this, "Cannot sign in. Please check the form and try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEmail.setError("Please enter a valid email address");
            return false;
        }
        return isGoodEmail;
    }

    private boolean isValidName(String name) {
        if (name.equals("")) {
            mDisplayName.setError("Please enter your name");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            mPassword.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            mPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }
}
