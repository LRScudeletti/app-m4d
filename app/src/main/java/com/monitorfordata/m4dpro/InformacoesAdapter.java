package com.monitorfordata.m4dpro;

//region [ IMPORTS ]
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
//endregion

public class InformacoesAdapter extends RecyclerView.Adapter<InformacoesAdapter.ViewHolder> {

    //region [ VARIAVEIS ]
    private final ArrayList<InformacoesClass> informacoesClasses;

    // private RecyclerViewButtonClickInterface recyclerViewButtonClickInterface;
    // private RecyclerViewImageButtonClickInterface recyclerViewImageButtonClickInterface;
    //endregion

    InformacoesAdapter(ArrayList<InformacoesClass> linhaItemRedesSociaisClass) {
        this.informacoesClasses = linhaItemRedesSociaisClass;
    }

    //region [ EVENTOS ]
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_informacao, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final InformacoesClass row_pos = informacoesClasses.get(position);

        holder.txtNome.setText(row_pos.getNome());
        holder.txtInformacao.setText(row_pos.getInformacao());
        holder.vCor.setBackgroundColor(row_pos.getCor());

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return informacoesClasses.size();
    }
    //endregion

    /*
    //region [ METODOS ]
    void setRecyclerViewButtonClickClass(RecyclerViewButtonClickInterface recyclerViewButtonClickInterface) {
        this.recyclerViewButtonClickInterface = recyclerViewButtonClickInterface;
    }

    void setRecyclerViewImageButtonClickClass(RecyclerViewImageButtonClickInterface recyclerViewImageButtonClickInterface) {
        this.recyclerViewImageButtonClickInterface = recyclerViewImageButtonClickInterface;
    }
    //endregion
    */

    //region [ CLASSES ]
    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView txtNome;
        final TextView txtInformacao;
        final View vCor;

        ViewHolder(View itemView) {
            super(itemView);

            txtNome = itemView.findViewById(R.id.txtNome);
            txtInformacao = itemView.findViewById(R.id.txtInformacao);
            vCor = itemView.findViewById(R.id.vCor);
        }
    }
    //endregion
}