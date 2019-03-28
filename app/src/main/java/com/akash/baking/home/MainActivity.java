package com.akash.baking.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.akash.baking.R;
import com.akash.baking.network.RetrofitController;
import com.akash.baking.network.model.BakingResponse;
import com.akash.baking.recipeDetail.RecipeDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnItemClickListner {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerViewRecipe;
    private List<BakingResponse> bakingResponseList;

    private ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewRecipe = findViewById(R.id.recycler_view_recipe);

        imageBack = findViewById(R.id.image_back);
        imageBack.setVisibility(View.GONE);

        if(findViewById(R.id.layout_tab_home) != null) {

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            recyclerViewRecipe.setLayoutManager(gridLayoutManager);

        } else {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerViewRecipe.setLayoutManager(linearLayoutManager);
        }

        getData();

    }

    private void getData() {

        final Call<List<BakingResponse>> bakingResponseCall = RetrofitController.getInstance().getBakingAPI().getBakingResponse();

        bakingResponseCall.enqueue(new Callback<List<BakingResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<BakingResponse>> call, @NonNull Response<List<BakingResponse>> response) {

                if(response.isSuccessful()){

                    if(response.body()!=null){
                        bakingResponseList = response.body();

                        RecipeAdapter recipeAdapter = new RecipeAdapter(bakingResponseList);
                        recipeAdapter.setItemClickListner(MainActivity.this);
                        recyclerViewRecipe.setAdapter(recipeAdapter);

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BakingResponse>> call, @NonNull Throwable t) {
                Log.e(TAG, "Baking response failed with error: "+t.getMessage());
                Toast.makeText(MainActivity.this, "No response from server", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putParcelableArrayListExtra("baking_response", (ArrayList<? extends Parcelable>) bakingResponseList);
        intent.putExtra("recipe_position", position);
        startActivity(intent);
    }
}
