package com.maboe.foodrecipes.requests;

import com.maboe.foodrecipes.requests.responses.RecipeResponse;
import com.maboe.foodrecipes.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    //Search
    @GET("/api/search")
//    @GET("/api/recipe/search/")
    Call<RecipeSearchResponse> searchRecipe(
            @Query("key") String key, //?
            @Query("q") String query, //&
            @Query("page") String page
    );

    //Get Recipe Request
    @GET("/api/get")
    Call<RecipeResponse> getRecipe(
            @Query("key") String key, //?
            @Query("rId") String recipe_id
            );
}
