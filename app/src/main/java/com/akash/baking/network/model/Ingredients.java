package com.akash.baking.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ingredients implements Parcelable {

    @JsonProperty("quantity")
    private double quantity;

    @JsonProperty("measure")
    private String measure;

    @JsonProperty("ingredient")
    private String ingredient;

    public static final Parcelable.Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel source) {
            return new Ingredients(source);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[0];
        }
    };

    public Ingredients(){

    }

    public Ingredients(Parcel source) {
        quantity = source.readDouble();
        measure = source.readString();
        ingredient = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);

    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
