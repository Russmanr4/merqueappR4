package com.example.merqueapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.merqueapp.R;
import com.example.merqueapp.models.Pokemon;

import java.util.ArrayList;


public class ListaPokemonAdapter extends RecyclerView.Adapter<ListaPokemonAdapter.ViewHolder>{

    private ArrayList<Pokemon> dataset; //designa el valor del nombre del pokemon
    private Context context;


    public ListaPokemonAdapter(Context context) {
        this.context=context;
        dataset = new ArrayList<>();
    }

    @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Pokemon p = dataset.get(position);//al item
            holder.nombreTextView.setText(p.getName());
            Glide.with(context)//con la que optenemos las imagenes
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + p.getNumber() + ".png")
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.fotoImageView);

        }

        @Override
        public int getItemCount() {
            return dataset.size();
        }

    public void adicionarListaPokemon(ArrayList<Pokemon> listapokemon) {
        dataset.addAll(listapokemon);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

            private ImageView fotoImageView;
            private TextView nombreTextView;

        public ViewHolder(View itemView){
            super(itemView);

            fotoImageView = itemView.findViewById(R.id.fotoImageView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);

        }

    }

}