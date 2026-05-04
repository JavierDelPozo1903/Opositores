package com.opositores.android.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.opositores.android.data.local.prefs.SessionManager;
import com.opositores.android.databinding.ActivityLoginBinding;
import com.opositores.android.ui.oppositions.OppositionListActivity;
import com.opositores.android.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Si ya hay sesión activa, ir directamente a la lista de oposiciones
        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            goToMain();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        setupObservers();
        setupListeners();
    }

    private void setupObservers() {
        viewModel.getAuthResult().observe(this, result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnLogin.setEnabled(true);
            binding.btnRegister.setEnabled(true);

            if (result.success) {
                goToMain();
            } else {
                Toast.makeText(this, result.errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString();

            if (!validateInputs(email, password)) return;

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnLogin.setEnabled(false);
            binding.btnRegister.setEnabled(false);
            viewModel.login(email, password);
        });

        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString();
            String name = binding.etName.getText().toString().trim();

            if (name.isEmpty()) {
                binding.etName.setError("Introduce tu nombre");
                return;
            }
            if (!validateInputs(email, password)) return;

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnLogin.setEnabled(false);
            binding.btnRegister.setEnabled(false);
            viewModel.register(email, password, name);
        });
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Email inválido");
            return false;
        }
        if (password.length() < 8) {
            binding.etPassword.setError("Mínimo 8 caracteres");
            return false;
        }
        return true;
    }

    private void goToMain() {
        startActivity(new Intent(this, OppositionListActivity.class));
        finish();
    }
}
