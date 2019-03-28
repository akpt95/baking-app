package com.akash.baking.recipeDetail;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.akash.baking.BakingWidgetProvider;
import com.akash.baking.R;
import com.akash.baking.network.model.BakingResponse;
import com.akash.baking.network.model.Ingredients;
import com.akash.baking.network.model.Steps;

import java.util.List;


public class RecipeDetailFragment extends Fragment implements RecipeDetailAdapter.OnStepItemClickListner {

    private List<BakingResponse> bakingResponseList;
    private int recipePosition;
    private List<Ingredients> ingredientsList;
    private List<Steps> stepsList;

    private RecyclerView recyclerViewIngredients;
    private RecyclerView recyclerViewSteps;

    private RecipeDetailActivity recipeDetailActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        recipeDetailActivity = (RecipeDetailActivity) getActivity();

        Bundle bundle = getArguments();
        if (bundle != null) {
            bakingResponseList = bundle.getParcelableArrayList("baking_response");
            recipePosition = bundle.getInt("recipe_position", 0);
        }

        if(bakingResponseList != null){
            ingredientsList = bakingResponseList.get(recipePosition).getIngredients();
            stepsList = bakingResponseList.get(recipePosition).getSteps();
        }

        recyclerViewIngredients = view.findViewById(R.id.recycler_view_ingredients);
        recyclerViewSteps = view.findViewById(R.id.recycler_view_steps);

        recyclerViewIngredients.setNestedScrollingEnabled(false);
        recyclerViewSteps.setNestedScrollingEnabled(false);

        setUpRecyclerViews();
        updateWidget(recipeDetailActivity);

        return view;
    }

    private void setUpRecyclerViews() {

        if(ingredientsList != null){
            LinearLayoutManager layoutManagerIngredients = new LinearLayoutManager(getContext());
            recyclerViewIngredients.setLayoutManager(layoutManagerIngredients);
            RecipeDetailAdapter recipeDetailAdapterIngredients = new RecipeDetailAdapter("ingredients", ingredientsList, null);
            recyclerViewIngredients.setAdapter(recipeDetailAdapterIngredients);
        }

        if(stepsList != null){
            LinearLayoutManager layoutManagerSteps = new LinearLayoutManager(getContext());
            recyclerViewSteps.setLayoutManager(layoutManagerSteps);
            RecipeDetailAdapter recipeDetailAdapterSteps = new RecipeDetailAdapter("steps", null, stepsList);
            recipeDetailAdapterSteps.setStepItemClickListner(this);
            recyclerViewSteps.setAdapter(recipeDetailAdapterSteps);

        }
    }

    @Override
    public void onStepItemClick(View view, int position) {

        recipeDetailActivity.launchRecipeStepDetailFragment(position);

    }

    private void updateWidget(Context context){
        AppWidgetManager appWidgetManager  = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        String recipe="";
        String ingredients = "";
        recipe = bakingResponseList.get(recipePosition).getName();
        for(int i = 0;i<ingredientsList.size();i++){

            ingredients = String.format("%s\n%s %s %s", ingredients, ingredientsList.get(i).getQuantity(), ingredientsList.get(i).getMeasure(), ingredientsList.get(i).getIngredient());

        }
        remoteViews.setTextViewText(R.id.widget_text_recipe_ingredients, ingredients);
        remoteViews.setTextViewText(R.id.widget_text_recipe, recipe);
        ComponentName componentName = new ComponentName(context,BakingWidgetProvider.class);
        int[] widgetId = appWidgetManager.getAppWidgetIds(componentName);
        BakingWidgetProvider.updateTextWidgets(context,appWidgetManager, ingredients, recipe,widgetId);
    }
}
