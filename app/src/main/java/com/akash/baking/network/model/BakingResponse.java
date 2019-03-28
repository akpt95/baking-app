package com.akash.baking.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class BakingResponse implements Parcelable {

    @JsonProperty("id")
    private double id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("ingredients")
    private List<Ingredients> ingredients;

    @JsonProperty("steps")
    private List<Steps> steps;

    @JsonProperty("image")
    private String image;

    @JsonProperty("servings")
    private int servings;

    public BakingResponse(){

    }


    public BakingResponse(Parcel source) {
        id = source.readDouble();
        name = source.readString();
        ingredients = new ArrayList<Ingredients>();
        source.readList(ingredients,Ingredients.class.getClassLoader());
        steps = new ArrayList<Steps>();
        source.readList(steps,Steps.class.getClassLoader());
        servings = source.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(id);
        dest.writeString(name);
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeInt(servings);

    }


    public static final Parcelable.Creator<BakingResponse> CREATOR = new Parcelable.Creator<BakingResponse>(){

        @Override
        public BakingResponse createFromParcel(Parcel source) {
            return new BakingResponse(source);
        }

        @Override
        public BakingResponse[] newArray(int size) {
            return new BakingResponse[0];
        }
    };

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> mIngredients) {
        this.ingredients = mIngredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> mSteps) {
        this.steps = mSteps;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }
}
