package com.monitorfordata.m4d;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SobreActivity extends AppCompatActivity {

    //region [ EVENTOS ]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sobre);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.tela_sobre);
        }

        TextView txtSobre = findViewById(R.id.txtSobre);
        String nomeVersao = BuildConfig.VERSION_NAME;

        txtSobre.setText(getString(R.string.versão, nomeVersao));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    //endregion
}
