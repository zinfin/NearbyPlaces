package com.esri.android.nearbyplaces.data;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import com.esri.android.nearbyplaces.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sand8529 on 7/7/16.
 */
public class CategoryHelper {
  static final List<String> foodTypes = Arrays.asList(
        "African Food",
        "American Food",
        "Argentinean Food",
        "Australian Food",
        "Austrian Food",
        "Bakery",
        "BBQ and Southern Food",
        "Belgian Food",
        "Bistro",
        "Brazilian Food",
        "Breakfast",
        "Brewpub",
        "British Isles Food",
        "Burgers",
        "Cajun and Creole Food",
        "Californian Food",
        "Caribbean Food",
        "Chicken Restaurant",
        "Chilean Food",
        "Chinese Food",
        "Continental Food",
        "Creperie",
        "East European Food",
        "Fast Food",
        "Filipino Food",
        "Fondue",
        "French Food",
        "Fusion Food",
        "German Food",
        "Greek Food",
        "Grill",
        "Hawaiian Food",
        "Ice Cream Shop",
        "Indian Food",
        "Indonesian Food",
        "International Food",
        "Irish Food",
        "Italian Food",
        "Japanese Food",
        "Korean Food",
        "Kosher Food",
        "Latin American Food",
        "Malaysian Food",
        "Mexican Food",
        "Middle Eastern Food",
        "Moroccan Food",
        "Other Restaurant",
        "Pastries",
        "Polish Food",
        "Portuguese Food",
        "Russian Food",
        "Sandwich Shop",
        "Scandinavian Food",
        "Seafood",
        "Snacks",
        "South American Food",
        "Southeast Asian Food",
        "Southwestern Food",
        "Spanish Food",
        "Steak House",
        "Sushi",
        "Swiss Food",
        "Tapas",
        "Thai Food",
        "Turkish Food",
        "Vegetarian Food",
        "Vietnamese Food",
        "Winery");

  public static String getCategoryForFoodType(String type){
    String category = type;
    if (foodTypes.contains(type)){
      category = "Food";
    }
    return category;
  }
  /**
   * Assign appropriate drawable given place type
   * @param p - Place
   * @return - Drawable
   */
  public static Integer getPinForPlace(Place p){
    String category = CategoryHelper.getCategoryForFoodType(p.getType());
    Integer d = null;
    switch (category){
      case "Pizza":
        d =  R.drawable.pizza_pin;
        break;
      case "Hotel":
        d =  R.drawable.hotel_pin;
        break;
      case "Food":
        d = R.drawable.restaurant_pin;
        break;
      case "Bar or Pub":
        d =  R.drawable.bar_pin;
        break;
      case "Coffee Shop":
        d = R.drawable.cafe_pin;
        break;
      default:
        d = R.drawable.empty_pin;
    }
    return d;
  }
  public static Integer getPinForCenterPlace(Place p){
    String category = CategoryHelper.getCategoryForFoodType(p.getType());
    Integer d = null;
    switch (category){
      case "Pizza":
        d =  R.drawable.blue_pizza_pin;
        break;
      case "Hotel":
        d =  R.drawable.blue_hotel_pin;
        break;
      case "Food":
        d = R.drawable.blue_rest_pin;
        break;
      case "Bar or Pub":
        d =  R.drawable.blue_bar_pin;
        break;
      case "Coffee Shop":
        d = R.drawable.blue_cafe_pin;
        break;
      default:
        d = R.drawable.blue_empty_pin;
    }
    return d;
  }
  public static Drawable getDrawableForPlace(Place p, Activity a){

    String placeType = p.getType();
    String category =  CategoryHelper.getCategoryForFoodType(placeType);
    Drawable d = null;
    switch (category){
      case "Pizza":
        d = ResourcesCompat.getDrawable(a.getResources(), R.drawable.ic_local_pizza_black_24dp,null);
        break;
      case "Hotel":
        d = ResourcesCompat.getDrawable(a.getResources(), R.drawable.ic_hotel_black_24dp,null);
        break;
      case "Food":
        d = ResourcesCompat.getDrawable(a.getResources(), R.drawable.ic_local_dining_black_24dp,null);
        break;
      case "Bar or Pub":
        d = ResourcesCompat.getDrawable(a.getResources(), R.drawable.ic_local_bar_black_24dp,null);
        break;
      case "Coffee Shop":
        d = ResourcesCompat.getDrawable(a.getResources(), R.drawable.ic_local_cafe_black_24dp,null);
        break;
      default:
        d = ResourcesCompat.getDrawable(a.getResources(), R.drawable.ic_place_black_24dp,null);
    }
    return d;
  }
}
