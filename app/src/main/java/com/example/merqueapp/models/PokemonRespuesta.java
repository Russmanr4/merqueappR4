package com.example.merqueapp.models;

//esta clase maneja la respuesta con su informacion de atributos de un array

import java.util.ArrayList;

public class PokemonRespuesta {
    private ArrayList<Pokemon> results;

    public ArrayList<Pokemon> getResults() {
        return results;
    }

    public void setResults(ArrayList<Pokemon> results) {
        this.results = results;
    }
}




