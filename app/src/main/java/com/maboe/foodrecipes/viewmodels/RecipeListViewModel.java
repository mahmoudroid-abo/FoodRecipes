package com.maboe.foodrecipes.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.maboe.foodrecipes.models.Recipe;
import com.maboe.foodrecipes.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private static final String TAG = "RecipeListViewModel";
    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipes;
    private boolean mIsPerformingQuery;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mIsPerformingQuery = false;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int pageNumber) {
        mIsViewingRecipes = true;
        mIsPerformingQuery = true;
        mRecipeRepository.searchRecipesApi(query, pageNumber);
    }

    public LiveData<Boolean> isQueryExhausted() {
        return mRecipeRepository.isQueryExhausted();
    }

    public void setIsViewingRecipes(boolean isViewingRecipes) {
        mIsViewingRecipes = isViewingRecipes;
    }

    public void setIsPerformingQuery(boolean isPerformingQuery) {
        mIsPerformingQuery = isPerformingQuery;
    }

    public boolean isPerformingQuery() {
        return mIsPerformingQuery;
    }

    public boolean isViewingRecipes() {
        return mIsViewingRecipes;
    }

    public void searchNextPage() {
        if (!mIsPerformingQuery
                && mIsViewingRecipes
                && !isQueryExhausted().getValue()) {
            mRecipeRepository.searchNextPage();
        }
    }



    public boolean onBackPressed() {
        if (mIsPerformingQuery) {
            mRecipeRepository.cancelRequest();
            mIsPerformingQuery = false;
        }
        if (mIsViewingRecipes) {
            mIsViewingRecipes = false;
            return false;
        }
        return true;
    }
}
