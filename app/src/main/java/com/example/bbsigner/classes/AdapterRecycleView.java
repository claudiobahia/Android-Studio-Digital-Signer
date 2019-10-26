package com.example.bbsigner.classes;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bbsigner.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterRecycleView extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<AssinaturaDados> items;
    private ArrayList<AssinaturaDados> itemsFiltered;
    private OnNoteListener mOnNoteListener;
    private OnLongNoteListener mOnLongNoteListener;

    public AdapterRecycleView(Context context, ArrayList<AssinaturaDados> items,
                              OnNoteListener onNoteListener, OnLongNoteListener onLongNoteListener) {
        this.context = context;
        this.items = items;
        this.mOnNoteListener = onNoteListener;
        this.itemsFiltered = items;
        this.mOnLongNoteListener = onLongNoteListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.custom_recycle, parent, false);
        Item item = new Item(row, mOnNoteListener, mOnLongNoteListener);
        return item;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((Item) holder).linearLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_anim));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String datahora = "";
        Date data = null;
        try {
            data = simpleDateFormat.parse(itemsFiltered.get(position).getAssinaturadata());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert data != null;
        datahora = new SimpleDateFormat("dd:MM:yyyy-HH:mm:ss").format(data);

        ((Item) holder).txtOutro.setText(itemsFiltered.get(position).getOutro());
        ((Item) holder).txtDatahora.setText(datahora);
        ((Item) holder).txtDescricao.setText(itemsFiltered.get(position).getDescricao());
    }

    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String Key = charSequence.toString();
                if (Key.isEmpty()) {
                    itemsFiltered = items;
                } else {
                    ArrayList<AssinaturaDados> listFiltered = new ArrayList<>();
                    for (AssinaturaDados dado : items) {
                        if (dado.getOutro().toLowerCase().contains(Key.toLowerCase())) {
                            listFiltered.add(dado);
                        }
                    }
                    itemsFiltered = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemsFiltered = (ArrayList<AssinaturaDados>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    public class Item extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView txtOutro, txtDatahora, txtDescricao;
        private OnNoteListener onNoteListener;
        private LinearLayout linearLayout;
        private OnLongNoteListener onLongNoteListener;


        public Item(View itemView, OnNoteListener onNoteListener, OnLongNoteListener onLongNoteListener) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.meuItemRecycleView);

            txtDatahora = itemView.findViewById(R.id.datahora);
            txtDescricao = itemView.findViewById(R.id.descricao);
            txtOutro = itemView.findViewById(R.id.outro);

            this.onLongNoteListener = onLongNoteListener;
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            onLongNoteListener.onLongNoteClick(getAdapterPosition());
            return false;
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    public interface OnLongNoteListener {
        void onLongNoteClick(int position);
    }
}
