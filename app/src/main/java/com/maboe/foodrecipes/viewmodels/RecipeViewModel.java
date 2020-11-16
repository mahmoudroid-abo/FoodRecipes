package com.maboe.foodrecipes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.maboe.foodrecipes.models.Recipe;
import com.maboe.foodrecipes.repositories.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private String mRecipeId;
    private boolean mDidRetrieveRecipe;

    public RecipeViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mDidRetrieveRecipe = false;
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipeRepository.getRecipe();
    }

    public void searchRecipeById(String recipeId) {
        mRecipeId = recipeId;
        mRecipeRepository.searchRecipeById(recipeId);
    }

    public String getRecipeId() {
        return mRecipeId;
    }

    public void setRetrievedRecipe(boolean retrievedRecipe) {
        mDidRetrieveRecipe = retrievedRecipe;
    }

    public boolean didRetrieveRecipe() {
        return mDidRetrieveRecipe;
    }

    public LiveData<Boolean> isRecipeRequestTimedOut() {
        return mRecipeRepository.isRecipeRequestTimedOut();
    }
}
