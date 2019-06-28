package com.monitorfordata.m4d;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

import static android.content.Context.MODE_PRIVATE;

@SuppressWarnings("ALL")
public class DashBoardFragment extends Fragment {

    LinearLayout llPrincipal;

    PieChartView pieChartView1;
    PieChartView pieChartView2;
    PieChartView pieChartView3;

    TextView txt01;
    TextView txt02;
    TextView txt03;

    private int intervaloAtualizacao = 0;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        llPrincipal = rootView.findViewById(R.id.llPrincipal);

        txt01 = rootView.findViewById(R.id.txt01);
        txt02 = rootView.findViewById(R.id.txt02);
        txt03 = rootView.findViewById(R.id.txt03);

        pieChartView1 = rootView.findViewById(R.id.chart1);
        pieChartView1.setInteractive(false);
        pieChartView2 = rootView.findViewById(R.id.chart2);
        pieChartView2.setInteractive(false);
        pieChartView3 = rootView.findViewById(R.id.chart3);
        pieChartView3.setInteractive(false);

        mHandler = new Handler();

        SharedPreferences intervaloAuto = getActivity().getSharedPreferences("preferencias", MODE_PRIVATE);
        String intervalo = intervaloAuto.getString("intervalo_Auto", "");

        if(!intervalo.equals("0") && !intervalo.equals("")){
            intervaloAtualizacao =  Integer.parseInt(intervalo) * 1000;
            iniciarAtualizacao();
        }

        return rootView;
    }

    public void carregarGraficos(List<InformacoesClass> listaCharts) {
        try {
            llPrincipal.setVisibility(View.VISIBLE);

            txt01.setText(listaCharts.get(1).getNome());
            txt02.setText(listaCharts.get(2).getNome());
            txt03.setText(listaCharts.get(3).getNome());

            double valor1 = Math.round(Float.parseFloat(listaCharts.get(1).getInformacao()) * 100) / 100.0;
            double valor2 = Math.round(Float.parseFloat(listaCharts.get(2).getInformacao()) * 100) / 100.0;
            double valor3 = Math.round(Float.parseFloat(listaCharts.get(3).getInformacao()) * 100) / 100.0;

            double valor1b = 100 - valor1;
            valor1b = Math.round(valor1b * 100) / 100.0;

            double valor2b = 100 - valor2;
            valor2b = Math.round(valor2b * 100) / 100.0;

            double valor3b = 100 - valor3;
            valor3b = Math.round(valor3b * 100) / 100.0;

            List pieData1 = new ArrayList<>();
            pieData1.add(new SliceValue((float) valor1, Color.parseColor("#ED5565")).setLabel(valor1 + "%"));
            pieData1.add(new SliceValue((float) valor1b, Color.parseColor("#48CFAD")).setLabel(valor1b + "%"));

            List pieData2 = new ArrayList<>();
            pieData2.add(new SliceValue((float) valor2, Color.parseColor("#ED5565")).setLabel(valor2 + "%"));
            pieData2.add(new SliceValue((float) valor2b, Color.parseColor("#48CFAD")).setLabel(valor2b + "%"));

            List pieData3 = new ArrayList<>();
            pieData3.add(new SliceValue((float) valor3, Color.parseColor("#ED5565")).setLabel(valor3 + "%"));
            pieData3.add(new SliceValue((float) valor3b, Color.parseColor("#48CFAD")).setLabel(valor3b + "%"));

            PieChartData pieChartData1 = new PieChartData(pieData1);
            pieChartData1.setHasLabels(true).setValueLabelTextSize(14);

            PieChartData pieChartData2 = new PieChartData(pieData2);
            pieChartData2.setHasLabels(true).setValueLabelTextSize(14);

            PieChartData pieChartData3 = new PieChartData(pieData3);
            pieChartData3.setHasLabels(true).setValueLabelTextSize(14);

            pieChartView1.setPieChartData(pieChartData1);
            pieChartView2.setPieChartData(pieChartData2);
            pieChartView3.setPieChartData(pieChartData3);
        } catch (Exception erro) {
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (new InformacoesFragment().listaCharts.size() != 0)
                carregarGraficos(new InformacoesFragment().listaCharts);
        }
    }

    Runnable checkEstado = new Runnable() {
        @Override
        public void run() {
            try {
                carregarGraficos(new InformacoesFragment().listaCharts);
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