package com.monitorfordata.m4d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfiguracoesActivity extends AppCompatActivity {
    SharedPreferences pathDownloadAuto;
    SharedPreferences intervaloAuto;

    EditText txtPath;
    EditText txtIntervaloAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuracoes);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.action_settings);
        }

        pathDownloadAuto = getSharedPreferences("preferencias", MODE_PRIVATE);
        String path = pathDownloadAuto.getString("path_auto", "");

        intervaloAuto = getSharedPreferences("preferencias", MODE_PRIVATE);
        String intervalo = intervaloAuto.getString("intervalo_Auto", "");

        txtPath = findViewById(R.id.txtPath);
        txtIntervaloAuto = findViewById(R.id.txtIntervaloAuto);

        txtPath.setText(path);
        txtIntervaloAuto.setText(intervalo);

        Button btnSalvar = findViewById(R.id.btnSalvvar);

        btnSalvar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtPath.getText().toString().equals("")) {
                    pathDownloadAuto.edit().putString("path_auto", txtPath.getText().toString()).apply();
                    intervaloAuto.edit().putString("intervalo_Auto", txtIntervaloAuto.getText().toString()).apply();
                    Snackbar.make(v, getString(R.string.reiniciar_app), Snackbar.LENGTH_LONG).setAction("", null).show();
                } else {
                    Snackbar.make(v, getString(R.string.informe_diretorio), Snackbar.LENGTH_LONG).setAction("", null).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
