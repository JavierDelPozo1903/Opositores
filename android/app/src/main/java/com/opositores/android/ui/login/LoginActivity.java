package com.opositores.android.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.opositores.android.R;
import com.opositores.android.data.local.prefs.SessionManager;
import com.opositores.android.databinding.ActivityLoginBinding;
import com.opositores.android.ui.oppositions.OppositionListActivity;
import com.opositores.android.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private boolean isLoginMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (new SessionManager(this).isLoggedIn()) {
            goToMain();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        updateMode();
        setupObservers();
        setupListeners();
    }

    private void updateMode() {
        if (isLoginMode) {
            binding.tvFormTitle.setText("Iniciar sesión");
            binding.tilName.setVisibility(View.GONE);
            binding.btnLogin.setText("Iniciar sesión");
            binding.btnRegister.setVisibility(View.GONE);
            binding.tvSwitchMode.setText(Html.fromHtml("¿No tienes cuenta? <b><font color='#3949AB'>Regístrate</font></b>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.tvFormTitle.setText("Crear cuenta");
            binding.tilName.setVisibility(View.VISIBLE);
            binding.btnLogin.setText("Crear cuenta");
            binding.btnRegister.setVisibility(View.GONE);
            binding.tvSwitchMode.setText(Html.fromHtml("¿Ya tienes cuenta? <b><font color='#3949AB'>Inicia sesión</font></b>", Html.FROM_HTML_MODE_COMPACT));
        }
    }

    private void setupObservers() {
        viewModel.getAuthResult().observe(this, result -> {
            setLoading(false);
            if (result.success) {
                goToMain();
            } else {
                Toast.makeText(this, result.errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.tvSwitchMode.setOnClickListener(v -> {
            isLoginMode = !isLoginMode;
            updateMode();
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString();

            if (!validateInputs(email, password)) return;

            setLoading(true);

            if (isLoginMode) {
                viewModel.login(email, password);
            } else {
                String name = binding.etName.getText().toString().trim();
                if (name.isEmpty()) {
                    binding.tilName.setError("Introduce tu nombre");
                    setLoading(false);
                    return;
                }
                viewModel.register(email, password, name);
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        boolean valid = true;
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Email inválido");
            valid = false;
        } else {
            binding.tilEmail.setError(null);
        }
        if (password.length() < 8) {
            binding.tilPassword.setError("Mínimo 8 caracteres");
            valid = false;
        } else {
            binding.tilPassword.setError(null);
        }
        return valid;
    }

    private void setLoading(boolean loading) {
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!loading);
        binding.tvSwitchMode.setEnabled(!loading);
    }

    private void goToMain() {
        startActivity(new Intent(this, OppositionListActivity.class));
        finish();
    }
}
