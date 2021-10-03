package com.maboe.foodrecipes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.maboe.foodrecipes.models.Recipe;
import com.maboe.foodrecipes.viewmodels.RecipeViewModel;

public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";
    AppCompatImageView mRecipeImage;
    TextView mRecipeTitle;
    TextView mRecipeRank;
    ScrollView mScrollView;
    LinearLayout mRecipeIngredientsContainer;

    private RecipeViewModel mRecipeViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeRank = findViewById(R.id.recipe_social_score);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);

        mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        showProgressBar(true);
        subscribeObservers();
        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            Log.d(TAG, "getIncomingIntent: " + recipe.getTitle());
            mRecipeViewModel.searchRecipeById(recipe.getRecipe_id());
        }
    }

    private void subscribeObservers() {
        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if (recipe != null) {
                    if (recipe.getRecipe_id().equals(mRecipeViewModel.getRecipeId())) {
                        setRecipeProperties(recipe);
                        mRecipeViewModel.setRetrievedRecipe(true);
                        Log.d(TAG, "onChanged:TEST " + recipe);
                    }
                }
            }
        });

        mRecipeViewModel.isRecipeRequestTimedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean && !mRecipeViewModel.didRetrieveRecipe()) {
                    Log.d(TAG, "onChanged: Time out...");
                    displayErrorScreen("Error Retrieving Data Check Network Connection...");
                }
            }
        });
    }

    public void displayErrorScreen(String errorMessage) {
        mRecipeTitle.setText(R.string.error_retrieving_recipe);
        mRecipeRank.setText("");
        TextView textView = new TextView(this);
        if (!errorMessage.equals("")) {
            textView.setText(errorMessage);
        } else {
            textView.setText(R.string.error);
        }
        textView.setTextSize(15);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        mRecipeIngredientsContainer.addView(textView);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.loading);
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(R.drawable.loading)
                .into(mRecipeImage);

        showParent();
        showProgressBar(false);
    }

    private void setRecipeProperties(Recipe recipe) {
        if (recipe.getIngredients() == null) {
            mRecipeIngredientsContainer.removeAllViews();
            displayErrorScreen("Error Retrieving Data Check Network Connection...");
            Toast.makeText(this, "Error!, Try Later", Toast.LENGTH_LONG).show();
        } else if (recipe != null) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.loading);

            String fullUrl = recipe.getImage_url();

            StringBuilder myUrl = new StringBuilder(fullUrl);
            myUrl.insert(4, 's');

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(myUrl.toString())
                    .into(mRecipeImage);
            Log.d(TAG, "setRecipePropertiesIMAGE: " + recipe.getImage_url());
            mRecipeTitle.setText(recipe.getTitle());
            mRecipeRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

            mRecipeIngredientsContainer.removeAllViews();
            for (String ingredient : recipe.getIngredients()) {
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                mRecipeIngredientsContainer.addView(textView);
            }
            showParent();
            showProgressBar(false);
        }
    }

    private void showParent() {
        mScrollView.setVisibility(View.VISIBLE);
    }
}
