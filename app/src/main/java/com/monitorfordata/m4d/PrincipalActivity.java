package com.monitorfordata.m4d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager viewpagerPrincipal;
    SharedPreferences pathDownloadAuto;
    SharedPreferences intervaloAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_principal);

            Toolbar toolbarPrincipal = findViewById(R.id.tPrincipal);
            setSupportActionBar(toolbarPrincipal);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.subtitulo);
            }

            viewpagerPrincipal = findViewById(R.id.vpPrincipal);

            carregarTabs();

            DrawerLayout dlPrincipal = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle actionBarDrawerTogglePrincipal = new ActionBarDrawerToggle(this, dlPrincipal, toolbarPrincipal, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            dlPrincipal.addDrawerListener(actionBarDrawerTogglePrincipal);
            actionBarDrawerTogglePrincipal.syncState();

            NavigationView nvPrincipal = findViewById(R.id.nvPrincipal);
            nvPrincipal.setNavigationItemSelectedListener(this);

            pathDownloadAuto = getSharedPreferences("preferencias", MODE_PRIVATE);
            String path = pathDownloadAuto.getString("path_auto", "");

            intervaloAuto = getSharedPreferences("preferencias", MODE_PRIVATE);
            String intervalo = intervaloAuto.getString("intervalo_Auto", "");

            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            }

            File criarPasta = new File(Environment.getExternalStorageDirectory() + "/M4D/");

            if (!criarPasta.exists()) {
                criarPasta.mkdirs();
            }

            if (path.equals("")) {
                pathDownloadAuto.edit().putString("path_auto", "/M4D/").apply();
            }

            if (intervalo.equals("")) {
                intervaloAuto.edit().putString("intervalo_Auto", "0").apply();
            }

        } catch (Exception erro) {
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.itemSobre) {
                Intent intentSobre = new Intent(PrincipalActivity.this, SobreActivity.class);
                startActivity(intentSobre);
            } else if (id == R.id.itemConfiguracoes) {
                Intent intentConfiguracoes = new Intent(PrincipalActivity.this, ConfiguracoesActivity.class);
                startActivity(intentConfiguracoes);
            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception erro) {
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            carregarTabs();
        }
    }
    //endregion

    //region [ MÉTODOS ]
    private void carregarTabs() {
        try {
            viewpagerPrincipal = findViewById(R.id.vpPrincipal);
            ViewPagerAdapter viewpageradapterPrincipal = new ViewPagerAdapter(getSupportFragmentManager());

            InformacoesFragment tabRedesSociais = new InformacoesFragment();
            viewpageradapterPrincipal.addFragment(tabRedesSociais, getString(R.string.imformation));

            DashBoardFragment tabRedesSociais2 = new DashBoardFragment();
            viewpageradapterPrincipal.addFragment(tabRedesSociais2, getString(R.string.dashboard));

            TabLayout tabLayoutPrincipal = findViewById(R.id.tlPrincipal);
            tabLayoutPrincipal.setupWithViewPager(viewpagerPrincipal);

            viewpagerPrincipal.setAdapter(viewpageradapterPrincipal);

        } catch (Exception erro) {

        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> listaFragmentos = new ArrayList<>();
        private final List<String> listaTitulos = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return listaFragmentos.get(position);
        }

        @Override
        public int getCount() {
            return listaFragmentos.size();
        }

        void addFragment(Fragment fragment, String title) {
            listaFragmentos.add(fragment);
            listaTitulos.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listaTitulos.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }
}
