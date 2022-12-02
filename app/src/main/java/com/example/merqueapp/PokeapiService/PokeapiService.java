package com.example.merqueapp.PokeapiService;

import com.example.merqueapp.models.PokemonRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;


public interface PokeapiService {
    @GET("Pokemon")
    Call<PokemonRespuesta> obtenerListaPokemon();
}
