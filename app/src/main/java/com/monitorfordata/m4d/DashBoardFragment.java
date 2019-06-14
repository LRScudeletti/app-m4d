package com.monitorfordata.m4d;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

@SuppressWarnings("ALL")
public class DashBoardFragment extends Fragment {

    PieChartView pieChartView1;
    PieChartView pieChartView2;
    PieChartView pieChartView3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        pieChartView1 = rootView.findViewById(R.id.chart1);
        pieChartView2 = rootView.findViewById(R.id.chart2);
        pieChartView3 = rootView.findViewById(R.id.chart3);

        return rootView;
    }

    public void carregarGraficos(List<DadosClass> listaCharts) {

        try {

            double valor1 = Math.round(Float.parseFloat(listaCharts.get(1).informacao) * 100) / 100.0;
            double valor2 = Math.round(Float.parseFloat(listaCharts.get(2).informacao) * 100) / 100.0;
            double valor3 = Math.round(Float.parseFloat(listaCharts.get(3).informacao) * 100) / 100.0;

            double valor1b = 100 - valor1;
            valor1b = Math.round(valor1b * 100) / 100.0;

            double valor2b = 100 - valor2;
            valor2b = Math.round(valor2b * 100) / 100.0;

            double valor3b = 100 - valor3;
            valor3b = Math.round(valor3b * 100) / 100.0;

            List pieData1 = new ArrayList<>();
            pieData1.add(new SliceValue((float) valor1, Color.parseColor("#FF0000")).setLabel(valor1 + "%"));
            pieData1.add(new SliceValue((float) valor1b, Color.parseColor("#32CD32")).setLabel(valor1b + "%"));

            List pieData2 = new ArrayList<>();
            pieData2.add(new SliceValue((float) valor2, Color.parseColor("#FF0000")).setLabel(valor2 + "%"));
            pieData2.add(new SliceValue((float) valor2b, Color.parseColor("#32CD32")).setLabel(valor2b + "%"));

            List pieData3 = new ArrayList<>();
            pieData3.add(new SliceValue((float) valor3, Color.parseColor("#FF0000")).setLabel(valor3 + "%"));
            pieData3.add(new SliceValue((float) valor3b, Color.parseColor("#32CD32")).setLabel(valor3b + "%"));

            PieChartData pieChartData1 = new PieChartData(pieData1);
            pieChartData1.setHasLabels(true).setValueLabelTextSize(14);
            pieChartData1.setHasCenterCircle(true).setCenterText1(listaCharts.get(1).campo).setCenterText1FontSize(12).setCenterText1Color(Color.parseColor("#000000"));

            PieChartData pieChartData2 = new PieChartData(pieData2);
            pieChartData2.setHasLabels(true).setValueLabelTextSize(14);
            pieChartData2.setHasCenterCircle(true).setCenterText1(listaCharts.get(2).campo).setCenterText1FontSize(12).setCenterText1Color(Color.parseColor("#000000"));

            PieChartData pieChartData3 = new PieChartData(pieData3);
            pieChartData3.setHasLabels(true).setValueLabelTextSize(14);
            pieChartData3.setHasCenterCircle(true).setCenterText1(listaCharts.get(3).campo).setCenterText1FontSize(12).setCenterText1Color(Color.parseColor("#000000"));

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
}