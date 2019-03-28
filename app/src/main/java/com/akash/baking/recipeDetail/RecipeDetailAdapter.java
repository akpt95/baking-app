package com.akash.baking.recipeDetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akash.baking.R;
import com.akash.baking.network.model.Ingredients;
import com.akash.baking.network.model.Steps;

import java.util.List;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String viewType;
    private List<Ingredients> ingredientsList;
    private List<Steps> stepsList;
    OnStepItemClickListner stepItemClickListner;

    RecipeDetailAdapter(String viewType, List<Ingredients> ingredientsList, List<Steps> stepsList){

        this.viewType = viewType;
        this.ingredientsList = ingredientsList;
        this.stepsList = stepsList;
    }

    @Override
    public int getItemViewType(int position) {
        if (viewType.equalsIgnoreCase("ingredients"))
            return 0;
        else
            return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        if(viewType == 0){
            View view = inflater.inflate(R.layout.recycler_view_item_ingredients, viewGroup, false);
            return new ViewHolderIngredients(view);

        } else {
            View view = inflater.inflate(R.layout.recycler_view_item_steps, viewGroup, false);
            return new ViewHolderSteps(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder.getItemViewType() == 0){

            ViewHolderIngredients viewHolderIngredients = (ViewHolderIngredients) viewHolder;

            if(ingredientsList != null){
                String ingredientItem = String.valueOf(ingredientsList.get(position).getQuantity()
                        + " " + ingredientsList.get(position).getMeasure()
                        + " " + ingredientsList.get(position).getIngredient());

                viewHolderIngredients.textIngredient.setText(ingredientItem);
            }


        } else {

            ViewHolderSteps viewHolderSteps = (ViewHolderSteps) viewHolder;

            if(stepsList != null){
                viewHolderSteps.textStep.setText(stepsList.get(position).getShortDescription());
            }

        }
    }

    @Override
    public int getItemCount() {

        int listSize = 0;

        if(viewType.equalsIgnoreCase("ingredients")){

            if(ingredientsList != null){
                listSize = ingredientsList.size();
            }

        } else {

            if(stepsList != null){
                listSize = stepsList.size();
            }
        }

        return listSize;
    }

    private class ViewHolderIngredients extends RecyclerView.ViewHolder {

        private TextView textIngredient;

        ViewHolderIngredients(@NonNull View itemView) {
            super(itemView);

            textIngredient = itemView.findViewById(R.id.text_ingredient);
        }
    }

    private class ViewHolderSteps extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textStep;

        ViewHolderSteps(@NonNull View itemView) {
            super(itemView);
            textStep = itemView.findViewById(R.id.text_step);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            stepItemClickListner.onStepItemClick(v, getAdapterPosition());
        }
    }

    public interface OnStepItemClickListner {
        void onStepItemClick(View view, int position);
    }

    void setStepItemClickListner(OnStepItemClickListner stepItemClickListner){
        this.stepItemClickListner = stepItemClickListner;
    }
}
