package com.opositores.android.ui.oppositions;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.opositores.android.R;
import com.opositores.android.data.local.prefs.SessionManager;
import com.opositores.android.databinding.ActivityOppositionListBinding;
import com.opositores.android.ui.login.LoginActivity;
import com.opositores.android.viewmodel.OppositionViewModel;

public class OppositionListActivity extends AppCompatActivity {

    private ActivityOppositionListBinding binding;
    private OppositionViewModel viewModel;
    private OppositionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOppositionListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis oposiciones");
        }

        viewModel = new ViewModelProvider(this).get(OppositionViewModel.class);

        setupRecyclerView();
        setupObservers();
        setupListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            new SessionManager(this).clearSession();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        adapter = new OppositionAdapter(opposition -> {
            Intent intent = new Intent(this, OppositionDetailActivity.class);
            intent.putExtra(OppositionDetailActivity.EXTRA_OPPOSITION_ID, opposition.id);
            intent.putExtra(OppositionDetailActivity.EXTRA_OPPOSITION_NAME, opposition.name);
            startActivity(intent);
        });
        binding.rvOppositions.setLayoutManager(new LinearLayoutManager(this));
        binding.rvOppositions.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.getOppositions().observe(this, oppositions -> {
            binding.progressBar.setVisibility(View.GONE);
            if (oppositions == null || oppositions.isEmpty()) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.tvEmpty.setVisibility(View.GONE);
                adapter.submitList(oppositions);
            }
        });

        viewModel.getError().observe(this, error -> {
            binding.progressBar.setVisibility(View.GONE);
            if (error != null) {
                com.google.android.material.snackbar.Snackbar
                        .make(binding.getRoot(), error, com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void setupListeners() {
        binding.fabAddOpposition.setOnClickListener(v -> showCreateDialog());
    }

    private void showCreateDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_opposition, null);
        TextInputEditText etName = dialogView.findViewById(R.id.etOppositionName);
        TextInputEditText etHours = dialogView.findViewById(R.id.etHoursPerWeek);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Nueva oposición")
                .setView(dialogView)
                .setPositiveButton("Crear", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String hoursStr = etHours.getText().toString().trim();
                    if (!name.isEmpty()) {
                        double hours = hoursStr.isEmpty() ? 10.0 : Double.parseDouble(hoursStr);
                        viewModel.createOpposition(name, null, null, hours);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
