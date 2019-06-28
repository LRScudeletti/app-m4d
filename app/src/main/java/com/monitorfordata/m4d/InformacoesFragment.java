package com.monitorfordata.m4d;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class InformacoesFragment extends Fragment {

    List<InformacoesClass> listaInformacoes = new ArrayList<>();
    public static List<InformacoesClass> listaCharts = new ArrayList<>();
    RecyclerView rvInformacoes;
    TextView txtMensagem;
    String path;

    private int intervaloAtualizacao = 0;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_informacoes, container, false);

        rvInformacoes = rootView.findViewById(R.id.rvInformacoes);
        rvInformacoes.setHasFixedSize(true);
        rvInformacoes.setNestedScrollingEnabled(false);
        rvInformacoes.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        LinearLayoutManager llmExercicios = new LinearLayoutManager(getActivity());
        llmExercicios.setOrientation(LinearLayoutManager.VERTICAL);

        rvInformacoes.setLayoutManager(llmExercicios);
        txtMensagem = rootView.findViewById(R.id.txtMensagem);

        SharedPreferences pathDownloadAuto = getActivity().getSharedPreferences("preferencias", MODE_PRIVATE);
        path = pathDownloadAuto.getString("path_auto", "");

        mHandler = new Handler();

        SharedPreferences intervaloAuto = getActivity().getSharedPreferences("preferencias", MODE_PRIVATE);
        String intervalo = intervaloAuto.getString("intervalo_Auto", "");

        if(!intervalo.equals("0") && !intervalo.equals("")){
            intervaloAtualizacao =  Integer.parseInt(intervalo) * 1000;
            iniciarAtualizacao();
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_principal, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAdicionar: {
                abrirArquivoManual();
                break;
            }
            case R.id.itemAtualizar:
                if (!path.equals(""))
                    abrirArquivoAuto(path);
                break;
        }
        return false;
    }

    public void abrirArquivoAuto(String diretorio) {
        String json;
        listaInformacoes.clear();
        listaCharts.clear();

        try {
            File f = new File(Environment.getExternalStorageDirectory(), diretorio);

            File[] files = f.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.isFile();
                }
            });

            if (files.length != 0) {
                long lastMod = Long.MIN_VALUE;

                File ultimo = null;
                FileInputStream stream;

                for (File file : files) {

                    String extensao = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));

                    if (file.lastModified() > lastMod && extensao.equals(".json")) {
                        ultimo = file;
                        lastMod = file.lastModified();
                    }
                }

                stream = new FileInputStream(ultimo);
                FileChannel fc = stream.getChannel();

                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                json = Charset.defaultCharset().decode(bb).toString();

                stream.close();
            } else {
                InputStream is = getActivity().getAssets().open("example.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            }

            try {
                JSONObject obj = new JSONObject(json);
                JSONArray array = obj.getJSONArray("resultado");
                JSONObject row = array.getJSONObject(0);

                Iterator x = row.keys();

                int contador = 0;

                String idioma = Locale.getDefault().getLanguage();
                Date data = Calendar.getInstance().getTime();

                while (x.hasNext()) {
                    if (contador == 0) {
                        if (idioma.equals("pt")) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
                            listaInformacoes.add(new InformacoesClass(getString(R.string.data_atualizacao_app), sdf.format(data), Color.parseColor("#32CD32")));
                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.getDefault());
                            listaInformacoes.add(new InformacoesClass(getString(R.string.data_atualizacao_app), sdf.format(data), Color.parseColor("#32CD32")));
                        }

                        contador++;
                    } else if (contador > 4) {
                        String key = (String) x.next();
                        String[] campo = key.replace("_", " ").split("-");
                        if (idioma.equals("pt")) {
                            listaInformacoes.add(new InformacoesClass(campo[0], row.getString(key), Color.parseColor(campo[2])));
                        } else {
                            listaInformacoes.add(new InformacoesClass(campo[1], row.getString(key), Color.parseColor(campo[2])));
                        }
                        contador++;
                    } else {
                        String key = (String) x.next();
                        String[] campo = key.replace("_", " ").split("-");
                        if (idioma.equals("pt")) {
                            listaCharts.add(new InformacoesClass(campo[0], row.getString(key), Color.parseColor(campo[2])));
                        } else {
                            listaCharts.add(new InformacoesClass(campo[1], row.getString(key), Color.parseColor(campo[2])));
                        }
                        contador++;
                    }
                }

                carregarRecyclerView(listaInformacoes);

                txtMensagem.setVisibility(View.GONE);
                rvInformacoes.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.invalido), Toast.LENGTH_SHORT);
                TextView v = toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
            }

        } catch (FileNotFoundException e) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.invalido), Toast.LENGTH_SHORT);
            TextView v = toast.getView().findViewById(android.R.id.message);
            if (v != null) v.setGravity(Gravity.CENTER);
            toast.show();

        } catch (IOException e) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.invalido), Toast.LENGTH_SHORT);
            TextView v = toast.getView().findViewById(android.R.id.message);
            if (v != null) v.setGravity(Gravity.CENTER);
            toast.show();
        } catch (Exception e) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.invalido), Toast.LENGTH_SHORT);
            TextView v = toast.getView().findViewById(android.R.id.message);
            if (v != null) v.setGravity(Gravity.CENTER);
            toast.show();
        }
    }

    private void abrirArquivoManual() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/octet-stream");

        startActivityForResult(intent, 991);
    }

    private void carregarJson(Uri uri) {
        String json;
        listaInformacoes.clear();
        listaCharts.clear();

        if (uri != null) {
            try {
                InputStream is = getActivity().getContentResolver().openInputStream(uri);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");

                JSONObject obj = new JSONObject(json);

                JSONArray array = obj.getJSONArray("resultado");

                JSONObject row = array.getJSONObject(0);

                Iterator x = row.keys();

                int contador = 0;

                String idioma = Locale.getDefault().getLanguage();
                Date data = Calendar.getInstance().getTime();

                while (x.hasNext()) {

                    if (contador == 0) {
                        if (idioma.equals("pt")) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
                            listaInformacoes.add(new InformacoesClass(getString(R.string.data_atualizacao_app), sdf.format(data), Color.parseColor("#32CD32")));
                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.getDefault());
                            listaInformacoes.add(new InformacoesClass(getString(R.string.data_atualizacao_app), sdf.format(data), Color.parseColor("#32CD32")));
                        }

                        contador++;
                    } else if (contador > 4) {
                        String key = (String) x.next();
                        String[] campo = key.replace("_", " ").split("-");
                        idioma = Locale.getDefault().getLanguage();
                        if (idioma.equals("pt")) {
                            listaInformacoes.add(new InformacoesClass(campo[0], row.getString(key), Color.parseColor(campo[2])));
                        } else {
                            listaInformacoes.add(new InformacoesClass(campo[1], row.getString(key), Color.parseColor(campo[2])));
                        }
                        contador++;
                    } else {
                        String key = (String) x.next();
                        String[] campo = key.replace("_", " ").split("-");
                        idioma = Locale.getDefault().getLanguage();
                        if (idioma.equals("pt")) {
                            listaCharts.add(new InformacoesClass(campo[0], row.getString(key), Color.parseColor(campo[2])));
                        } else {
                            listaCharts.add(new InformacoesClass(campo[1], row.getString(key), Color.parseColor(campo[2])));
                        }
                        contador++;
                    }
                }

                carregarRecyclerView(listaInformacoes);

                txtMensagem.setVisibility(View.GONE);
                rvInformacoes.setVisibility(View.VISIBLE);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

                Toast toast = Toast.makeText(getActivity(), getString(R.string.invalido), Toast.LENGTH_SHORT);
                TextView v = toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
            } catch (IOException e) {
                e.printStackTrace();

                Toast toast = Toast.makeText(getActivity(), getString(R.string.invalido), Toast.LENGTH_SHORT);
                TextView v = toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
            } catch (JSONException e) {
                e.printStackTrace();

                Toast toast = Toast.makeText(getActivity(), getString(R.string.invalido), Toast.LENGTH_SHORT);
                TextView v = toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
            } catch (Exception erro) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.invalido), Toast.LENGTH_SHORT);
                TextView v = toast.getView().findViewById(android.R.id.message);
                if (v != null) v.setGravity(Gravity.CENTER);
                toast.show();
            }
        }
    }

    private void carregarRecyclerView(List<InformacoesClass> lista) {
        ArrayList<InformacoesClass> informacoesClasses = new ArrayList<>();

        for (int i = 0; i < lista.size(); i++) {
            InformacoesClass linhaItemClass = new InformacoesClass(lista.get(i).getNome(), lista.get(i).getInformacao(), lista.get(i).getCor());
            informacoesClasses.add(linhaItemClass);
        }

        InformacoesAdapter informacoesAdapter = new InformacoesAdapter(informacoesClasses);
        // informacoesAdapter.setRecyclerViewButtonClickClass(this);
        // informacoesAdapter.setRecyclerViewImageButtonClickClass(this);

        rvInformacoes.setAdapter(informacoesAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == 991 && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                carregarJson(uri);
            }
        }
    }

    Runnable checkEstado = new Runnable() {
        @Override
        public void run() {
            try {
                abrirArquivoAuto(path);
            } finally {
                mHandler.postDelayed(checkEstado, intervaloAtualizacao);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        pararAtualizacao();
    }

    void iniciarAtualizacao() {
        checkEstado.run();
    }

    void pararAtualizacao() {
        mHandler.removeCallbacks(checkEstado);
    }
}