package com.akash.baking.recipeDetail;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akash.baking.R;
import com.akash.baking.network.model.BakingResponse;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();

    private List<BakingResponse> bakingResponseList;
    private int recipePosition;
    private boolean isTabView;

    private TextView textToolbarTitle;
    private ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        bakingResponseList = getIntent().getParcelableArrayListExtra("baking_response");
        recipePosition = getIntent().getIntExtra("recipe_position", 0);

        textToolbarTitle = findViewById(R.id.text_toolbar_title);
        imageBack = findViewById(R.id.image_back);

        setUpUI();

    }

    private void setUpUI() {
        textToolbarTitle.setText(bakingResponseList.get(recipePosition).getName());
        imageBack.setVisibility(View.VISIBLE);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("baking_response", (ArrayList<? extends Parcelable>) bakingResponseList);
        bundle.putInt("recipe_position", recipePosition);
        recipeDetailFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_detail_fragment_container, recipeDetailFragment)
                .commit();

        if(findViewById(R.id.layout_recipe_detail_tab)!= null){
            isTabView = true;
            launchRecipeStepDetailFragment(0);

        } else {
            isTabView = false;
        }
    }

    public void launchRecipeStepDetailFragment(int position){

        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("steps",
                (ArrayList<? extends Parcelable>) bakingResponseList.get(recipePosition).getSteps());
        bundle.putInt("recipe_step_position", position);
        bundle.putBoolean("is_tab_view", isTabView);
        recipeStepDetailFragment.setArguments(bundle);

        if (isTabView)
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_detail_fragment_container, recipeStepDetailFragment)
                    .commit();

        else
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_detail_fragment_container, recipeStepDetailFragment)
                    .addToBackStack(null)
                    .commit();
    }

}
