package com.monitorfordata.m4d;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class InformacoesFragment extends Fragment {

    List<DadosClass> listaInformacoes = new ArrayList<>();
    public static List<DadosClass> listaCharts = new ArrayList<>();
    CustomListView listView;
    TextView txtMensagem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_informacoes, container, false);

        listView = rootView.findViewById(R.id.lvInformacoes);
        txtMensagem = rootView.findViewById(R.id.txtMensagem);

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
                abrirArquivo();
            }
            case R.id.itemAtualizar:
                break;
        }
        return false;
    }

    public void carregarJson(Uri uri) {
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

                while (x.hasNext()) {
                    if (contador > 3) {
                        String key = (String) x.next();
                        String[] campo = key.replace("_", " ").split("-");
                        String idioma = Locale.getDefault().getLanguage();
                        if (idioma == "pt") {
                            listaInformacoes.add(new DadosClass(campo[0], row.getString(key), Color.parseColor(campo[2])));
                        } else {
                            listaInformacoes.add(new DadosClass(campo[1], row.getString(key), Color.parseColor(campo[2])));
                        }
                        contador++;
                    } else {
                        String key = (String) x.next();
                        String[] campo = key.replace("_", " ").split("-");
                        String idioma = Locale.getDefault().getLanguage();
                        if (idioma == "pt") {
                            listaCharts.add(new DadosClass(campo[0], row.getString(key), Color.parseColor(campo[2])));
                        } else {
                            listaCharts.add(new DadosClass(campo[1], row.getString(key), Color.parseColor(campo[2])));
                        }
                        contador++;
                    }
                }

                ListViewAdapter adapter = new ListViewAdapter(getActivity(), listaInformacoes, new ListViewAdapter.Listener() {
                    @Override
                    public void onGrab(int position, RelativeLayout row) {
                        listView.onGrab(position, row);
                    }
                });

                listView.setAdapter(adapter);
                listView.setListener(new CustomListView.Listener() {
                    @Override
                    public void swapElements(int indexOne, int indexTwo) {
                        DadosClass temp = listaInformacoes.get(indexOne);
                        listaInformacoes.set(indexOne, listaInformacoes.get(indexTwo));
                        listaInformacoes.set(indexTwo, temp);
                    }
                });

                txtMensagem.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception erro) {
            }
        }
    }

    private void abrirArquivo() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        startActivityForResult(intent, 991);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == 991 && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                carregarJson(uri);
            }
        }
    }
}