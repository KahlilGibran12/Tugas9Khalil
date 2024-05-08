package com.example.tugas9khalil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tugas9khalil.api.ApiClient;
import com.example.tugas9khalil.api.ApiInterface;
import com.example.tugas9khalil.model.login.Login;
import com.example.tugas9khalil.model.login.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edUsername, edPassword;
    Button btnLogin;
    TextView btnRegister;
    ApiInterface apiInterface;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edUsername = findViewById(R.id.etUsername);
        edPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.tvCreateAccount);

        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        sessionManager = new SessionManager(LoginActivity.this);

        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        btnLogin.setOnClickListener(v -> {
            String username = edUsername.getText().toString().trim();
            String password = edPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Username dan password harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            performLogin(username, password);
        });
    }

    private void performLogin(String username, String password) {
        Call<Login> loginCall = apiInterface.loginResponse(username, password);
        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginData data = response.body().getLoginData();
                    sessionManager.createLoginSession(data);

                    Toast.makeText(LoginActivity.this, data.getName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    String errorMessage = "Login gagal, silakan coba lagi";
                    if (response.errorBody() != null) {
                        errorMessage = response.errorBody().toString();
                    }
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "Gagal terhubung ke server, silakan coba lagi nanti", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
