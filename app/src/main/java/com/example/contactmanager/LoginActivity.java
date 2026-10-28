package com.example.contactmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(view -> login());
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (!"admin".equals(username) || !"123456".equals(password)) {
            tvError.setText(R.string.login_error);
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
