package com.akash.baking.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akash.baking.R;
import com.akash.baking.network.model.BakingResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<BakingResponse> bakingResponseList;
    private Context context;
    private OnItemClickListner itemClickListner;

    RecipeAdapter(List<BakingResponse> bakingResponseList) {

        this.bakingResponseList = bakingResponseList;

    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_item_recipe, viewGroup, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int position) {

        if (!bakingResponseList.isEmpty()) {

            if (bakingResponseList.get(position).getImage() != null && !TextUtils.isEmpty(bakingResponseList.get(position).getImage())) {

                if (context != null)
                    Picasso.with(context).load(bakingResponseList.get(position).getImage()).into(recipeViewHolder.imageRecipeItem);
            }

            recipeViewHolder.textRecipeItem.setText(bakingResponseList.get(position).getName());
            recipeViewHolder.textRecipeItemServings.setText(String.format("Servings : %s", String.valueOf(bakingResponseList.get(position).getServings())));
        }

    }

    @Override
    public int getItemCount() {

        if (bakingResponseList != null) {
            return bakingResponseList.size();

        } else
            return 0;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageRecipeItem;
        private TextView textRecipeItem;
        private TextView textRecipeItemServings;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            imageRecipeItem = itemView.findViewById(R.id.image_recipe_item);
            textRecipeItem = itemView.findViewById(R.id.text_recipe_item);
            textRecipeItemServings = itemView.findViewById(R.id.text_recipe_item_servings);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListner.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListner {
        void onItemClick(View view, int position);
    }

    void setItemClickListner(OnItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

}
