package com.monitorfordata.m4dpro;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

        TextView txtSite = findViewById(R.id.txtSite);
        txtSite.setText( Html.fromHtml("<a href=\"http://www.monitor4database.com\">www.monitor4database.com</a>"));
        txtSite.setMovementMethod(LinkMovementMethod.getInstance());

        TextView txtPrivacidade = findViewById(R.id.txtPrivacidade);
        txtPrivacidade.setText( Html.fromHtml("<a href=\"https://www.monitor4database.com/politica-de-privacidade\">Políticas de Privacidade</a>"));
        txtPrivacidade.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    //endregion
}
