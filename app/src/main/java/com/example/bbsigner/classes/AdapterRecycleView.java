package com.example.bbsigner.classes;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bbsigner.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AdapterRecycleView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<AssinaturaDados> items;
    private OnNoteListener mOnNoteListener;

    public AdapterRecycleView(Context context, ArrayList<AssinaturaDados> items, OnNoteListener onNoteListener) {
        this.context = context;
        this.items = items;
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.custom_recycle, parent, false);
        Item item = new Item(row,mOnNoteListener);
        return item;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String datahora = "";
        Date data = null;
        try {
            data = simpleDateFormat.parse(items.get(position).getAssinaturadata());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert data != null;
        datahora = new SimpleDateFormat("dd:MM:yyyy-HH:mm:ss").format(data);

        ((Item) holder).txtOutro.setText(items.get(position).getOutro());
        ((Item) holder).txtDatahora.setText(datahora);
        ((Item) holder).txtDescricao.setText(items.get(position).getDescricao());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Item extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtOutro, txtDatahora, txtDescricao;
        OnNoteListener onNoteListener;

        public Item(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            txtDatahora = itemView.findViewById(R.id.datahora);
            txtDescricao = itemView.findViewById(R.id.descricao);
            txtOutro = itemView.findViewById(R.id.outro);

            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
